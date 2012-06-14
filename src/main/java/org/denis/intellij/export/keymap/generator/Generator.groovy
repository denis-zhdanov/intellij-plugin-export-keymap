package org.denis.intellij.export.keymap.generator

import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPRow
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.denis.intellij.export.keymap.Bundle
import org.jetbrains.annotations.NotNull
import com.itextpdf.text.*

import static org.denis.intellij.export.keymap.generator.GenerationConstants.*
import com.intellij.openapi.util.Pair

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

    PdfPTable rootTable = new PdfPTable(COLUMNS_NUMBER);
    rootTable.setWidthPercentage(100);
    for (i in 0..<COLUMNS_NUMBER) {
      rootTable.addCell(new PdfPCell(paddingLeft: 0f, paddingRight: 20f, border: Rectangle.NO_BORDER))
    }
    rootTable.getRow(0).getCells().each { context.columns << it }

    addData(context);

    PdfPTable headerTable = new PdfPTable(1);
    headerTable.widthPercentage = 100;
    def headerFont = new Font(FONT_FAMILY, HEADER_FONT_SIZE, Font.BOLD, TITLE_COLOR);
    final PdfPCell headerCell = new PdfPCell(new Paragraph(Bundle.message("document.header", context.keymapName), headerFont));
    headerCell.border = Rectangle.NO_BORDER;
    headerTable.addCell(headerCell);
    document.add(headerTable);

    document.add(rootTable);
    document.close();

    // Calculate real row heights. iText doesn't provide an API to do it without flushing the document.
    if (!context.realGenerationIteration) {
      for (element in rootTable.getRow(0).cells[0].compositeElements) {
        PdfPTable table = (PdfPTable)element;
        for (PdfPRow row : table.getRows()) {
          context.rowHeights << row.getCells()[0].getHeight()
        }
      }
      context.maxTableHeight = document.pageSize.height - headerTable.totalHeight - document.bottomMargin() - document.topMargin()
    }
  }

  private def addData(@NotNull GenerationContext context) {
    for (sectionData in context.data) {
      PdfPTable table = addSection(sectionData.name, context)
      for (Pair<String, String> pair : sectionData.actions) {
        table = addRow(table, pair.first, pair.second, context)
      }
    }
  }

  private static PdfPTable addSection(@NotNull String title, @NotNull GenerationContext context) {
    def result = new PdfPTable(2)
    result.setWidthPercentage(100)
    def font = new Font(FONT_FAMILY, ACTION_GROUP_FONT_SIZE, Font.BOLD, TITLE_COLOR)
    def titleCell = new PdfPCell(new Paragraph(title, font))
    titleCell.setBorder(Rectangle.BOTTOM)
    titleCell.setBorderColor(COLOR_BORDER_HEADER);
    titleCell.setColspan(2);
    result.addCell(titleCell);
    
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
    def tableToUse = table;
    context.updateColumn(1, {
      tableToUse = new PdfPTable(2)
      tableToUse.widthPercentage = 100
      it.addElement(tableToUse)
    })

    Font keyFont = new Font(FONT_FAMILY, DATA_FONT_SIZE, Font.BOLD)
    PdfPCell keyCell = new PdfPCell(new Paragraph(key, keyFont))
    keyCell.setBorder(Rectangle.BOTTOM)
    keyCell.setBackgroundColor(COLOR_BACKGROUND_KEY)
    keyCell.setBorderColor(COLOR_BORDER_DATA)
    tableToUse.addCell(keyCell)

    Font valueFont = new Font(FONT_FAMILY, DATA_FONT_SIZE)
    final PdfPCell valueCell = new PdfPCell(new Paragraph(value, valueFont))
    valueCell.setBorder(Rectangle.BOTTOM)
    valueCell.setBorderColor(COLOR_BORDER_DATA)
    tableToUse.addCell(valueCell)
    context.onNewRow()
    tableToUse
  }

}
