package org.denis.intellij.export.keymap.model

import org.jetbrains.annotations.NotNull

/**
 * @author Denis Zhdanov
 * @since 6/18/12
 */
class Header implements DataEntry {

  String name
  int depth
  
  @Override void visit(@NotNull DataVisitor visitor) { visitor.visit(this) }
  @Override String toString() { name }
}
