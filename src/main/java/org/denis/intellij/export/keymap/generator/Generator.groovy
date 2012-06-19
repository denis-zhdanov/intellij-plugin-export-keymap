package org.denis.intellij.export.keymap.generator

import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.denis.intellij.export.keymap.Bundle
import org.jetbrains.annotations.NotNull
import com.itextpdf.text.*
import org.denis.intellij.export.keymap.model.*

import static org.denis.intellij.export.keymap.generator.GenerationConstants.*

/**
 * @author Denis Zhdanov
 * @since 6/13/12 6:10 PM
 */
class Generator {

  def generate(@NotNull java.util.List<DataEntry> data, @NotNull String outputPath, String keymapName) {
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
    PdfWriter.getInstance(document, new BufferedOutputStream(new FileOutputStream(context.outputPath)))
    document.addAuthor(Bundle.message("document.author"))
    document.addSubject(Bundle.message("document.subject", context.keymapName))
    document.open()

    PdfPTable rootTable = new PdfPTable(COLUMNS_NUMBER)
    rootTable.setWidthPercentage(100)
    COLUMNS_NUMBER.times { rootTable.addCell(
      new PdfPCell(paddingLeft: 0f, paddingRight: GAP_BETWEEN_COLUMNS, border: Rectangle.NO_BORDER)
    )}
    rootTable.getRow(0).getCells().each { context.columns << it }

    addData(context)
    addFooter(rootTable, context)

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
          addHeaders(activeHeaders, context)
          activeHeaders.clear()
        }
        
        def paddingTopBottom = 1f
        def paddingLeft = 3f
        
        def keyFont = new Font(FONT_FAMILY, DATA_FONT_SIZE, Font.BOLD)
        def keyCell = new PdfPCell(new Paragraph(data.shortcut, keyFont))
        keyCell.border = Rectangle.BOTTOM
        keyCell.backgroundColor = COLOR_BACKGROUND_KEY
        keyCell.borderColor = COLOR_BORDER_DATA
        keyCell.paddingTop = paddingTopBottom
        keyCell.paddingBottom = paddingTopBottom
        keyCell.paddingLeft = paddingLeft
        context.dataTable.addCell(keyCell)
    
        def valueFont = new Font(FONT_FAMILY, DATA_FONT_SIZE)
        def valueCell = new PdfPCell(new Paragraph(data.description, valueFont))
        valueCell.border = Rectangle.BOTTOM
        valueCell.borderColor = COLOR_BORDER_DATA
        valueCell.paddingTop = paddingTopBottom
        valueCell.paddingBottom = paddingTopBottom
        valueCell.paddingLeft = paddingLeft
        context.dataTable.addCell(valueCell)
        context.onNewRow()
      }
    
      @Override void visitHeader(@NotNull org.denis.intellij.export.keymap.model.Header data) { activeHeaders << data }
    
      @Override
      void visitColumnBreak(@NotNull ColumnBreak columnBreak) {
        if (context.realGenerationIteration) {
          context.nextColumn()
        }
      }
    }
    
    context.data.each { it.invite(visitor) }
  }
  
  private def addFooter(@NotNull PdfPTable rootTable, @NotNull GenerationContext context) {
    if (!context.realGenerationIteration) {
      return
    }
    
    def font = new Font(FONT_FAMILY, FOOTER_FONT_SIZE, Font.BOLD)

    def dataInfo = [
      [GenerationConstants.HOME_IMAGE_PATH, GenerationConstants.PRODUCT_URL],
      [GenerationConstants.BLOG_IMAGE_PATH, GenerationConstants.BLOG_URL],
      [GenerationConstants.TWITTER_IMAGE_PATH, GenerationConstants.TWITTER_ID]
    ]
    
    dataInfo.eachWithIndex { data, i ->
      def footer = new PdfPTable(2)
      footer.widthPercentage = 100f
      def (imgPath, text) = data

      def distinctInfoTable = new PdfPTable(7)
      distinctInfoTable.setWidthPercentage(100f)

      def padding = 5f
      def imgCell = new PdfPCell(loadImage(imgPath, context))
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

      def logoCell = new PdfPCell(loadImage(GenerationConstants.JETBRAINS_LOGO_PATH, context))
      logoCell.border = Rectangle.NO_BORDER
      logoCell.horizontalAlignment = Element.ALIGN_RIGHT
      logoCell.paddingTop = padding
      footer.addCell(logoCell)
      
      def footerCell = new PdfPCell(footer)
      footerCell.paddingLeft = 0f
      footerCell.paddingRight = GAP_BETWEEN_COLUMNS
      footerCell.border = Rectangle.NO_BORDER
      rootTable.addCell(footerCell)
    }
  }
  
  private static Image loadImage(@NotNull String path, @NotNull GenerationContext context) {
    def url = new URL(null, "classpath:$path", new URLStreamHandler() {
      @Override
      protected URLConnection openConnection(URL u) {
        getClass().classLoader.getResource(path).openConnection()
      }
    })
    def image = Image.getInstance(url)
    image.scaleToFit((context.headerWidth / 2) as float, (context.headerHeight / 3 * 2) as float)
    image
  }
  
  private static void addHeaders(@NotNull java.util.List<Header> headers, @NotNull GenerationContext context) {
    context.updateColumn(headers.size() + 1 /* at least one data row */)
    context.newTable()
    def font = new Font(FONT_FAMILY, ACTION_GROUP_FONT_SIZE, Font.BOLD, TITLE_COLOR)
    def addCell = { String title, boolean withBorder ->
      def titleCell = new PdfPCell(new Paragraph(title, font))
      titleCell.colspan = 2
      if (withBorder) {
        titleCell.border = Rectangle.BOTTOM
        titleCell.borderColor = COLOR_BORDER_HEADER
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
    headers.size().times { context.onNewRow() }
  }
}
