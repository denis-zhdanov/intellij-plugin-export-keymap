package org.denis.intellij.export.keymap.ui

/**
 * Defines contract for a strategy that decides whether particular action should be included to the resulting PDF
 * 
 * @author Denis Zhdanov
 * @since 6/14/12 3:02 PM
 */
public interface ActionPredicate {
  boolean includeAction()
}