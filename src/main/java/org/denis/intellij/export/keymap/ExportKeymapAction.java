package org.denis.intellij.export.keymap;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

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
    // TODO den implement
  }
}
