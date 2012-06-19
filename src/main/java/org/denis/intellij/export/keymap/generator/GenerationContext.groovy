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
  boolean realGenerationIteration

  java.util.List<Float> rowHeights = []
  float maxTableHeight
  float currentHeight

  private float headerWidth
  private float headerHeight
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
    addFooter()
    if (currentColumnIndex >= GenerationConstants.COLUMNS_NUMBER) {
      document.newPage();
      currentColumnIndex = 0;
    }
    else {
      currentColumnIndex++;
    }
    currentHeight = 0;
    addHeader()
  }
  
  def addFooter() {
    def font = new Font(FONT_FAMILY, FOOTER_FONT_SIZE, Font.BOLD)
    def dataHandlers = [
      0: [GenerationConstants.HOME_IMAGE_PATH, GenerationConstants.PRODUCT_URL],
      1: [GenerationConstants.BLOG_IMAGE_PATH, GenerationConstants.BLOG_URL],
      2: [GenerationConstants.TWITTER_IMAGE_PATH, GenerationConstants.TWITTER_ID]
    ]

    def footer = new PdfPTable(2)
    footer.widthPercentage = 100f
    def (imgPath, text) = dataHandlers[currentColumnIndex]

    def distinctInfoTable = new PdfPTable(7)
    distinctInfoTable.setWidthPercentage(100f)
    
    def padding = 5f
    def imgCell = new PdfPCell(loadImage(imgPath))
    imgCell.border = Rectangle.NO_BORDER
    imgCell.paddingTop = padding
    distinctInfoTable.addCell(imgCell)
    
    def dataCell = new PdfPCell(new Paragraph(text, font))
    dataCell.border = Rectangle.NO_BORDER
    dataCell.colspan = 6
    dataCell.paddingTop = padding
    dataCell.verticalAlignment = Element.ALIGN_MIDDLE
    distinctInfoTable.addCell(dataCell)
    
    def containerCell = new PdfPCell(distinctInfoTable)
    containerCell.border = Rectangle.NO_BORDER
    containerCell.horizontalAlignment = Element.ALIGN_LEFT
    containerCell.padding = 0f
    footer.addCell(containerCell)
    
    def logoCell = new PdfPCell(loadImage(GenerationConstants.JETBRAINS_LOGO_PATH))
    logoCell.border = Rectangle.NO_BORDER
    logoCell.horizontalAlignment = Element.ALIGN_RIGHT
    logoCell.paddingTop = padding
    footer.addCell(logoCell)
    
    currentColumn().addElement(footer)
  }

  private Image loadImage(@NotNull String path) {
    def url = new URL(null, "classpath:$path", new URLStreamHandler() {
      @Override
      protected URLConnection openConnection(URL u) {
        getClass().classLoader.getResource(path).openConnection()
      }
    })
    def image = Image.getInstance(url)
    image.scaleToFit((headerWidth / 2) as float, (headerHeight / 3 * 2) as float)
    image
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
    currentHeight += headerHeight + PADDING_HEADER_BOTTOM
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
    def header = table.getRow(0).cells[0].compositeElements[0]
    headerWidth = header.totalWidth
    headerHeight = header.totalHeight
    
    // Calculate real row heights. iText doesn't provide an API to do it without flushing the document.
    table.getRow(0).cells[0].compositeElements.rows*.each { rowHeights << it.getCells()[0].height }
    rowHeights.pop()
    maxTableHeight = document.pageSize.height - document.bottomMargin() - document.topMargin()
  }
}
