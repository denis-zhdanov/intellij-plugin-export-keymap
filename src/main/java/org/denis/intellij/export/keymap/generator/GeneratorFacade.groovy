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
  
  static final String GO_TO_ACTION_TASK_ID = 'GotoAction'
  
  def generate(@NotNull Keymap keymap, @NotNull ActionsProfile profile, @NotNull String outputPath) {
    def actionManager = ActionManager.instance
    def visitor = new DataVisitor() {
      @Override void visitActionData(@NotNull ActionData data) {
        // Ensure action shortcut is defined.
        if (!data.shortcut) {
          def buffer = new StringBuilder()
          data.id.each {
            def shortcuts = keymap.getShortcuts(it)
            if (shortcuts) {
              buffer.append(" / ${shortcutDescription(shortcuts[0] as KeyboardShortcut, keymap)}")
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
      goToActionShortcut = shortcutDescription(shortcuts[0] as KeyboardShortcut, keymap)
    }
    
    // Generate PDF.
    new Generator().generate(profile.entries, goToActionShortcut, outputPath, keymap.presentableName)
  }
  
  private static String shortcutDescription(@NotNull KeyboardShortcut shortcut, @NotNull Keymap keymap) {
    def result = new StringBuilder()
    def conversions = getShortcutConversions(keymap)
    def text = shortcut.toString().toLowerCase()
    conversions.each { key, value -> text = text.replace(key, value) }
    def parts = text.split()
    
    // Sort shortcut description parts (e.g. use not 'Alt + Ctrl + S' but 'Ctrl + Alt + S').
    parts = parts.sort { a, b ->
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
      if (it) {
        result.append(StringUtil.capitalize(it)).append(" + ")
      }
    }
    if (result.length()) {
      result.length = result.length() - 3
    }
    result
  }
  
  @NotNull
  private static Map<String, String> getShortcutConversions(@NotNull Keymap keymap) {
    def conversions = [
      '['             : '',
      ']'             : '',
      'pressed'       : '',
      'slash'         : '/',
      'back_quote'    : 'BackQuote(`)',
      'close_bracket' : ']',
      'open_bracket'  : '[',
      'back_space'    : 'Backspace',
      'add'           : 'NumPad+',
      'subtract'      : 'NumPad-'
    ].withDefault { it }
    
    if (isMac(keymap)) {
      conversions['ctrl']         = '^'
      conversions['shift']        = '\u21e7'
      conversions['alt']          = '⌥'
      conversions['meta']         = '⌘'
      conversions['back_space']   = '⌫'
      conversions['delete']       = '⌦'
      conversions['escape']       = '⎋'
      conversions['enter']        = '↩'
      conversions['tab']          = '⇥'
      conversions['up']           = '↑'
      conversions['down']         = '↓'
      conversions['left']         = '←'
      conversions['right']        = '→'
    }
    
    conversions
  }
  
  private static boolean isMac(@NotNull Keymap keymap) {
    while (keymap) {
      if (keymap.name.contains('Mac')) {
        return true
      }
      keymap = keymap.parent
    }
    false
  }
}
