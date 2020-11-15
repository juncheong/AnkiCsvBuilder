package ui

import controller.TranslatedLinesController
import controller.VocabWordsController
import domain.FileType
import domain.Language
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.ComboBox
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.stage.FileChooser
import tornadofx.View
import tornadofx.action
import tornadofx.button
import tornadofx.chooseFile
import tornadofx.combobox
import tornadofx.fieldset
import tornadofx.form
import tornadofx.hbox
import tornadofx.radiobutton
import tornadofx.selectedItem
import tornadofx.singleAssign
import java.io.File


class MainView : View() {

    private val SUPPORTED_FILE_TYPES = arrayOf(FileChooser.ExtensionFilter("Text file", "*.txt"))
    private val LANGUAGES: ObservableList<Language> = FXCollections.observableArrayList(listOf(Language.DEUTSCH, Language.SVENSKA))

    private val translatedLinesController: TranslatedLinesController by inject()
    private val vocabWordsController: VocabWordsController by inject()
    private var languageSelection: ComboBox<Language> by singleAssign()
    private val inputFormatToggle = ToggleGroup()
    private var file: File? = null
    private var directory: String? = null

    override val root = form {
        fieldset("Input") {
            button("Choose file") {
                action {
                    val fileList = chooseFile("Select a file to parse", SUPPORTED_FILE_TYPES)

                    if (fileList.isNotEmpty()) {
                        file = fileList[0]
                        directory = file!!.parent
                        text = file!!.name
                    }
                }
            }

            combobox<Language> {
                items = LANGUAGES
                languageSelection = this
            }

            hbox {
                radiobutton(FileType.LINES.toString(), inputFormatToggle)
                radiobutton(FileType.VOCAB.toString(), inputFormatToggle)
            }
        }


        button("Convert") {
            action {
                if (file != null && directory != null && languageSelection.selectedItem != null && inputFormatToggle.selectedToggle != null) {
                    val selectedButton: RadioButton = inputFormatToggle.selectedToggle as RadioButton
                    if (FileType.valueOf(selectedButton.text) == FileType.LINES) {
                        translatedLinesController.buildCsvFromLines(file!!, directory!!, languageSelection.selectedItem!!)
                    } else {
                        vocabWordsController.buildCsvFromVocabularyWords(file!!, directory!!, languageSelection.selectedItem!!)
                    }
                } else {
                    // TODO: Output some kind of error to GUI?
                    println("Something is null")
                }
            }
        }
    }
}