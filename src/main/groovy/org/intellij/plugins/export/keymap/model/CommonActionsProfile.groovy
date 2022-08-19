package org.intellij.plugins.export.keymap.model

import org.intellij.plugins.export.keymap.Bundle

/**
 * @author Denis Zhdanov
 * @since 6/21/12 1:13 PM
 */
class CommonActionsProfile extends ActionsProfile {

    CommonActionsProfile() {
        entries = [
//                CREATE AND EDIT
new Header(depth: 0, name: 'CREATE AND EDIT'),
new ActionData(id: ['ShowIntentionActions']),
new ActionData(id: ['CodeCompletion'], description: 'Basic Code Completion'),
new ActionData(id: ['SmartTypeCompletion'], description: 'Smart Code Completion'),
new ActionData(id: ['ClassNameCompletion'], description: 'Type Name Completion'),
new ActionData(id: ['EditorCompleteStatement'], description: 'Complete Statement'),
new ActionData(id: ['ParameterInfo'], description: 'Parameter Information'),
new ActionData(id: ['QuickImplementations']),
new ActionData(id: ['QuickJavaDoc', 'ExternalJavaDoc'], description: 'Quick/External Documentation'),
new ActionData(id: ['NewElement'], description: 'Generate Code'),
new ActionData(id: ['OverrideMethods', 'ImplementMethods'], description: 'Override/Implement Members'),
new ActionData(id: ['SurroundWith']),
new ActionData(id: ['CommentByLineComment', 'CommentByBlockComment'], description: 'Comment with Line/Block Comment'),
new ActionData(id: ['EditorSelectWord', 'EditorUnSelectWord'], description: 'Extend/Shrink Selection'),
new ActionData(id: ['EditorContextInfo']),
new ActionData(id: ['OptimizeImports']),
new ActionData(id: ['AutoIndentLines']),
new ActionData(id: ['$Cut', '$Copy', '$Paste'], description: 'Cut/Copy/Paste'),
new ActionData(id: ['CopyPaths'], description: 'Copy Document Path'),
new ActionData(id: ['PasteMultiple'], description: 'Paste from Clipboard History'),
new ActionData(id: ['EditorDuplicate'], description: 'Duplicate Current Line or Selection'),
new ActionData(id: ['MoveLineUp', 'MoveLineDown'], description: 'Move Line Up/Down'),
new ActionData(id: ['MoveStatementUp', 'MoveStatementDown'], description: 'Move Statement Up/Down'),
new ActionData(id: ['MoveElementRight', 'MoveElementLeft'], description: 'Move Element Right/Left'),
new ActionData(id: ['EditorDeleteLine'], description: 'Delete Line at Caret'),
new ActionData(id: ['EditorJoinLines', 'EditorSplitLine'], description: 'Join/Split Line'),
new ActionData(id: ['EditorStartNewLineBefore','EditorStartNewLine'], description: 'New Line Above/Below'),
new ActionData(id: ['EditorToggleCase'], description: 'Toggle Case'),
new ActionData(id: ['ExpandRegion', 'CollapseRegion'], description: 'Expand/Collapse Code Block'),
new ActionData(id: ['RiderExpandCollapse']),
new ActionData(id: ['SaveAll']),

//                ANALYZE AND EXPLORE

new Header(depth: 0, name: 'ANALYZE AND EXPLORE'),
new ActionData(id: ['InspectCode']),
new ActionData(id: ['ShowErrorDescription'], description: 'Show Error Description'),
new ActionData(id: ['GotoNextError', 'GotoPreviousError'], description: 'Next/Previous Highlighted Error'),
new ActionData(id: ['ReSharperGotoNextErrorInSolution', 'ReSharperGotoPrevErrorInSolution'],
        description: 'Next/Previous Error in Solution'),
new ActionData(id: ['EnableDaemon'], description: 'Toggle Code Inspection in File'),
new ActionData(id: ['RunInspection']),
new ActionData(id: ['InspectThis']),
new ActionData(id: ['TypeHierarchy']),
new ActionData(id: ['MethodHierarchy']),
new ActionData(id: ['CallHierarchy']),


//                VCS/LOCAL HISTORY

new Header(depth: 0, name: 'VERSION CONTROL'),
new ActionData(id: ['Vcs.QuickListPopupAction']),
new ActionData(id: ['CheckinProject']),
new ActionData(id: ['Vcs.UpdateProject']),
new ActionData(id: ['RecentChanges']),
new ActionData(id: ['ChangesView.Revert']),
new ActionData(id: ['Vcs.Push']),
new ActionData(id: ['VcsShowNextChangeMarker','VcsShowPrevChangeMarker'], description: 'Next/Previous Change'),


new ColumnBreak(),

//                MASTER THE IDE

new Header(depth: 0, name: 'MASTER YOUR IDE'),
new ActionData(id: ['GotoAction']),
new ActionData(id: ['RiderOpenSolution'], description: 'Open Solution or Project'),
new ActionData(id: ['OpenFile'], description: 'Open File or Folder'),
new ActionData(id: ['ActivateMessagesToolWindow', 'ActivateProjectToolWindow', 'ActivateFavoritesToolWindow', 'ActivateFindToolWindow',
                    'ActivateRunToolWindow', 'ActivateDebugToolWindow', 'ActivateTODOToolWindow', 'ActivateStructureToolWindow',
                    'ActivateHierarchyToolWindow', 'ActivateChangesToolWindow'],
        shortcut: 'Alt+[0-9]',
        description: 'Open a Tool Window'),
new ActionData(id: ['Synchronize']),
new ActionData(id: ['ToggleFullScreen']),
//                   new ActionData(id: ['CodeInspection.OnEditor']),
new ActionData(id: ['QuickChangeScheme']),
new ActionData(id: ['ShowSettings']),
new ActionData(id: ['ShowProjectStructureSettings']),
new ActionData(id: ['EditSource']),
new ActionData(id: ['ShowNavBar']),
new ActionData(id: ['JumpToLastWindow']),
new ActionData(id: ['HideActiveWindow','HideAllWindows'], description: 'Hide Active/All Tool Windows'),
//new ActionData(id: ['NextSplitter']),
new ActionData(id: ['NextTab', 'PreviousTab'], description: 'Go to Next/Previous Editor Tab'),
new ActionData(id: ['EditorEscape'], description: 'Go to Editor (from a Tool Window)'),
new ActionData(id: ['CloseActiveTab', 'CloseContent'], description: 'Close Active Tab/Window'),

//                FIND EVERYTHING

new Header(depth: 0, name: 'FIND EVERYTHING'),
new ActionData(id: ['SearchEverywhere'], shortcut: 'Double Shift'),
new ActionData(id: ['Find', 'Replace'], description: 'Find/Replace in File'),
new ActionData(id: ['FindInPath', 'ReplaceInPath'], description: 'Find/Replace in Project'),
new ActionData(id: ['GotoClass'], description: 'Go to Class'),
new ActionData(id: ['FileStructurePopup'], description: 'Go to File Member'),
new ActionData(id: ['GotoFile'], description: 'Go to File'),
new ActionData(id: ['GotoSymbol'], description: 'Go to Symbol'),
new ActionData(id: ['TextSearchAction'], description: 'Go to Text'),
new ActionData(id: ['StructuralSearchPlugin.StructuralSearchAction'], description: 'Search Structurally'),
new ActionData(id: ['StructuralSearchPlugin.StructuralReplaceAction'], description: 'Replace Structurally'),

//                NAVIGATE FROM SYMBOLS

new Header(depth: 0, name: 'NAVIGATE FROM SYMBOLS'),
new ActionData(id: ['ReSharperNavigateTo']),
new ActionData(id: ['GotoDeclaration']),
new ActionData(id: ['GotoTypeDeclaration']),
new ActionData(id: ['GotoSuperMethod', 'GotoImplementation'], description: 'Go to Base/Derived Symbols'),
new ActionData(id: ['ReSharperGotoImplementation'], description: 'Go to Implementations'),
new ActionData(id: ['FindUsages', 'FindUsagesInFile'], description: 'Find Usages in Project/File'),
new ActionData(id: ['HighlightUsagesInFile']),
new ActionData(id: ['ShowUsages']),

//                NAVIGATE IN CONTEXT

new Header(depth: 0, name: 'NAVIGATE CONTEXT'),
new ActionData(id: ['SelectIn']),
new ActionData(id: ['Switcher']),
new ActionData(id: ['LocateInSolutionView']),
new ActionData(id: ['RecentFiles','RecentChangedFiles'], description: 'Recently Viewed/Changed Files'),
new ActionData(id: ['JumpToLastChange']),
new ActionData(id: ['Back', 'Forward'], description: 'Navigate Back/Forward'),
new ActionData(id: ['MethodDown', 'MethodUp'], description: 'Next/Previous method'),
new ActionData(id: ['GotoLine']),
new ActionData(id: ['EditorCodeBlockEnd', 'EditorCodeBlockStart'], description: 'Go to Code Block End/Start'),
new ActionData(id: ['FindNext','FindPrevious'], description: 'Next/Previous Occurrence'),
new ActionData(id: ['FindWordAtCaret','FindPrevWordAtCaret'], description: 'Next/Previous Word at Caret'),
new ActionData(id: ['ToggleBookmark']),
new ActionData(id: ['ToggleBookmarkWithMnemonic'], description: 'Toggle Bookmark with Mnemonic'),
new ActionData(id: ['GotoBookmark0', 'GotoBookmark1', 'GotoBookmark2', 'GotoBookmark3', 'GotoBookmark4', 'GotoBookmark5',
                    'GotoBookmark6', 'GotoBookmark7', 'GotoBookmark8', 'GotoBookmark9'],
        shortcut: 'Ctrl+[0-9]',
        description: 'Go to Numbered Bookmark'),
new ActionData(id: ['GotoNextBookmark','GotoPreviousBookmark'], description: 'Next/Previous Bookmark'),
new ActionData(id: ['ShowBookmarks']),

new ColumnBreak(),

//                BUILD AND RUN

new Header(depth: 0, name: 'BUILD, RUN, AND DEBUG'),
new ActionData(id: ['RiderNuGetQuickListPopupAction']),
new ActionData(id: ['BuildSolutionAction']),
new ActionData(id: ['CompileDirty']),
new ActionData(id: ['Compile']),
new ActionData(id: ['BuildCurrentProject']),
new ActionData(id: ['CancelBuildAction'], description: 'Cancel Build'),
new ActionData(id: ['RunAnything'], shortcut: 'Double Ctrl'),
new ActionData(id: ['RunClass']),
new ActionData(id: ['ChooseRunConfiguration', 'ChooseDebugConfiguration'], description: 'Run/Debug Selected Configuration'),
new ActionData(id: ['Run', 'Debug'], description:  'Run/Debug Current Configuration'),
new ActionData(id: ['XDebugger.AttachToLocalProcess']),
new ActionData(id: ['StepOver']),
new ActionData(id: ['StepInto']),
new ActionData(id: ['SmartStepInto']),
new ActionData(id: ['StepOut']),
new ActionData(id: ['RunToCursor']),
new ActionData(id: ['ForceRunToCursor']),
new ActionData(id: ['ShowExecutionPoint']),
new ActionData(id: ['SetNextStatement']),
new ActionData(id: ['EvaluateExpression']),
new ActionData(id: ['Stop']),
new ActionData(id: ['Resume']),
//                breakpoints
new ActionData(id: ['ToggleLineBreakpoint']),
new ActionData(id: ['ToggleTemporaryLineBreakpoint']),
new ActionData(id: ['ToggleBreakpointEnabled'], description: 'Enable/Disable Breakpoint'),
new ActionData(id: ['EditBreakpoint']),
new ActionData(id: ['ViewBreakpoints']),

//                UNIT TESTS (Rider)

new Header(depth: 0, name: 'UNIT TESTS'),
new ActionData(id: ['RiderUnitTestQuickListPopupAction']),
new ActionData(id: ['RiderUnitTestRunContextAction', 'RiderUnitTestDebugContextAction'], description: 'Run/Debug Unit Tests'),
new ActionData(id: ['RiderUnitTestSessionAbortAction']),
new ActionData(id: ['RiderUnitTestRepeatPreviousRunAction']),
new ActionData(id: ['RiderUnitTestSessionRerunFailedTestsAction']),
new ActionData(id: ['RiderUnitTestRunSolutionAction']),
new ActionData(id: ['RiderUnitTestNewSessionAction']),
new ActionData(id: ['RiderUnitTestAppendTestsAction']),
new ActionData(id: ['RiderUnitTestRunCurrentSessionAction']),
new ActionData(id: ['RerunTests']),
new ActionData(id: ['ShowRecentTests']),
new ActionData(id: ['GotoTest']),
new ActionData(id: ['SwitchCoverage']),
new ActionData(id: ['Coverage']),

//                REFACTORINGS

new Header(depth: 0, name: 'REFACTOR AND CLEAN UP'),
new ActionData(id: ['Refactorings.QuickListPopupAction']),
new ActionData(id: ['CopyElement']),
new ActionData(id: ['Move']),
new ActionData(id: ['SafeDelete']),
new ActionData(id: ['RenameElement']),
new ActionData(id: ['ChangeSignature']),
new ActionData(id: ['Inline']),
new ActionData(id: ['ExtractMethod'], description: 'Extract Method'),
new ActionData(id: ['IntroduceVariable'], description: 'Introduce Variable'),
new ActionData(id: ['IntroduceField'], description: 'Introduce Field'),
new ActionData(id: ['IntroduceConstant'], description: 'Introduce Constant'),
new ActionData(id: ['IntroduceParameter'], description: 'Introduce Parameter'),
new ActionData(id: ['ReformatCode']),
new ActionData(id: ['CodeCleanup', 'SilentCodeCleanup'], description: 'Code Cleanup/Silent Cleanup')
        ]
        name = Bundle.message("profile.name.default")
    }
}
