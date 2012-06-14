package org.denis.intellij.export.keymap;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.denis.intellij.export.keymap.ui.ExportModel;
import org.denis.intellij.export.keymap.ui.ExportSettingsControlBuilder;

import javax.swing.*;
import java.awt.*;

/**
 * @author Denis Zhdanov
 * @since 6/5/12 3:47 PM
 */
public class ExportKeymapAction extends AnAction {

  public ExportKeymapAction() {
    getTemplatePresentation().setText(Bundle.message("action.export.keymap.name"));
    getTemplatePresentation().setDescription(Bundle.message("action.export.keymap.name"));
  }
  
  @Override
  public void actionPerformed(AnActionEvent event) {
    Project project = PlatformDataKeys.PROJECT.getData(event.getDataContext());
    if (project == null) {
      project = ProjectManager.getInstance().getDefaultProject();
    }
    ExportModel model = new ExportModel();
    for (Keymap keymap : KeymapManagerEx.getInstanceEx().getAllKeymaps()) {
      model.getKeymapNames().add(keymap.getName());
    }
    BalloonBuilder builder = JBPopupFactory.getInstance().createBalloonBuilder(new ExportSettingsControlBuilder().build(model));

    JFrame frame = WindowManager.getInstance().getFrame(project);
    if (frame == null) {
      return;
    }

    builder.setShowCallout(false).createBalloon().show(RelativePoint.fromScreen(MouseInfo.getPointerInfo().getLocation()),
                                                       Balloon.Position.above);
  }
}
