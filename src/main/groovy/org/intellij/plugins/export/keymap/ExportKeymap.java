package org.intellij.plugins.export.keymap;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.intellij.plugins.export.keymap.model.CommonActionsProfile;
import org.intellij.plugins.export.keymap.model.Settings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ExportKeymap extends AnAction implements DumbAware {

    public ExportKeymap() {
        getTemplatePresentation().setText(Bundle.message("action.print.keymap.text"));
        getTemplatePresentation().setDescription(Bundle.message("action.create.pdf.description"));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Settings settings = Settings.getInstance();
        Keymap[] keymaps = KeymapManagerEx.getInstanceEx().getAllKeymaps();

        String outputPathLabelText = Bundle.message("label.output.path");

        TextFieldWithBrowseButton pathControl = new TextFieldWithBrowseButton();

        pathControl.addBrowseFolderListener(Bundle.message("dialog.title.choose.output.path"),
                outputPathLabelText, null,
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
                setTitle(Bundle.message("dialog.title.print.keymap"));
            }

            @Override
            protected JComponent createCenterPanel() {
                JPanel dialogPanel = new JPanel(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                JLabel labelKeymap = new JLabel(Bundle.message("label.keymap"));
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
        path = Helpers.validatePath(path, keymap.getPresentableName());
        if (path == null) return;

        GeneratorFacade.generate(keymap, new CommonActionsProfile(), path);

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(path));
            } catch (IOException ex) {
                Helpers.showError(Bundle.message("cannot.open.the.generated.pdf.document"));
            }
        }
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }

}
