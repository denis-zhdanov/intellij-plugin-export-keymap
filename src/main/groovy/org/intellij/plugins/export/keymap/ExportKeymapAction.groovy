package org.intellij.plugins.export.keymap

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.keymap.Keymap
import com.intellij.openapi.keymap.ex.KeymapManagerEx
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.TextComponentAccessor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.SystemProperties
import groovy.swing.SwingBuilder
import org.intellij.plugins.export.keymap.model.ActionsProfileManager
import org.intellij.plugins.export.keymap.model.Settings
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

import javax.swing.*
import java.awt.*
import java.util.List

/**
 * @author Denis Zhdanov
 * @since 6/5/12 3:47 PM
 */
class ExportKeymapAction extends AnAction implements DumbAware {

  public ExportKeymapAction() {
    getTemplatePresentation().setText(Bundle.message("action.export.keymap.name"));
    getTemplatePresentation().setDescription(Bundle.message("action.export.keymap.name"));
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
    def pathControl = new TextFieldWithBrowseButton()
    pathControl.addBrowseFolderListener(
      "",
      pathText,
      null,
      FileChooserDescriptorFactory.createSingleFolderDescriptor(),
      TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT,
      false
    );
    pathControl.text = settings.outputPath
    if (pathControl.text == null)
      pathControl.text = SystemProperties.getUserHome()
    
    def keyMapComboBox
    //def useMacButtonsCheckBox
    
    def content = new SwingBuilder().panel() {
      gridBagLayout()
      emptyBorder([15, 15, 10, 15], parent: true)
      def labelConstraints = gbc(anchor: WEST, insets: [0, 0, 5, 5])
      def controlConstraints = gbc(gridwidth: REMAINDER, fill: HORIZONTAL, anchor:  WEST, insets: [0, 4, 5, 0])

      label(text: Bundle.message("label.keymap") + ":", constraints: labelConstraints)
      keyMapComboBox = comboBox(items: keymaps.collect { it.presentableName }, constraints: controlConstraints)

//      useMacButtonsCheckBox = checkBox(text: Bundle.message('label.use.mac.buttons'),
//                                       selected: settings.useMacButtons,
//                                       constraints: gbc(gridwidth: REMAINDER, fill: HORIZONTAL, anchor:  WEST, insets: [0, 0, 5, 0]))

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
    
//    settings.useMacButtons = useMacButtonsCheckBox.selected

    Keymap keymap = keymaps[keyMapComboBox.selectedIndex]
    settings.keymapName = keymap.presentableName

    def path = pathControl.text?.trim()
    settings.outputPath = path
    path = validatePath(path, keymap.presentableName)
    if (!path) {
      return
    }
    
    def profileManager = ServiceManager.getService(ActionsProfileManager)
    new GeneratorFacade().generate(keymap,
                                   profileManager.getProfile(ActionsProfileManager.DEFAULT_PROFILE_NAME),
                                   path,
                                   settings.useMacButtons)

    if (Desktop.isDesktopSupported()) {
      try {

        //TODO. don't ask path
        def homePath = SystemProperties.getUserHome()
        Desktop.getDesktop().open(new File(path));
      } catch (IOException ex) {
      }
    }
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

  public static def showError(@NotNull String text) {
    JBPopupFactory.instance.createHtmlTextBalloonBuilder(text, MessageType.ERROR, null).setShowCallout(false).createBalloon()
      .show(RelativePoint.fromScreen(MouseInfo.getPointerInfo().getLocation()), Balloon.Position.above)
  }
}
