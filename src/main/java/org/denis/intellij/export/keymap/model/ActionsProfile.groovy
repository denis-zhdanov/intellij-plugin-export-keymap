package org.denis.intellij.export.keymap.model

import org.denis.intellij.export.keymap.Bundle

/**
 * @author Denis Zhdanov
 * @since 6/15/12 1:00 PM
 */
class ActionsProfile {
  
  List<DataEntry> entries = []
  String name

  @Override String toString() { name }
  
  static def DEFAULT = new ActionsProfile(name: Bundle.message("profile.name.default"), entries: [
    new Header(depth: 0, name: 'Editing'),
    new ActionData(id: ['CodeCompletion'], description: 'Basic code completion (the name of any class, method or variable)'), 
    new ActionData(id: ['SmartTypeCompletion'], description: '''Smart code completion (filters the list of methods and variables 
by expected type)'''),
    new ActionData(id: ['ClassNameCompletion'], description: '''Class name completion (the name of any project class 
independently of current imports)'''),
    new ActionData(id: ['EditorCompleteStatement'], description: 'Complete statement'),
    new ActionData(id: ['ParameterInfo']),
    new ActionData(id: ['QuickJavaDoc']),
    new ActionData(id: ['ExternalJavaDoc']),
    new ActionData(shortcut: 'Ctrl + mouse over code', description: 'Brief Info'),
    new ActionData(id: ['ShowErrorDescription']),
    new ActionData(id: ['NewElement'], description: 'Generate code... (Getters, Setters, Constructors, hashCode/equals, toString)'),
    new ActionData(id: ['OverrideMethods']),
    new ActionData(id: ['ImplementMethods']),
    new ActionData(id: ['SurroundWith']),
    new ActionData(id: ['CommentByLineComment']),
    new ActionData(id: ['CommentByBlockComment']),
    new ActionData(id: ['EditorSelectWord'], description: 'Select successively increasing code blocks'),
    new ActionData(id: ['EditorUnSelectWord'], description: 'Decrease current selection to previous state'),
    new ActionData(id: ['EditorContextInfo'], description: 'Show intention actions and quick-fixes'),
    new ActionData(id: ['ShowIntentionActions']),
    new ActionData(id: ['ReformatCode']),
    new ActionData(id: ['OptimizeImports']),
    new ActionData(id: ['AutoIndentLines']),
    new ActionData(id: ['EditorIndentSelection', 'EditorUnindentSelection'], description: 'Indent/Unindent selected lines'),
    new ActionData(id: ['$Cut'], description: 'Cut current line or selected block to clipboard'),
    new ActionData(id: ['$Copy'], description: 'Copy current line or selected block to clipboard'),
    new ActionData(id: ['$Paste']),
    new ActionData(id: ['PasteMultiple'], description: 'Paste from recent buffers...'),
    new ActionData(id: ['EditorDuplicate'], description: 'Duplicate current line or selected block'),
    new ActionData(id: ['EditorDeleteLine'], description: 'Delete line at caret'),
    new ActionData(id: ['EditorJoinLines'], description: 'Smart line join'),
    new ActionData(id: ['EditorSplitLine'], description: 'Smart line split'),
    new ActionData(id: ['EditorStartNewLine'], description: 'Start new line'),
    new ActionData(id: ['EditorToggleCase'], description: 'Toggle case for word at caret or selected block'),
    new ActionData(id: ['EditorCodeBlockEndWithSelection', 'EditorCodeBlockStartWithSelection'],
                   description: 'Select till code block end/start'),
    new ActionData(id: ['EditorDeleteToWordEnd'], description: 'Delete to word end'),
    new ActionData(id: ['EditorDeleteToWordStart'], description: 'Delete to word start'),
    new ActionData(id: ['ExpandRegion', 'CollapseRegion'], description: 'Expand/collapse code block'),
    new ActionData(id: ['ExpandAll'], description: 'Expand all'),
    new ActionData(id: ['CollapseAll'], description: 'Collapse all'),
    new ActionData(id: ['CloseContent']),

    new Header(depth: 0, name: 'Search/Replace'),
    new ActionData(id: ['Find']),
    new ActionData(id: ['FindNext']),
    new ActionData(id: ['FindPrevious']),
    new ActionData(id: ['Replace']),
    new ActionData(id: ['FindInPath']),
    new ActionData(id: ['ReplaceInPath']),
    new ActionData(id: ['StructuralSearchPlugin.StructuralSearchAction'], description: 'Search structurally (Ultimate Edition only)'),
    new ActionData(id: ['StructuralSearchPlugin.StructuralReplaceAction'], description: 'Replace structurally (Ultimate Edition only)'),

    new ColumnBreak(),

    new Header(depth: 0, name: 'Usage Search'),
    new ActionData(id: ['FindUsages', 'FindUsagesInFile'], description: 'Find usages / Find Usages in file'),
    new ActionData(id: ['HighlightUsagesInFile']),
    new ActionData(id: ['ShowUsages']),

    new Header(depth: 0, name: 'Compile and Run'),
    new ActionData(id: ['CompileDirty']),
    new ActionData(id: ['Compile']),
    new ActionData(id: ['ChooseRunConfiguration']),
    new ActionData(id: ['ChooseDebugConfiguration']),
    new ActionData(id: ['Run']),
    new ActionData(id: ['Debug']),
    new ActionData(id: ['RunClass'])
  ])
}
