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

/**
 * @author Denis Zhdanov
 * @since 6/14/12 3:05 PM
 */
class ExportSettingsControlBuilder {

  @NotNull
  JComponent build(@NotNull List<KeymapInfo> keymaps, @NotNull List<ActionsProfile> profiles, @NotNull Collection<ActionsGroup> allActions)
  {
    def root = new CheckedTreeNode("root")
    def tree = new CheckboxTree(new TreeNodeRenderer(), root)
    def model = new DefaultTreeModel(root)
    tree.model = model
    for (group in allActions) {
      def groupNode = new CheckedTreeNode(group)
      for (action in group.actions) {
        groupNode.add(new CheckedTreeNode(action))
      }
      tree.model.root.add(groupNode)
    }
    
    model.nodeStructureChanged(root)
    
    new SwingBuilder().panel() {
      gridBagLayout()
      emptyBorder([15, 15, 10, 15], parent: true)
      
      def labelConstraints = gbc(anchor: WEST, insets: [ 0, 0, 5, 5])
      label(text: Bundle.message("label.keymap") + ":", constraints: labelConstraints)
      
      def controlConstraints = gbc(gridwidth: REMAINDER, fill: HORIZONTAL, insets: [0, 0, 5, 0])
      def keymap = comboBox(items: keymaps.collect { it.name }, constraints: controlConstraints)
      
      widget(new JBScrollPane(tree), constraints: gbc(gridwidth: REMAINDER, fill: BOTH, insets: [0, 0, 5, 0]))
      
      def buttonConstraints = gbc(gridwidth: REMAINDER, anchor: CENTER)
      button(text: Bundle.message("button.generate"), constraints: buttonConstraints, actionPerformed: {
        new GeneratorFacade().generate(keymap.selectedItem.toString(), [].toSet(), '/home/denis/Downloads/output.pdf')
      })
    }
  }
}
