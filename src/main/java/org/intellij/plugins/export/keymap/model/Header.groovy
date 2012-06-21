package org.intellij.plugins.export.keymap.model

import org.jetbrains.annotations.NotNull

/**
 * @author Denis Zhdanov
 * @since 6/18/12
 */
class Header implements DataEntry {

  String name
  int depth
  
  @Override void invite(@NotNull DataVisitor visitor) { visitor.visitHeader(this) }
  @Override String toString() { name }
}
