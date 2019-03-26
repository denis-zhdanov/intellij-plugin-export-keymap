package org.intellij.plugins.export.keymap.model;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Denis Zhdanov
 * @since 6/20/12 3:36 PM
 */
@State(
    name = "KeymapToPDF",
    storages = { @Storage( file = "$APP_CONFIG$/keymap-to-pdf.xml") }
)
public class Settings implements PersistentStateComponent<Settings> {

  @Nullable private String myOutputPath;
  
  @NotNull
  public static Settings getInstance() {
    return ServiceManager.getService(Settings.class);
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
