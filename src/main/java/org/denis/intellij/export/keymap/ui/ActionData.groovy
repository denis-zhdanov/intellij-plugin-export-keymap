package org.denis.intellij.export.keymap.ui

/**
 * @author Denis Zhdanov
 * @since 6/15/12 1:00 PM
 */
class ActionData {
  
  List<String> id = []
  String shortcut
  String description

  @Override String toString() { description }
}
