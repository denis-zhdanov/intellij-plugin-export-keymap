package org.denis.intellij.export.keymap.ui

import groovy.swing.SwingBuilder
import org.jetbrains.annotations.NotNull

import java.awt.GridBagLayout
import javax.swing.JComponent

/**
 * @author Denis Zhdanov
 * @since 6/14/12 3:05 PM
 */
class ExportSettingsControlBuilder {

  @NotNull
  JComponent build(@NotNull ExportModel model) {
    new SwingBuilder().panel(layout: new GridBagLayout()) {
      comboBox(items: model.keymapNames)
      comboBox(items: model.profileNames)
    }
  }
}
