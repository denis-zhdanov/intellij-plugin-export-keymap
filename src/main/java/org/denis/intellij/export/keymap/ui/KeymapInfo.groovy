package org.denis.intellij.export.keymap.ui

/**
 * @author Denis Zhdanov
 * @since 6/15/12 5:10 PM
 */
class KeymapInfo {
  
  String name
  /** Actions which have shortcuts at the target keymap. */
  List<ActionsGroup> actions = []

  @Override String toString() { name }
}
