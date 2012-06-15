package org.denis.intellij.export.keymap;


import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.actionSystem.ex.QuickList
import com.intellij.openapi.keymap.KeymapManager
import com.intellij.openapi.keymap.ex.KeymapManagerEx
import com.intellij.openapi.keymap.impl.ui.ActionsTreeUtil
import com.intellij.openapi.keymap.impl.ui.Group
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.BalloonBuilder
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.awt.RelativePoint
import org.jetbrains.annotations.NotNull

import java.awt.MouseInfo

import org.denis.intellij.export.keymap.ui.*

/**
 * @author Denis Zhdanov
 * @since 6/5/12 3:47 PM
 */
class ExportKeymapAction extends AnAction {

  public ExportKeymapAction() {
    getTemplatePresentation().setText(Bundle.message("action.export.keymap.name"));
    getTemplatePresentation().setDescription(Bundle.message("action.export.keymap.name"));
  }

  @Override
  void update(AnActionEvent e) {
    def project = PlatformDataKeys.PROJECT.getData(e.dataContext)
    e.presentation.enabled = project != null
  }

  @Override
  public void actionPerformed(AnActionEvent event) {
    def project = PlatformDataKeys.PROJECT.getData(event.dataContext)
    if (!project) {
      return
    }

    def keymapManager = KeymapManagerEx.instanceEx
    def rootGroup = ActionsTreeUtil.createMainGroup(project, keymapManager.getKeymap(KeymapManager.DEFAULT_IDEA_KEYMAP), new QuickList[0])

    def actionManager = ActionManager.instance
    List<ActionsGroup> allActions = []
    for (group in rootGroup.children) {
      fillGroups(group, allActions, actionManager)
    }
    
    List<KeymapInfo> keymaps = []
    for (keymap in keymapManager.allKeymaps) {
      def keymapInfo = new KeymapInfo(name: keymap.presentableName)
      keymaps << keymapInfo
      for (group in allActions) {
        def filteredGroup = new ActionsGroup(name: group.name)
        for (action in group.actions) {
          def shortcuts = keymap.getShortcuts(action.id)
          if (shortcuts) {
            filteredGroup.actions << new ActionData(id: [action.id], description: action.description, shortcut: shortcuts.toString())
          }
        }
        if (filteredGroup.actions) {
          keymapInfo.actions << filteredGroup
        }
      }
    }
    
    def content = new ExportSettingsControlBuilder().build(keymaps, [ActionsProfile.DEFAULT], allActions)
    BalloonBuilder builder = JBPopupFactory.getInstance().createDialogBalloonBuilder(content, Bundle.message("action.export.keymap.name"));

    builder.setShowCallout(false).setAnimationCycle(0).setCloseButtonEnabled(false).setHideOnClickOutside(true).createBalloon()
      .show(RelativePoint.fromScreen(MouseInfo.getPointerInfo().getLocation()), Balloon.Position.above);
  }
  
  private def fillGroups(@NotNull Group group, @NotNull List<ActionsGroup> holder, @NotNull ActionManager actionManager) {
    def result = new ActionsGroup(name: group.name)
    for (child in group.children) {
      if (child in String) {
        def action = actionManager.getAction(child)
        if (action && action.templatePresentation.text) {
          result.actions << new ActionData(id: [child], description: action.templatePresentation.text)
        }
      }
      else if (child in Group) {
        fillGroups(child, holder, actionManager)
      }
    }
    if (result.actions) {
      holder << result
    }
  }
}
