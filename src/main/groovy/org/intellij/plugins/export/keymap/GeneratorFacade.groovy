package org.intellij.plugins.export.keymap

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.KeyboardShortcut
import com.intellij.openapi.keymap.Keymap
import com.intellij.openapi.keymap.KeymapUtil
import org.intellij.plugins.export.keymap.generator.Generator
import org.intellij.plugins.export.keymap.model.*
import org.jetbrains.annotations.NotNull

/**
 * @author Denis Zhdanov
 * @since 6/14/12
 */
class GeneratorFacade {

    static def generate(
            @NotNull Keymap keymap,
            @NotNull ActionsProfile profile, @NotNull String outputPath) {
        def actionManager = ActionManager.instance
        def visitor = new DataVisitor() {
            @Override
            void visitActionData(@NotNull ActionData data) {
                // Ensure action shortcut is defined.
                if (!data.shortcut) {
                    def buffer = new StringBuilder()
                    data.id.each {
                        def shortcut = keymap.getShortcuts(it).find { it in KeyboardShortcut }
                        if (shortcut) {
                            def shortcutText = KeymapUtil.getShortcutText(shortcut)
                            buffer.append(" / ${shortcutText}")
                        }
                    }
                    if (buffer.length() > 0) {
                        data.shortcut = buffer.toString().substring(3)
                    }
                }
                if (data.shortcut)
                    data.shortcut = prettifyShortcutString(data.shortcut, keymap)

                // Ensure action description is defined
                if (!data.description) {
                    def action = actionManager.getAction(data.id.first())
                    if (action) {
                        def p = action.templatePresentation
//            data.description = p?.description ?: p?.text
                        data.description = p?.text
                    }
                }
            }

            @Override
            void visitColumnBreak(@NotNull ColumnBreak columnBreak) {}

            @Override
            void visitHeader(@NotNull Header data) {}
        }

        // Fill shortcuts
        profile.entries.each { it.invite(visitor) }

        // Remove actions with undefined shortcuts or description.
        profile.entries.removeAll { it in ActionData && (!it.shortcut || !it.description) }

        // Remove category header if there is no action in the category
        DataEntry entry
        List<DataEntry> entriesToRemove = new ArrayList<DataEntry>()
        profile.entries.each {
            if (it in Header && entry in Header) entriesToRemove.add(entry)
            entry = it
        }
        profile.entries.removeAll(entriesToRemove)

        // Generate PDF.
        new Generator().generate(profile.entries, outputPath, keymap.presentableName)
    }

    @NotNull
    static String prettifyShortcutString(@NotNull String shortcutString, @NotNull Keymap keymap) {

        def myConversions = [
                'Equals'       : '=',
                '+Back Slash'  : '+\\',
                '+Slash'       : '+/',
                'Back Quote'   : '`',
                'Close Bracket': ']',
                'Open Bracket' : '[',
                'Page Down'    : 'PgDown',
                'Page Up'      : 'PgUp',
                'Backspace'    : 'Back',
                'Escape'       : 'Esc',
                'Minus'        : '-',
                'Comma'        : ',',
                'Semicolon'    : ';'
        ].withDefault { it }

        def macSkips = [
                'NumPad +': 'NUMPAD_PLUS',
        ].withDefault { it }

        def macConversions = [
                '+'          : ' ',
                'Ctrl'       : '^',
                'Shift'      : '⇧',
                'Alt'        : '⌥',
                'Meta'       : '⌘',
                'NUMPAD_PLUS': 'NumPad +'
        ].withDefault { it }


        myConversions.each { key, value -> shortcutString = shortcutString.replace(key, value) }

        if (keymap.name.contains("OS X") || keymap.name.contains("OSX") || keymap.name.contains("Xcode")) {
            macSkips.each { key, value -> shortcutString = shortcutString.replace(key, value) }
            macConversions.each { key, value -> shortcutString = shortcutString.replace(key, value) }
        }

        return shortcutString
    }
}
