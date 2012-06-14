package org.denis.intellij.export.keymap.generator

import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.denis.intellij.export.keymap.Bundle
import org.jetbrains.annotations.NotNull
import com.itextpdf.text.*

import static org.denis.intellij.export.keymap.generator.GenerationConstants.*

/**
 * @author Denis Zhdanov
 * @since 6/13/12 6:10 PM
 */
class Generator {
  
  def generate(@NotNull java.util.List<SectionData> data, @NotNull String outputPath, @NotNull String keymapName) {
    def context = new GenerationContext(outputPath: outputPath, data: data, keymapName: keymapName)
    doGenerate(context)
    context.realGenerationIteration = true
    context.columns.clear()
    doGenerate(context)
  }

  private def doGenerate(@NotNull GenerationContext context) {
    def document = new Document()
    context.document = document
    document.setPageSize(PageSize.A4.rotate())
    PdfWriter.getInstance(document, new BufferedOutputStream(new FileOutputStream(context.outputPath)))
    document.addAuthor(Bundle.message("document.author"))
    document.addSubject(Bundle.message("document.subject", context.keymapName))
    document.open()

    PdfPTable rootTable = new PdfPTable(COLUMNS_NUMBER)
    rootTable.setWidthPercentage(100)
    COLUMNS_NUMBER.times { rootTable.addCell(new PdfPCell(paddingLeft: 0f, paddingRight: 20f, border: Rectangle.NO_BORDER)) }
    rootTable.getRow(0).getCells().each { context.columns << it }

    addData(context)

    PdfPTable headerTable = new PdfPTable(1)
    headerTable.widthPercentage = 100
    def headerFont = new Font(FONT_FAMILY, HEADER_FONT_SIZE, Font.BOLD, TITLE_COLOR)
    def headerCell = new PdfPCell(new Paragraph(Bundle.message("document.header", context.keymapName), headerFont))
    headerCell.border = Rectangle.NO_BORDER
    headerTable.addCell(headerCell)
    document.add(headerTable)

    document.add(rootTable)
    document.close()

    // Calculate real row heights. iText doesn't provide an API to do it without flushing the document.
    if (!context.realGenerationIteration) {
      rootTable.getRow(0).cells[0].compositeElements.rows*.each { context.rowHeights << it.getCells()[0].height }
      context.maxTableHeight = document.pageSize.height - headerTable.totalHeight - document.bottomMargin() - document.topMargin()
    }
  }

  private def addData(@NotNull GenerationContext context) {
    for (sectionData in context.data) {
      def table = addSection(sectionData.name, context)
      sectionData.actions.each { table = addRow(table, it.first, it.second, context) }
    }
  }

  private static PdfPTable addSection(@NotNull String title, @NotNull GenerationContext context) {
    def result = new PdfPTable(2)
    result.setWidthPercentage(100)
    def font = new Font(FONT_FAMILY, ACTION_GROUP_FONT_SIZE, Font.BOLD, TITLE_COLOR)
    def titleCell = new PdfPCell(new Paragraph(title, font))
    titleCell.border = Rectangle.BOTTOM
    titleCell.borderColor = COLOR_BORDER_HEADER
    titleCell.colspan = 2
    result.addCell(titleCell)
    
    context.updateColumn(2)
    context.currentColumn().addElement(result)
    context.onNewRow()
    result
  }

  private static PdfPTable addRow(final @NotNull PdfPTable table,
                                  @NotNull String key,
                                  @NotNull String value,
                                  @NotNull GenerationContext context)
  {
    def tableToUse = table
    context.updateColumn(1, {
      tableToUse = new PdfPTable(2)
      tableToUse.widthPercentage = 100
      it.addElement(tableToUse)
    })

    def keyFont = new Font(FONT_FAMILY, DATA_FONT_SIZE, Font.BOLD)
    def keyCell = new PdfPCell(new Paragraph(key, keyFont))
    keyCell.border = Rectangle.BOTTOM
    keyCell.backgroundColor = COLOR_BACKGROUND_KEY
    keyCell.borderColor = COLOR_BORDER_DATA
    tableToUse.addCell(keyCell)

    def valueFont = new Font(FONT_FAMILY, DATA_FONT_SIZE)
    def valueCell = new PdfPCell(new Paragraph(value, valueFont))
    valueCell.border = Rectangle.BOTTOM
    valueCell.borderColor = COLOR_BORDER_DATA
    tableToUse.addCell(valueCell)
    context.onNewRow()
    tableToUse
  }
}
