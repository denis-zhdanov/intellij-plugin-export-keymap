package org.intellij.plugins.export.keymap.model

/**
 * @author Denis Zhdanov
 * @since 6/15/12 1:00 PM
 */
class ActionsProfile {
  
  List<DataEntry> entries = []
  String name

  @Override String toString() { name }
}
