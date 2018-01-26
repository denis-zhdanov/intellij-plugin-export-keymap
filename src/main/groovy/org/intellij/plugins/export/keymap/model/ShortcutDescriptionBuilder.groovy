package org.intellij.plugins.export.keymap.model

import com.intellij.openapi.actionSystem.KeyboardShortcut
import com.intellij.openapi.keymap.KeymapUtil
import org.jetbrains.annotations.NotNull
import com.intellij.openapi.util.text.StringUtil

/**
 * @author Denis Zhdanov
 * @since 6/21/12 12:20 PM
 */
class ShortcutDescriptionBuilder {
  
  def Strategy strategy = new DefaultStrategy()
  
  @NotNull
  String buildDescription(@NotNull KeyboardShortcut shortcut) {
//    def conversions = [
//      '['             : '',
//      ']'             : '',
//      'pressed'       : '',
//      'slash'         : '/',
//      'back_quote'    : '`',
//      'page_down'     : 'Page down',
//      'page_up'       : 'Page up',
//      'close_bracket' : ']',
//      'open_bracket'  : '[',
//      'back_space'    : 'Back',
//      'add'           : 'NumPad+',
//      'subtract'      : 'NumPad-',
//      'comma'         : ',',
//      'semicolon'     : ';'
//    ].withDefault { it }
    
//    def text = shortcut.toString().toLowerCase()
//    conversions.each { key, value -> text = text.replace(key, value) }
//
//    def parts = text.split()
//
//    // Sort shortcut description parts (e.g. use not 'Alt + Ctrl + S' but 'Ctrl + Alt + S').
//    def orderVector = ['ctrl', 'alt', 'shift']
//    parts = parts.sort { a, b ->
//      def indexA = orderVector.indexOf(a)
//      def indexB = orderVector.indexOf(b)
//      if (indexA < 0 && indexB >= 0) {
//        return 1
//      }
//      else if (indexB < 0 && indexA >= 0) {
//        return -1
//      }
//      indexA - indexB
//    }
//
//    def result = new StringBuilder()
//    parts.each {
//      if (it) {
//        result.append(strategy.convert(it)).append(strategy.separator)
//      }
//    }
//    if (result.length()) {
//      result.length = result.length() - strategy.separator.length()
//    }
//    result.toString()
    def result = KeymapUtil.getShortcutText(shortcut)

    def myConversions = [
            'Equals'        : '=',
            '+Back Slash'   : '+\\',
            '+Slash'        : '+/',
            'Back Quote'    : '`',
            'Close Bracket' : ']',
            'Open Bracket'  : '[',
            'Backspace'    : 'Back',
            'Comma'         : ',',
            'Semicolon'     : ';'
    ].withDefault { it }
    myConversions.each { key, value -> result = result.replace(key, value) }
    return result


  }
  
  interface Strategy {
    @NotNull String getSeparator()
    @NotNull String convert(@NotNull String s)
  }
  
  class DefaultStrategy implements Strategy {
    @Override String getSeparator() { "+" }
    @Override String convert(String s) { StringUtil.capitalize(s) }
  }
}
