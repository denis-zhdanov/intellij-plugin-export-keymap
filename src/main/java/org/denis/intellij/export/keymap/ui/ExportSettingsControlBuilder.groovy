package org.denis.intellij.export.keymap.ui

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.components.JBScrollPane
import groovy.swing.SwingBuilder
import org.denis.intellij.export.keymap.Bundle
import org.denis.intellij.export.keymap.generator.GeneratorFacade
import org.jetbrains.annotations.NotNull

import javax.swing.JComponent
import javax.swing.tree.DefaultTreeModel
import org.denis.intellij.export.keymap.model.ActionsProfile
import org.denis.intellij.export.keymap.model.KeymapInfo
import com.intellij.ide.util.projectWizard.NamePathComponent
import com.intellij.openapi.keymap.Keymap

/**
 * @author Denis Zhdanov
 * @since 6/14/12 3:05 PM
 */
class ExportSettingsControlBuilder {

  @NotNull
  JComponent build(@NotNull List<Keymap> keymaps, @NotNull List<ActionsProfile> profiles)
  {
    def pathText = Bundle.message("label.path")
    def pathControl = new NamePathComponent("", pathText, pathText, "", false, false)
    
    new SwingBuilder().panel() {
      gridBagLayout()
      emptyBorder([15, 15, 10, 15], parent: true)
      
      def labelConstraints = gbc(anchor: WEST, insets: [ 0, 0, 5, 5])
      label(text: Bundle.message("label.keymap") + ":", constraints: labelConstraints)
      
      def controlConstraints = gbc(gridwidth: REMAINDER, fill: HORIZONTAL, insets: [0, 0, 5, 0])
      def keymap = comboBox(items: keymaps.collect { it.presentableName }, constraints: controlConstraints)

//      widget(pathControl, constraints: gbc(gridwidth: REMAINDER, anchor: WEST))
      
      def buttonConstraints = gbc(gridwidth: REMAINDER, anchor: CENTER)
      button(text: Bundle.message("button.generate"), constraints: buttonConstraints, actionPerformed: {
        new GeneratorFacade().generate(keymaps[keymap.selectedIndex], ActionsProfile.DEFAULT, '/home/denis/Downloads/output.pdf')
      })
    }
  }
}
