package controller

import model.Card
import tornadofx.*
import util.toCsvFile
import java.io.File

class TranslatedLinesController: Controller() {
}

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