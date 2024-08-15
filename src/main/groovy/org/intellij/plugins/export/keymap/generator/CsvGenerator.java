package org.intellij.plugins.export.keymap.generator;

import org.intellij.plugins.export.keymap.model.ActionData;
import org.intellij.plugins.export.keymap.model.DataEntry;
import org.intellij.plugins.export.keymap.model.Header;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CsvGenerator {
    public void generate(@NotNull ArrayList<DataEntry> data,
                         @NotNull String outputPath,
                         @NotNull String keymapName) {

        try (OutputStream os = new FileOutputStream(outputPath);) {
            // a workaround to support Unicode characters
            os.write(239);
            os.write(187);
            os.write(191);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.append("\"Action\", \"Shortcut\"");
            writer.append("\n");
            for (DataEntry dataEntry : data) {
                if (dataEntry instanceof ActionData) {
                    ActionData actionData = (ActionData) dataEntry;
                    writer.
                            append("\"").
                            append(actionData.getDescription()).
                            append("\",\"").
                            append(actionData.getShortcut()).
                            append("\"");
                }
                else if (dataEntry instanceof Header) {
                    writer.
                            append("\n").
                            append("\"").
                            append(((Header) dataEntry).getName()).
                            append("\"");
                }
                else continue;
                writer.append("\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
