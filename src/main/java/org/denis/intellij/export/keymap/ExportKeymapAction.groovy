package org.denis.intellij.export.keymap;


import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.keymap.Keymap
import com.intellij.openapi.keymap.ex.KeymapManagerEx
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.BalloonBuilder
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.awt.RelativePoint
import org.denis.intellij.export.keymap.model.ActionsProfile
import org.denis.intellij.export.keymap.ui.ExportSettingsControlBuilder

import java.awt.MouseInfo

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
    def keymapManager = KeymapManagerEx.instanceEx
    List<Keymap> keymaps = keymapManager.allKeymaps as List<Keymap>
    def content = new ExportSettingsControlBuilder().build(keymaps, [ActionsProfile.DEFAULT])
    BalloonBuilder builder = JBPopupFactory.getInstance().createDialogBalloonBuilder(content, Bundle.message("action.export.keymap.name"));

    builder.setShowCallout(false).setAnimationCycle(0).setCloseButtonEnabled(false).setHideOnClickOutside(true).createBalloon()
      .show(RelativePoint.fromScreen(MouseInfo.getPointerInfo().getLocation()), Balloon.Position.above);
  }
  
//  private def fillGroups(@NotNull Group group, @NotNull List<ActionsGroup> holder, @NotNull ActionManager actionManager) {
//    def result = new ActionsGroup(name: group.name)
//    for (child in group.children) {
//      if (child in String) {
//        def action = actionManager.getAction(child)
//        if (action && action.templatePresentation.text) {
//          result.actions << new ActionData(id: [child], description: action.templatePresentation.text)
//        }
//      }
//      else if (child in Group) {
//        fillGroups(child, holder, actionManager)
//      }
//    }
//    if (result.actions) {
//      holder << result
//    }
//  }
}
