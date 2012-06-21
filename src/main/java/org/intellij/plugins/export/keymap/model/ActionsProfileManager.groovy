package org.intellij.plugins.export.keymap.model

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

/**
 * @author Denis Zhdanov
 * @since 6/21/12 1:11 PM
 */
class ActionsProfileManager {
  
  static final String DEFAULT_PROFILE_NAME = "CommonActions"
  
  @Nullable
  ActionsProfile getProfile(@NotNull String name) {
    if (name == DEFAULT_PROFILE_NAME) {
      return new CommonActionsProfile()
    }
    null
  }
}
