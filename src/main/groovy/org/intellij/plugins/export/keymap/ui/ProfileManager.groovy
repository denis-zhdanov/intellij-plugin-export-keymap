package org.intellij.plugins.export.keymap.ui

import org.jetbrains.annotations.NotNull
import com.intellij.openapi.actionSystem.AnAction

/**
 * @author Denis Zhdanov
 * @since 6/14/12
 */
class ProfileManager {
  
  private def DEFAULT_PROFILE = [ '' ].toSet()
  
  @NotNull
  List<AnAction> applyProfile(@NotNull String profileName, List<AnAction> actions) {
    actions
  }
}
