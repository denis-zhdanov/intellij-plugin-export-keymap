package org.denis.intellij.export.keymap.model;

import org.jetbrains.annotations.NotNull;

/**
 * @author Denis Zhdanov
 * @since 6/18/12
 */
public interface DataVisitor {
  
  void visitActionData(@NotNull ActionData data);
  void visitHeader(@NotNull Header data);
  void visitColumnBreak(@NotNull ColumnBreak columnBreak);
}