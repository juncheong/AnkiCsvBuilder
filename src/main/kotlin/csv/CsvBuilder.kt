package csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.FileWriter

// TODO: Probably need to take the file as an arg
fun buildCsvFromLines() {
    print("File path: ")
    val filePath = readLine()!!
    val outputPath = filePath.substring(0, filePath.indexOfLast { it == '\\' })
    println("output path: $outputPath")

    val cards = mutableListOf<Card>()
    var card: Card? = null
    File(filePath).forEachLine { line ->
        if (line.isBlank() && card != null) {
            cards.add(card!!)
            card = null
        } else {
            if (card == null) {
                card = Card()
            }
            if (card!!.front == null) {
                card!!.front = line
            } else {
                card!!.back = line
            }
        }
    }

    card?.let { cards.add(it) }

    toCsvFile(cards, outputPath)
}

// TODO: Probably need to take the file as an arg
fun buildCsvFromVocabularyWords() {
    // TODO
}

fun toCsvFile(cards: List<Card>, outputPath: String) {
    CSVPrinter(FileWriter("$outputPath\\output.csv"), CSVFormat.DEFAULT).use { printer ->
        cards.forEach { card ->
            printer.printRecord(card.front, card.back)
        }
    }
}