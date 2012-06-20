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
)
public class Settings implements PersistentStateComponent<Settings> {

  @Nullable private String myKeymapName;
  @Nullable private String myOutputPathName;
  
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
    setOutputPathName(state.getOutputPathName());
  }

  @Nullable
  public String getKeymapName() {
    return myKeymapName;
  }

  public void setKeymapName(@Nullable String keymapName) {
    myKeymapName = keymapName;
  }

  @Nullable
  public String getOutputPathName() {
    return myOutputPathName;
  }

  public void setOutputPathName(@Nullable String outputPathName) {
    myOutputPathName = outputPathName;
  }
}
