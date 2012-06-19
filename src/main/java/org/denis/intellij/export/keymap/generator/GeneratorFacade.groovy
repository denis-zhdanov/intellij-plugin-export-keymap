package org.denis.intellij.export.keymap.generator

import com.intellij.openapi.actionSystem.KeyboardShortcut
import com.intellij.openapi.keymap.Keymap
import com.intellij.openapi.util.text.StringUtil
import org.jetbrains.annotations.NotNull
import org.denis.intellij.export.keymap.model.*
import com.intellij.openapi.actionSystem.ActionManager

/**
 * @author Denis Zhdanov
 * @since 6/14/12
 */
class GeneratorFacade {

  def generate(@NotNull Keymap keymap, @NotNull ActionsProfile profile, @NotNull String outputPath) {
    def actionManager = ActionManager.instance
    def visitor = new DataVisitor() {
      @Override void visit(ActionData data) {
        // Ensure action shortcut is defined.
        if (!data.shortcut) {
          def buffer = new StringBuilder()
          data.id.each {
            def shortcuts = keymap.getShortcuts(it)
            if (shortcuts) {
              buffer.append("/ ${shortcutDescription(shortcuts[0] as KeyboardShortcut)}")
            }
          }
          if (buffer.length() > 0) {
            data.shortcut = buffer.toString().substring(2)
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
      
      @Override void visit(ColumnBreak columnBreak) { } 
      @Override void visit(Header data) { }
    }
    
    // Fill shortcuts
    profile.entries.each { visitor.visit(it) }
    
    // Remove actions with undefined shortcuts or description.
    profile.entries.removeAll {it in ActionData && (!it.shortcut || !it.description) }
    new Generator().generate(profile.entries, outputPath, keymap.presentableName)
  }
  
  private static String shortcutDescription(@NotNull KeyboardShortcut shortcut) {
    def result = new StringBuilder()
    def conversions = ['[' : '', ']' : '', 'pressed' : '', 'slash' : '/', 'back_quote' : 'BackQuote(`)'].withDefault { it }
    def parts = shortcut.toString().toLowerCase().split()
    parts.sort { a, b ->
      def helper = { first, second ->
        if (first == 'ctrl'
          || (first == 'shift' && second != 'ctrl')
          || (first == 'alt' && !(second in ['ctrl', 'shift'])))
        {
          return -1
        }
        0
      }
      if (helper(a, b)) {
        return -1
      }
      else if (helper(b, a)) {
        return 1
      }
      0
    }
    parts.each {
      def text = it
      conversions.each { key, value -> text = text.replace(key, value) }
      if (text) {
        result.append(StringUtil.capitalize(text)).append(" + ")
      }
    }
    if (result.length()) {
      result.length = result.length() - 3
    }
    result
  }
}
