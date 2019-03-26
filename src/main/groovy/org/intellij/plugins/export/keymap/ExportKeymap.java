package org.intellij.plugins.export.keymap;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import org.intellij.plugins.export.keymap.model.CommonActionsProfile;
import org.intellij.plugins.export.keymap.model.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExportKeymap extends AnAction implements DumbAware {

    public ExportKeymap() {
        getTemplatePresentation().setText("Print keymap");
        getTemplatePresentation().setDescription(
                "Create a PDF document that lists actions and shortcuts from the selected keymap");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Settings settings = Settings.getInstance();
        Keymap[] keymaps = KeymapManagerEx.getInstanceEx().getAllKeymaps();

        String outputPathLabelText = "Output path:";

        TextFieldWithBrowseButton pathControl = new TextFieldWithBrowseButton();

        pathControl.addBrowseFolderListener("Choose Output Path for Keymap's PDF", outputPathLabelText, null,
                FileChooserDescriptorFactory.createSingleFileDescriptor(),
                TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

        ComboBox<Keymap> keymapComboBox = new ComboBox<>(keymaps);

        String outputPath = settings.getOutputPath();

        if (outputPath == null || outputPath.isEmpty())
            outputPath = com.intellij.util.SystemProperties.getUserHome();

        pathControl.setText(outputPath);

        DialogWrapper dialog = new DialogWrapper(PlatformDataKeys.PROJECT.getData(e.getDataContext())) {
            {
                init();
                setTitle("Print Keymap");
            }

            @Override
            protected JComponent createCenterPanel() {
                JPanel dialogPanel = new JPanel(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                JLabel labelKeymap = new JLabel("Keymap:");
                JLabel outputPathLabel = new JLabel(outputPathLabelText);

                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = 0;
                c.ipadx = 10;
                dialogPanel.add(labelKeymap, c);

                c.gridx = 1;
                dialogPanel.add(keymapComboBox, c);

                c.gridx = 0;
                c.gridy = 1;
                dialogPanel.add(outputPathLabel, c);

                c.gridx = 1;
                c.gridy = 1;
                dialogPanel.add(pathControl, c);

                return dialogPanel;
            }
        };

        dialog.show();
        if (!dialog.isOK())
            return;

        Keymap keymap = (Keymap) keymapComboBox.getSelectedItem();


        String path = pathControl.getText().trim();
        settings.setOutputPath(path);
        path = validatePath(path, keymap.getPresentableName());
        if (path == null) return;

        GeneratorFacade.generate(keymap, new CommonActionsProfile(), path);

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(path));
            } catch (IOException ex) {
                showError("Cannot open the generated PDF document.");
            }
        }
    }


    @Nullable
    private String validatePath(@Nullable String path, @NotNull String keymapName) {
        if (path == null) {

            showError("Cannot create PDF document.<br>Reason: output path is undefined");
            return null;
        }

        File file = new File(path);
        if (file.isFile()) {
            return null;
        }
        if (file.isDirectory()) {
            try {
                return file.getCanonicalPath() + "/Keymap-" + keymapName + ".pdf";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<File> pathEntries = new ArrayList<>();
        File parent = file.getParentFile();
        while (parent != null) {
            pathEntries.add(parent);
            parent = parent.getParentFile();
        }

        Collections.reverse(pathEntries);


        for (File entry : pathEntries) {
            if (entry.isFile()) {
                showError(String.format(
                        "Cannot create PDF document at '%s'.<br>Reason: there is a regular file at path '%s'",
                        path, entry.getPath()));
                return null;
            } else if (!entry.exists()) {
                if (!entry.mkdir()) {
                    showError(String.format(
                            "Cannot create PDF document at '%s'.<br>Reason: cannot create directory '%s'",
                            path, entry.getPath()));
                    return null;
                }
            }
        }
        return path;
    }

    public static void showError(@NotNull String text) {
        JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(
                text, MessageType.ERROR, null).setShowCallout(false).createBalloon()
                .show(RelativePoint.fromScreen(MouseInfo.getPointerInfo().getLocation()), Balloon.Position.above);
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }

}
