package org.denis.intellij.export.keymap.generator

import org.jetbrains.annotations.NotNull
import com.intellij.openapi.keymap.KeymapManager
import com.intellij.openapi.keymap.ex.KeymapManagerEx

/**
 * @author Denis Zhdanov
 * @since 6/14/12
 */
class GeneratorFacade {

  def generate(@NotNull String keymapName, @NotNull Set<String> actionsProfile, @NotNull String outputPath) {
    def keymap = KeymapManager.instance.getKeymap(keymapName)
    for (actionId in keymap.actionIds) {
      keymap.get
    }
  }
}
