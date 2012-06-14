package org.denis.intellij.export.keymap.generator

import com.itextpdf.text.Document
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import org.denis.intellij.export.keymap.Bundle
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

import static org.denis.intellij.export.keymap.generator.GenerationConstants.*

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
  
  private float headerHeight
  private boolean first = true

  def updateColumn(int rowsToAdvance, @Nullable Closure columnChangeListener = null) {
    def addHeaderClosure = {
      def headerTable = new PdfPTable(1)
      headerTable.widthPercentage = 100
      def headerFont = new Font(FONT_FAMILY, HEADER_FONT_SIZE, Font.BOLD, TITLE_COLOR)
      def headerCell = new PdfPCell(new Paragraph(Bundle.message("document.header", keymapName), headerFont))
      headerCell.border = Rectangle.NO_BORDER
      headerCell.paddingBottom = PADDING_HEADER_BOTTOM
      headerTable.addCell(headerCell)
      currentColumn().addElement(headerTable)
      currentHeight += headerHeight + PADDING_HEADER_BOTTOM
    }
    
    if (first && realGenerationIteration) {
      addHeaderClosure()
      first = false
    }
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
    addHeaderClosure()
    
    columnChangeListener?.call(columns[currentColumnIndex])
  }

  def onNewRow() {
    if (realGenerationIteration) {
      currentHeight += rowHeights.remove(0)
    }
  }
  
  def currentColumn() { columns[currentColumnIndex] }

  def prepareToRealGeneration(@NotNull PdfPTable table) {
    realGenerationIteration = true
    columns.clear()
    headerHeight = table.getRow(0).cells[0].compositeElements[0].totalHeight
    
    // Calculate real row heights. iText doesn't provide an API to do it without flushing the document.
    table.getRow(0).cells[0].compositeElements.rows*.each { rowHeights << it.getCells()[0].height }
    maxTableHeight = document.pageSize.height - document.bottomMargin() - document.topMargin()
  }
}
