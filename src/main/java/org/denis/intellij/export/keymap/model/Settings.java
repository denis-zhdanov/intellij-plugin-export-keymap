package org.denis.intellij.export.keymap.model;

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
    storages = { @Storage( file = "$WORKSPACE_FILE$") }
    //storages = { @Storage( file = "$APP_CONFIG$/to-pdf.xml") }
)
public class Settings implements PersistentStateComponent<Settings> {

  @Nullable private String myKeymapName;
  @Nullable private String myOutputPath;
  private boolean myUseMacButtons;
  
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
    setKeymapName(state.getKeymapName());
    setOutputPath(state.getOutputPath());
    setUseMacButtons(state.isUseMacButtons());
  }

  @Nullable
  public String getKeymapName() {
    return myKeymapName;
  }

  public void setKeymapName(@Nullable String keymapName) {
    myKeymapName = keymapName;
  }

  @Nullable
  public String getOutputPath() {
    return myOutputPath;
  }

  public void setOutputPath(@Nullable String outputPath) {
    myOutputPath = outputPath;
  }

  public boolean isUseMacButtons() {
    return myUseMacButtons;
  }

  public void setUseMacButtons(boolean useMacButtons) {
    myUseMacButtons = useMacButtons;
  }
}
