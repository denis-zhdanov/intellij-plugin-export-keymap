package org.intellij.plugins.export.keymap.generator

import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.intellij.plugins.export.keymap.Bundle
import org.intellij.plugins.export.keymap.ExportKeymapAction
import org.jetbrains.annotations.NotNull
import com.itextpdf.text.*
import org.intellij.plugins.export.keymap.model.*

import static org.intellij.plugins.export.keymap.generator.GenerationConstants.*

/**
 * @author Denis Zhdanov
 * @since 6/13/12 6:10 PM
 */
class Generator {

  def generate(@NotNull java.util.List<DataEntry> data,
               @NotNull String outputPath,
               @NotNull String keymapName)
  {
    def context = new GenerationContext(outputPath: outputPath, data: data, keymapName: keymapName)
    def table = doGenerate(context)
    context.prepareToRealGeneration(table)
    doGenerate(context)
  }

  private def doGenerate(@NotNull GenerationContext context) {
    def document = new Document()
    float margin = 15f
    document.setMargins(margin, margin, margin, margin)
    context.document = document
    document.setPageSize(PageSize.A4.rotate())
    try {
      PdfWriter.getInstance(document, new BufferedOutputStream(new FileOutputStream(context.outputPath)))
    } catch (Exception ex) {
      ExportKeymapAction.showError(Bundle.message('error.output.is.busy'))
    }
    document.addTitle (Bundle.message("document.title", context.productName, context.keymapName))
    document.addAuthor(Bundle.message("document.author"))
    document.addSubject(Bundle.message("document.subject", context.productName, context.keymapName))
    document.open()

    PdfPTable rootTable = new PdfPTable(COLUMNS_NUMBER)
    rootTable.setWidthPercentage(100)
    rootTable.extendLastRow = true
    COLUMNS_NUMBER.times { rootTable.addCell(
      new PdfPCell(paddingLeft: 0f, paddingRight: GAP_BETWEEN_COLUMNS, border: Rectangle.NO_BORDER)
    )}
    rootTable.getRow(0).getCells().each { context.columns << it }

    addData(context)
    addHeaderTable(document, context)
    document.add(rootTable)
    document.close()
    rootTable
  }

  private def addData(@NotNull GenerationContext context) {
    java.util.List<Header> activeHeaders = []
    def visitor = new DataVisitor() {
      @Override
      void visitActionData(@NotNull ActionData data) {
        if (activeHeaders) {
          addSectionTitles(activeHeaders, context)
          activeHeaders.clear()
        }
        
        def paddingTopBottom = 2f
        def paddingLeft = 3f

        def valueParagraph = new Paragraph(data.description, ACTION_DESCRIPTION_FONT)
        valueParagraph.alignment = Element.ALIGN_MIDDLE
        def valueCell = new PdfPCell(valueParagraph)
        valueCell.border = Rectangle.BOTTOM
        valueCell.borderColor = COLOR_BACKGROUND_AND_LINES
        valueCell.paddingTop = paddingTopBottom
        valueCell.paddingBottom = paddingTopBottom
        valueCell.paddingLeft = paddingLeft
        context.dataTable.addCell(valueCell)
        
        def keyParagraph = new Paragraph(data.shortcut, ACTION_SHORTCUT_FONT)
        keyParagraph.alignment = Element.ALIGN_MIDDLE
        def keyCell = new PdfPCell(keyParagraph)
        keyCell.border = Rectangle.BOTTOM
        keyCell.borderColor = COLOR_BACKGROUND_AND_LINES
        keyCell.paddingTop = paddingTopBottom
        keyCell.paddingBottom = paddingTopBottom
        keyCell.paddingLeft = paddingLeft
        keyCell.verticalAlignment = Element.ALIGN_MIDDLE
        context.dataTable.addCell(keyCell)



      }
    
      @Override void visitHeader(@NotNull org.intellij.plugins.export.keymap.model.Header data) { activeHeaders << data }
    
      @Override
      void visitColumnBreak(@NotNull ColumnBreak columnBreak) {
        if (context.realGenerationIteration) {
          context.nextColumn()
        }
      }
    }
    
    context.data.each { it.invite(visitor) }

  }

  private static def addHeaderTable(@NotNull Document document, @NotNull GenerationContext context) {
    if (!context.realGenerationIteration) {
      return
    }
    context.addHeaderTable(document)
  }

  private static void addSectionTitles(@NotNull java.util.List<Header> headers, @NotNull GenerationContext context) {
    context.updateColumn(headers.size() + 1 /* at least one data row */)
    context.newTable()
    def addCell = { String title, boolean withBorder ->
      def titleCell = new PdfPCell(new Paragraph(title, ACTION_GROUP_FONT))
      titleCell.colspan = 2
      titleCell.backgroundColor = COLOR_BACKGROUND_AND_LINES
      if (withBorder) {
        titleCell.border = Rectangle.BOTTOM
        titleCell.borderColor = COLOR_BACKGROUND_AND_LINES
      }
      else {
        titleCell.border = Rectangle.NO_BORDER
      }
      
      context.dataTable.addCell(titleCell)
    }
    for (header in headers.subList(0, headers.size() - 1)) {
      addCell(header.name, false)
    }
    addCell(headers.last().name, true)

  }
}
