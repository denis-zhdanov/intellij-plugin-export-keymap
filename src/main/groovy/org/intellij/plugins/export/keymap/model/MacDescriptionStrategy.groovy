package org.intellij.plugins.export.keymap.model

import com.intellij.openapi.util.text.StringUtil

/**
 * @author Denis Zhdanov
 * @since 6/21/12 12:37 PM
 */
class MacDescriptionStrategy implements ShortcutDescriptionBuilder.Strategy {
  
  static MacDescriptionStrategy instance = new MacDescriptionStrategy() 
  
  private static conversions = [
    'ctrl'         : '^',
    'shift'        : '⇧',
    'alt'          : '⌥',
    'meta'         : '⌘',
    'Backspace'   : '⌫',
    'delete'       : '⌦',
    'escape'       : '⎋',
    'enter'        : '↩',
    'tab'          : '⇥',
    'up'           : '↑',
    'down'         : '↓',
    'left'         : '←',
    'right'        : '→',
    'NumPad+'      : '+',
    'NumPad-'      : '-',
    'BackQuote(`)' : '`'
  ].withDefault { it }
  
  @Override String getSeparator() { ''}
  @Override String convert(String s) { StringUtil.capitalize(conversions[s]) }
}
