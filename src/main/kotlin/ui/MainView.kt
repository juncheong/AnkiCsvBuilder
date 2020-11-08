package ui

import controller.TranslatedLinesController
import controller.VocabWordsController
import domain.Language
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.ToggleGroup
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File


class MainView : View() {

    private val translatedLinesController: TranslatedLinesController by inject()
    private val vocabWordsController: VocabWordsController by inject()
    private var file: List<File>? = null
    private var directory: File? = null
    private val languages: ObservableList<Language> = FXCollections.observableArrayList(listOf(Language.DEUTSCH, Language.SVENSKA))
    private val toggleGroup = ToggleGroup()

    override val root = form {
        fieldset("Input") {
            button("Choose file") {
                action {
                    file = chooseFile("Select a file to parse",
                            arrayOf(FileChooser.ExtensionFilter("Text file", "*.txt")))

                    if (file != null) {
                        // todo: set output directory the same as input
                    }
                }
            }

            combobox<Language> {
                items = languages
            }

            hbox {
                // TODO:// TODO:
                radiobutton("Lines", toggleGroup)
                radiobutton("Vocab", toggleGroup)
            }
        }


        button("Convert") {
            action {
                println("Converting")
                // TODO:
            }
        }

        textfield {
            // TODO: set output here
        }
    }

    // TODO: add text area to display output
}