package util

import model.Card
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.FileWriter

fun toCsvFile(cards: List<Card>, outputPath: String) {
    CSVPrinter(FileWriter("$outputPath\\output.csv"), CSVFormat.DEFAULT).use { printer ->
        cards.forEach { card ->
            printer.printRecord(card.front, card.back)
        }
    }
}