package org.intellij.plugins.export.keymap.generator

import com.intellij.openapi.application.ApplicationNamesInfo
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import org.intellij.plugins.export.keymap.Bundle
import org.intellij.plugins.export.keymap.model.DataEntry
import org.jetbrains.annotations.NotNull

import com.itextpdf.text.*

import static org.intellij.plugins.export.keymap.generator.GenerationConstants.*

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
//  float goToActionTextHeight
    /** Height of the tallest column within the target table (including the header but not including the footer). */
    float maxRealColumnHeight

    private boolean first = true

    def updateColumn(int rowsToAdvance) {
        if (first) {
            //addHeader()
            first = false
        }
        if (!realGenerationIteration || !rowHeights) {
            return
        }
        def newHeight = currentHeight + rowHeights[0..<Math.min(rowHeights.size(), rowsToAdvance)].inject(0, { acc, val -> acc + val })
        if (newHeight > maxTableHeight) {
            nextColumn()
        }
    }

    def nextColumn() {
        if (currentColumnIndex >= COLUMNS_NUMBER-1) {
            document.newPage();
            currentColumnIndex = 0;
        } else {
            currentColumnIndex++;
        }
        maxRealColumnHeight = Math.max(maxRealColumnHeight, currentHeight)
        currentHeight = 0;
        //addHeader()
    }

//    private void addHeader() {
//        def appName = ApplicationNamesInfo.getInstance().fullProductName;
//        dataTable = new PdfPTable(1)
//        dataTable.widthPercentage = 100
//        def headerCell =
//                new PdfPCell(new Paragraph(Bundle.message("document.header", appName, keymapName), HEADER_FONT))
//        headerCell.border = Rectangle.NO_BORDER
//        headerCell.paddingBottom = PADDING_HEADER_BOTTOM
//        dataTable.addCell(headerCell)
//        currentColumn().addElement(dataTable)
//        currentHeight += headerHeight
//    }

    void addHeaderTable(Document document) {
        def appName = ApplicationNamesInfo.getInstance().fullProductName;
        PdfPTable headerTable = new PdfPTable(1)
        headerTable.setWidthPercentage(100)
        def headerCell =
                new PdfPCell(new Paragraph(Bundle.message("document.header", appName, keymapName), HEADER_FONT))
        headerCell.border = Rectangle.NO_BORDER
        headerCell.paddingBottom = PADDING_HEADER_BOTTOM
        headerCell.paddingTop = 0f
        headerTable.addCell(headerCell)
        document.add(headerTable)
    }

    def newTable() {
        dataTable = new PdfPTable(2)
        dataTable.setWidthPercentage(100)
        dataTable.setWidths([4, 3] as float[]);
        try{
        currentColumn().addElement(dataTable)}
        catch (Exception ex){
           println()
        }
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

//    goToActionTextHeight = rootCellElements.last().totalHeight

        // Calculate real row heights. iText doesn't provide an API to do it without flushing the document.
        rootCellElements.rows*.each { rowHeights << it.cells.max { it?.height }.height }
        rowHeights.remove(0)
        maxTableHeight = document.pageSize.height - document.bottomMargin() - document.topMargin()
    }
}
