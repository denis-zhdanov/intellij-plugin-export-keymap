package org.denis.intellij.export.keymap.generator

import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import org.denis.intellij.export.keymap.Bundle
import org.denis.intellij.export.keymap.model.DataEntry
import org.jetbrains.annotations.NotNull

import com.itextpdf.text.*

import static org.denis.intellij.export.keymap.generator.GenerationConstants.*

/**
 * Encapsulates temporary info used during PDF generation.
 * 
 * @author Denis Zhdanov
 * @since 6/13/12 5:45 PM
 */
class GenerationContext {
  
  java.util.List<PdfPCell> columns = []
  java.util.List<DataEntry> data = []
  PdfPTable dataTable
  
  int currentColumnIndex
  String keymapName
  Document document
  String outputPath
  String goToActionShortcut
  boolean realGenerationIteration

  java.util.List<Float> rowHeights = []
  /** Max available table height (including header and footer) */
  float maxTableHeight
  float currentHeight
  float headerWidth
  float headerHeight
  float goToActionTextHeight
  /** Height of the tallest column within the target table (including the header but not including the footer). */
  float maxRealColumnHeight

  private boolean first = true

  def updateColumn(int rowsToAdvance) {
    if (first) {
      addHeader()
      first = false
    }
    if (!realGenerationIteration || !rowHeights) {
      return
    }
    def newHeight = currentHeight + rowHeights[0..<Math.min(rowHeights.size(), rowsToAdvance)].inject(0, { acc, val -> acc + val  })
    if (newHeight > maxTableHeight) {
      nextColumn()
    }
  }

  def nextColumn() {
    if (currentColumnIndex >= GenerationConstants.COLUMNS_NUMBER) {
      document.newPage();
      currentColumnIndex = 0;
    }
    else {
      currentColumnIndex++;
    }
    maxRealColumnHeight = Math.max(maxRealColumnHeight, currentHeight)
    currentHeight = 0;
    addHeader()
  }
  
  private void addHeader() {
    dataTable = new PdfPTable(1)
    dataTable.widthPercentage = 100
    def headerFont = new Font(FONT_FAMILY, HEADER_FONT_SIZE, Font.BOLD, TITLE_COLOR)
    def headerCell = new PdfPCell(new Paragraph(Bundle.message("document.header", keymapName), headerFont))
    headerCell.border = Rectangle.NO_BORDER
    headerCell.paddingBottom = PADDING_HEADER_BOTTOM
    dataTable.addCell(headerCell)
    currentColumn().addElement(dataTable)
    currentHeight += headerHeight
  }
  
  def newTable() {
    dataTable = new PdfPTable(2)
    dataTable.setWidthPercentage(100)
    currentColumn().addElement(dataTable)
  }
  
  def onNewRow() {
    if (realGenerationIteration) {
      currentHeight += rowHeights.remove(0)
    }
  }
  
  def currentColumn() { columns[currentColumnIndex] }

  def prepareToRealGeneration(@NotNull PdfPTable table) {
    first = true
    realGenerationIteration = true
    columns.clear()
    def rootCellElements = table.getRow(0).cells.first().compositeElements
    def header = rootCellElements.first()
    headerWidth = header.totalWidth
    headerHeight = header.totalHeight
    
    goToActionTextHeight = rootCellElements.last().totalHeight
    
    // Calculate real row heights. iText doesn't provide an API to do it without flushing the document.
    rootCellElements.rows*.each { rowHeights << it.cells.max { it?.height }.height }
    rowHeights.remove(0)
    maxTableHeight = document.pageSize.height - document.bottomMargin() - document.topMargin()
  }
}
