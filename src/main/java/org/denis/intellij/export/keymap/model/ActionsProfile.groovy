package org.denis.intellij.export.keymap.model

import org.denis.intellij.export.keymap.Bundle

/**
 * @author Denis Zhdanov
 * @since 6/15/12 1:00 PM
 */
class ActionsProfile {
  
  List<DataEntry> entries = []
  String name

  @Override String toString() { name }
}
