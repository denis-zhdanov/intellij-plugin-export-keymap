package org.intellij.plugins.export.keymap.model;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Denis Zhdanov
 * @since 6/20/12 3:36 PM
 */
@State(
    name = "KeymapToPDF",
    storages = { @Storage( value = "keymap-to-pdf.xml") }
)
public class Settings implements PersistentStateComponent<Settings> {

  @Nullable private String myOutputPath;
  
  @NotNull
  public static Settings getInstance() {
    return ApplicationManager.getApplication().getService(Settings.class);
  }
  
  @Override
  public Settings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull Settings state) {
    setOutputPath(state.getOutputPath());
  }

  @Nullable
  public String getOutputPath() {
    return myOutputPath;
  }

  public void setOutputPath(@Nullable String outputPath) {
    myOutputPath = outputPath;
  }



}
