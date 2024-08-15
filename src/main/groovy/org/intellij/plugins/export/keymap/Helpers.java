package org.intellij.plugins.export.keymap;

import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Helpers {

    public static String getProductName(){
        return ApplicationNamesInfo.getInstance().getFullProductName();
    }

    @Nullable
    public static String validatePath(@Nullable String path, @NotNull String keymapName, @NotNull String format) {
        if (path == null) {
            showError(Bundle.message("cannot.create.pdf.document"));
            return null;
        }

        String fileExtension = format.toLowerCase();
        File file = new File(path);
        if (file.isFile()) {
            return null;
        }
        if (file.isDirectory()) {
            try {
                return file.getCanonicalPath() + "/" + getProductName() + "-" +
                        keymapName + "-keymap." + fileExtension;
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
}
