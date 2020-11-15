package controller

import domain.FileType
import domain.Language
import model.Card
import service.DictionaryService
import tornadofx.Controller
import util.toCsvFile
import java.io.File

class VocabWordsController: Controller() {

    private val dictionaryService = DictionaryService()

    fun buildCsvFromVocabularyWords(file: File, outputDirectory: String, language: Language) {
        val cards = mutableListOf<Card>()

        file.forEachLine { word ->
            if (!word.isBlank()) {
                val card: Card? = dictionaryService.translate(word, language)
                if (card != null) {
                    cards.add(card)
                } else {
                    //TODO: make this appear on the GUI
                    println("Could not find definition for $word")
                }
            }
        }

        toCsvFile(cards, outputDirectory, FileType.VOCAB, language)
    }
}

