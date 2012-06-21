package org.intellij.plugins.export.keymap.model

import org.jetbrains.annotations.NotNull

/**
 * @author Denis Zhdanov
 * @since 6/18/12
 */
class ColumnBreak implements DataEntry {

  @Override void invite(@NotNull DataVisitor visitor) { visitor.visitColumnBreak(this) }
}
