package org.denis.intellij.export.keymap.ui;

/**
 * @author Denis Zhdanov
 * @since 6/15/12 1:01 PM
 */
class ActionsGroup {
  
  List<ActionData> actions = []
  String name

  @Override String toString() { name }
}
