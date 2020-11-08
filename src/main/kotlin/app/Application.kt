package app

import tornadofx.*
import ui.MainView

class Application : App(MainView::class)

fun main(args: Array<String>) {
    launch<Application>(args)
}