package org.denis.intellij.export.keymap.ui

import org.denis.intellij.export.keymap.Bundle

/**
 * @author Denis Zhdanov
 * @since 6/15/12 1:00 PM
 */
class ActionsProfile {
  
  List<ActionsGroup> actionGroups = []
  String name

  @Override String toString() { name }
  
  static def DEFAULT = new ActionsProfile(name: Bundle.message("profile.name.default"), actionGroups: [
    new ActionsGroup(name: 'Editing', actions: [
      new ActionData(id: ['CodeCompletion']),
      new ActionData(id: ['SmartTypeCompletion']),
      new ActionData(id: ['ClassNameCompletion']),
      new ActionData(id: ['EditorCompleteStatement']),
      new ActionData(id: ['ParameterInfo']),
      new ActionData(id: ['QuickJavaDoc']),
      new ActionData(id: ['ExternalJavaDoc']),
      new ActionData(shortcut: 'Ctrl + mouse over code', description: 'Brief Info'),
      new ActionData(id: ['ShowErrorDescription']),
      new ActionData(id: ['NewElement']),
      new ActionData(id: ['OverrideMethods']),
      new ActionData(id: ['ImplementMethods']),
      new ActionData(id: ['SurroundWith']),
      new ActionData(id: ['CommentByLineComment']),
      new ActionData(id: ['CommentByBlockComment']),
      new ActionData(id: ['EditorSelectWord']),
      new ActionData(id: ['EditorUnSelectWord']),
      new ActionData(id: ['EditorContextInfo']),
      new ActionData(id: ['ShowIntentionActions']),
      new ActionData(id: ['ReformatCode']),
      new ActionData(id: ['OptimizeImports']),
      new ActionData(id: ['AutoIndentLines']),
      new ActionData(id: ['EditorIndentSelection', 'EditorUnindentSelection'], description: 'Indent/Unindent selected lines'),
      new ActionData(id: ['$Cut']),
      new ActionData(id: ['$Copy']),
      new ActionData(id: ['$Paste']),
      new ActionData(id: ['PasteMultiple']),
      new ActionData(id: ['EditorDuplicate']),
      new ActionData(id: ['EditorDeleteLine']),
      new ActionData(id: ['EditorJoinLines']),
      new ActionData(id: ['EditorStartNewLine']),
      new ActionData(id: ['EditorToggleCase']),
      new ActionData(id: ['EditorCodeBlockEndWithSelection', 'EditorCodeBlockStartWithSelection'],
                     description: 'Select till code block end/start'),
      new ActionData(id: ['EditorDeleteToWordEnd']),
      new ActionData(id: ['EditorDeleteToWordStart']),
      new ActionData(id: ['ExpandRegion', 'CollapseRegion'], description: 'Expand/collapse code block'),
      new ActionData(id: ['ExpandAll']),
      new ActionData(id: ['CollapseAll']),
      new ActionData(id: ['CloseContent'])
    ]),
    
    new ActionsGroup(name: 'Search/Replace', actions: [
      new ActionData(id: ['Find']),
      new ActionData(id: ['FindNext']),
      new ActionData(id: ['FindPrevious']),
      new ActionData(id: ['Replace']),
      new ActionData(id: ['FindInPath']),
      new ActionData(id: ['ReplaceInPath']),
      new ActionData(id: ['StructuralSearchPlugin.StructuralSearchAction']),
      new ActionData(id: ['StructuralSearchPlugin.StructuralReplaceAction'])
    ]),

    new ActionsGroup(name: 'Usage Search', actions: [
      new ActionData(id: ['FindUsages', 'FindUsagesInFile'], description: 'Find usages / Find Usages in file'),
      new ActionData(id: ['HighlightUsagesInFile']),
      new ActionData(id: ['ShowUsages'])
    ])
  ])
}
