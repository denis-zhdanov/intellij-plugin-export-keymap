package org.denis.intellij.export.keymap;


import com.intellij.ide.util.projectWizard.NamePathComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.keymap.ex.KeymapManagerEx
import com.intellij.openapi.ui.DialogWrapper
import groovy.swing.SwingBuilder
import org.denis.intellij.export.keymap.generator.GeneratorFacade
import org.denis.intellij.export.keymap.model.ActionsProfile
import org.denis.intellij.export.keymap.model.Settings

import javax.swing.JComponent
import com.intellij.openapi.keymap.Keymap
import org.jetbrains.annotations.Nullable
import org.jetbrains.annotations.NotNull
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.awt.RelativePoint
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
  public void actionPerformed(AnActionEvent e) {
    def settings = Settings.instance
    def keymaps = KeymapManagerEx.instanceEx.allKeymaps
    keymaps = keymaps.sort { a, b ->
      if (a.name == settings.keymapName) return -1
      if (b.name == settings.keymapName) return 1
      a.name <=> b.name
    }
    
    def pathText = Bundle.message('label.path')
    def pathControl = new NamePathComponent('', '', pathText, '', false, false)
    pathControl.nameComponentVisible = false
    pathControl.pathPanel.remove(pathControl.pathLabel)
    pathControl.path = settings.outputPath
    def keyMapComboBox
    def content = new SwingBuilder().panel() {
      gridBagLayout()
      emptyBorder([15, 15, 10, 15], parent: true)
      def labelConstraints = gbc(anchor: WEST, insets: [0, 0, 5, 5])
      def controlConstraints = gbc(gridwidth: REMAINDER, fill: HORIZONTAL, insets: [0, 4, 5, 0])
      
      label(text: Bundle.message("label.keymap") + ":", constraints: labelConstraints)
      keyMapComboBox = comboBox(items: keymaps.collect { it.presentableName }, constraints: controlConstraints)

      label("$pathText:", constraints: labelConstraints)
      controlConstraints.insets.left = 0
      widget(pathControl, constraints: controlConstraints)
    }
    
    def dialog = new DialogWrapper(PlatformDataKeys.PROJECT.getData(e.dataContext)) {
      
      {
        init()
      }
      
      @Override protected JComponent createCenterPanel() { content }
    }
    dialog.title = Bundle.message('action.export.keymap.name')
    
    dialog.show()
    if (!dialog.OK) {
      return
    }

    Keymap keymap = keymaps[keyMapComboBox.selectedIndex]
    settings.keymapName = keymap.presentableName
    
    def path = pathControl.path?.trim()
    settings.outputPath = path
    path = validatePath(path, keymap.presentableName)
    if (!path) {
      return
    }
    
    new GeneratorFacade().generate(keymap, ActionsProfile.DEFAULT, path)
  }

  @Nullable
  private String validatePath(@Nullable String path, @NotNull String keymapName) {
    if (!path) {
      showError(Bundle.message('error.output.is.undefined'))
      return null
    }

    def file = new File(path)
    if (file.file) {
      return null
    }
    if (file.directory) {
      return "${file.canonicalPath}/Keymap-${keymapName}.pdf"
    }

    List<File> pathEntries = []
    def parent = file.parentFile
    while (parent) {
      pathEntries << parent
      parent = parent.parentFile
    }

    pathEntries = pathEntries.reverse()
    for (entry in pathEntries) {
      if (entry.file) {
        showError(Bundle.message('error.output.path.entry.is.file', path, entry.path))
        return null
      }
      else if (!entry.exists()) {
        if (!entry.mkdir()) {
          showError(Bundle.message('error.cant.create.dir', path, entry.path))
          return null
        }
      }
    }

    path
  }

  private def showError(@NotNull String text) {
    JBPopupFactory.instance.createHtmlTextBalloonBuilder(text, MessageType.ERROR, null).setShowCallout(false).createBalloon()
      .show(RelativePoint.fromScreen(MouseInfo.getPointerInfo().getLocation()), Balloon.Position.above)
  }
}
