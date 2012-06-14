package org.denis.intellij.export.keymap.generator

import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfPCell
import org.jetbrains.annotations.Nullable

/**
 * Encapsulates temporary info used during PDF generation.
 * 
 * @author Denis Zhdanov
 * @since 6/13/12 5:45 PM
 */
class GenerationContext {
  
  def List<PdfPCell> columns = []
  def List<SectionData> data = []
  
  def int currentColumnIndex
  def String keymapName
  def Document document
  def String outputPath
  def boolean realGenerationIteration
  
  def List<Float> rowHeights = []
  def float maxTableHeight
  def float currentHeight

  def updateColumn(int rowsToAdvance, @Nullable Closure columnChangeListener = null) {
    if (!realGenerationIteration || !rowHeights) {
      return
    }
    def newHeight = currentHeight + rowHeights[0..<Math.min(rowHeights.size(), rowsToAdvance)].inject(0, { acc, val -> acc + val  })
    if (newHeight <= maxTableHeight) {
      return
    }
    if (currentColumnIndex >= GenerationConstants.COLUMNS_NUMBER) {
      document.newPage();
      currentColumnIndex = 0;
    }
    else {
      currentColumnIndex++;
    }
    currentHeight = 0;
    columnChangeListener?.call(columns[currentColumnIndex])
  }

  def onNewRow() {
    if (realGenerationIteration) {
      currentHeight += rowHeights.remove(0)
    }
  }
  
  def currentColumn() { columns[currentColumnIndex] }
}
