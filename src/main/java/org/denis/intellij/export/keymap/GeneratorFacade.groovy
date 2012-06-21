package org.denis.intellij.export.keymap

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.KeyboardShortcut
import com.intellij.openapi.keymap.Keymap
import org.denis.intellij.export.keymap.generator.Generator
import org.jetbrains.annotations.NotNull
import org.denis.intellij.export.keymap.model.*

/**
 * @author Denis Zhdanov
 * @since 6/14/12
 */
class GeneratorFacade {
  
  static final String GO_TO_ACTION_TASK_ID = 'GotoAction'
  
  def generate(@NotNull Keymap keymap, @NotNull ActionsProfile profile, @NotNull String outputPath, boolean useMacButtons) {
    def actionManager = ActionManager.instance
    def visitor = new DataVisitor() {
      @Override void visitActionData(@NotNull ActionData data) {
        // Ensure action shortcut is defined.
        if (!data.shortcut) {
          def buffer = new StringBuilder()
          data.id.each {
            def shortcut = keymap.getShortcuts(it).find { it in KeyboardShortcut}
            if (shortcut) {
              buffer.append(" / ${shortcutDescription(shortcut, useMacButtons)}")
            }
          }
          if (buffer.length() > 0) {
            data.shortcut = buffer.toString().substring(3)
          }
        }
        
        // Ensure action description is defined
        if (!data.description) {
          def action = actionManager.getAction(data.id.first())
          if (action) {
            def p = action.templatePresentation
            data.description = p?.description ?: p?.text
          }
        }
      }
      
      @Override void visitColumnBreak(@NotNull ColumnBreak columnBreak) { } 
      @Override void visitHeader(@NotNull Header data) { }
    }
    
    // Fill shortcuts
    profile.entries.each { it.invite(visitor) }
    
    // Remove actions with undefined shortcuts or description.
    profile.entries.removeAll {it in ActionData && (!it.shortcut || !it.description) }
    
    String goToActionShortcut = null
    def shortcuts = keymap.getShortcuts(GO_TO_ACTION_TASK_ID)
    if (shortcuts) {
      goToActionShortcut = shortcutDescription(shortcuts[0] as KeyboardShortcut, useMacButtons)
    }
    
    // Generate PDF.
    new Generator().generate(profile.entries, goToActionShortcut, outputPath, keymap.presentableName)
  }
  
  private static String shortcutDescription(@NotNull KeyboardShortcut shortcut, @NotNull boolean useMacButtons) {
    def builder = new ShortcutDescriptionBuilder()
    if (useMacButtons) {
      builder.strategy = MacDescriptionStrategy.instance
    }
    builder.buildDescription(shortcut)
  }
}
