package org.denis.intellij.export.keymap.ui

import groovy.swing.SwingBuilder
import org.jetbrains.annotations.NotNull

import java.awt.GridBagLayout
import javax.swing.JComponent
import java.awt.GridBagConstraints
import org.denis.intellij.export.keymap.Bundle
import org.denis.intellij.export.keymap.generator.Generator
import org.denis.intellij.export.keymap.generator.SectionData
import org.denis.intellij.export.keymap.generator.GeneratorFacade

/**
 * @author Denis Zhdanov
 * @since 6/14/12 3:05 PM
 */
class ExportSettingsControlBuilder {

  @NotNull
  JComponent build(@NotNull ExportModel model) {
    new SwingBuilder().panel() {
      gridBagLayout()
      def labelConstraints = gbc(anchor: GridBagConstraints.WEST, insets: [ 0, 0, 0, 5])
      def controlConstraints = gbc(gridwidth: GridBagConstraints.REMAINDER, fill: GridBagConstraints.HORIZONTAL)
      
      label(text: Bundle.message("label.keymap") + ":", constraints: labelConstraints)
      def keymap = comboBox(items: model.keymapNames, constraints: controlConstraints)

      label(text: Bundle.message("label.action.group") + ":", constraints: labelConstraints)
      comboBox(items: model.profileNames, constraints: controlConstraints)
      
      button(text: Bundle.message("button.generate"), constraints: controlConstraints, actionPerformed: {
        new GeneratorFacade().generate(keymap.selectedItem.toString(), [].toSet(), '/home/denis/Downloads/output.pdf')
      })
    }
  }
}
