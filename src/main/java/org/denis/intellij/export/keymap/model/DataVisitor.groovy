package org.denis.intellij.export.keymap.model

import org.jetbrains.annotations.NotNull

/**
 * @author Denis Zhdanov
 * @since 6/18/12
 */
interface DataVisitor {
  
  void visit(@NotNull ActionData data)
  void visit(@NotNull Header data)
  void visit(@NotNull ColumnBreak columnBreak)
}