package controller

import domain.FileType
import domain.Language
import model.Card
import service.DictionaryService
import service.PronunciationService
import tornadofx.Controller
import util.printToGui
import util.toCsvFile
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue


class VocabWordsController : Controller() {

    private val dictionaryService: DictionaryService by inject()
    private val pronunciationService: PronunciationService by inject()

    // TODO: remove default value for forvoApiKey once the UI starts taking in values
    fun buildCsvFromVocabularyWords(
        file: File,
        outputDirectory: String,
        language: Language,
        forvoApiKey: String? = null
    ) {
        printToGui("Building CSV from vocabulary words.")
        forvoApiKey?.also {
            printToGui("Adding pronunciations using Forvo API Key: $forvoApiKey")
        }

        val words = mutableSetOf<String>()
        file.forEachLine { line ->
            if (line.isNotBlank()) {
                words.add(line.toLowerCase().trim())
            }
        }

        val cards = ConcurrentLinkedQueue<Card>()
        val invalidWords = ConcurrentLinkedQueue<String>()
        words.parallelStream().forEach { word ->
            dictionaryService.translate(word, language)?.also { card ->
                cards.add(card)
                forvoApiKey?.also {
                    pronunciationService.addPronunciation(card, language, forvoApiKey)
                }
            } ?: run {
                invalidWords.add(word)
            }
        }

        toCsvFile(cards, outputDirectory, FileType.VOCAB, language)

        printToGui("Successfully output to csv file.")
        if (!invalidWords.isEmpty()) {
            printToGui("Could not find definition for: ")
            invalidWords.forEach { word -> printToGui(word) }
        }
    }
}

