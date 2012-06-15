package org.denis.intellij.export.keymap.ui

import com.intellij.ui.CheckboxTree
import javax.swing.JTree

/**
 * @author Denis Zhdanov
 * @since 6/15/12 6:34 PM
 */
class TreeNodeRenderer extends CheckboxTree.CheckboxTreeCellRenderer {
  @Override
  void customizeRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    if (value.userObject in ActionsGroup) {
      textRenderer.append(value.userObject.name)
    }
    else if (value.userObject in ActionData) {
      textRenderer.append(value.userObject.description)
    }
  }
}
