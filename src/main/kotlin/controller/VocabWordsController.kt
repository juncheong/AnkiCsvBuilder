package controller

import domain.FileType
import domain.Language
import model.Card
import service.DictionaryService
import tornadofx.Controller
import util.toCsvFile
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue


class VocabWordsController : Controller() {

    private val dictionaryService = DictionaryService()

    fun buildCsvFromVocabularyWords(file: File, outputDirectory: String, language: Language) {
        val words = mutableSetOf<String>()
        file.forEachLine { line ->
            if (line.isNotBlank()) {
                words.add(line.toLowerCase().trim())
            }
        }

        val cards = ConcurrentLinkedQueue<Card>()
        val invalidWords = ConcurrentLinkedQueue<String>()
        words.parallelStream().forEach { word ->
            val card: Card? = dictionaryService.translate(word, language)
            if (card != null) {
                cards.add(card)
            } else {
                invalidWords.add(word)
            }
        }

        toCsvFile(cards, outputDirectory, FileType.VOCAB, language)

        // todo: output this to GUI
        println("Could not find words for: ")
        invalidWords.forEach { word -> println(word) }
    }
}

