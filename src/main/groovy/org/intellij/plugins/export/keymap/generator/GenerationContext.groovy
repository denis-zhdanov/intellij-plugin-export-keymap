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

    java.util.List<PdfPCell> listOfColumnsInRootTable = []
    java.util.List<DataEntry> listOfDataEntries = []
    PdfPTable actionShortcutSubTable

    String productName = ApplicationNamesInfo.getInstance().fullProductName;
    int indexOfCurrentColumnInRootTable
    String keymapName
    Document document
    String outputPath
    boolean realGenerationIteration

    java.util.List<Float> rowHeights = []
    /** Max available table height (including header and footer) */
    float maxTableHeight
    float currentHeight
    float headerWidth
    float headerHeight
    /** Height of the tallest column within the target table (including the header but not including the footer). */
    float maxRealColumnHeight

    private boolean first = true

    def updateColumn(int rowsToAdvance) {
        if (first) {
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
        if (indexOfCurrentColumnInRootTable >= COLUMNS_NUMBER-1) {
            document.newPage();
            indexOfCurrentColumnInRootTable = 0;
        } else {
            indexOfCurrentColumnInRootTable++;
        }
        maxRealColumnHeight = Math.max(maxRealColumnHeight, currentHeight)
        currentHeight = 0;
    }

    def createActionShortcutSubTable() {
        actionShortcutSubTable = new PdfPTable(2)
        actionShortcutSubTable.setWidthPercentage(100)
        actionShortcutSubTable.setWidths([5, 4] as float[]);
        try{
            currentColumnInRootTable().addElement(actionShortcutSubTable)}
        catch (Exception ex){
           println(ex.toString())
        }
    }

    def currentColumnInRootTable() { listOfColumnsInRootTable[indexOfCurrentColumnInRootTable] }

    def prepareToRealGeneration(@NotNull PdfPTable table) {
        first = true
        realGenerationIteration = true
        listOfColumnsInRootTable.clear()
        def rootCellElements = table.getRow(0).cells.first().compositeElements
        def header = rootCellElements.first()
        headerWidth = header.totalWidth
        headerHeight = header.totalHeight

        // Calculate real row heights. iText doesn't provide an API to do it without flushing the document.
        rootCellElements.rows*.each { rowHeights << it.cells.max { it?.height }.height }
        rowHeights.remove(0)
        maxTableHeight = document.pageSize.height - document.bottomMargin() - document.topMargin()
    }
}
