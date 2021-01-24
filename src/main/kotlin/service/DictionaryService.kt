package service

import com.google.gson.Gson
import domain.Language
import model.Card
import model.WiktionaryResponse
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


class DictionaryService {

    private val numRegex = ".*[0-9].*".toRegex()
    private val alphaRegex = ".*[a-zA-Z].*".toRegex()

    private val httpClient = HttpClient.newBuilder().build()
    private val gson = Gson()

    fun translate(word: String, language: Language): Card? {
        val elements = getElementsFromApi(word)

        return when (language) {
            Language.SVENSKA -> {
                getDefinition(word, elements, Language.SVENSKA) ?:
                getDefinition(word.replaceFirst(word[0], word[0].toUpperCase()), elements, Language.SVENSKA)
            }
            Language.DEUTSCH -> {
                getDefinition(word, elements, Language.DEUTSCH) ?:
                getDefinition(word.replaceFirst(word[0], word[0].toUpperCase()), elements, Language.DEUTSCH)
            }
        }
    }

    private fun getElementsFromApi(word: String): Elements {
        val encodedWord = URLEncoder.encode(word, "UTF-8")
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://en.wiktionary.org/w/api.php?titles=$encodedWord&action=query&prop=extracts&format=json"))
            .GET()
            .build()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val htmlResponse = gson.fromJson(response.body(), WiktionaryResponse::class.java)
            .query!!
            .pages!!
            .iterator()
            .next()
            .value
            .extract

        return Jsoup.parse(htmlResponse.toString()).select("*")
    }

    private fun getDefinition(word: String, elements: Elements, language: Language): Card? {
        var i = 0
        while (i < elements.size && elements[i].id() != language.inEnglish) {
            i++
        }
        i++
        val definitions = StringBuilder()
        var printSection = true
        while (i < elements.size && elements[i].tagName() != "h2" && elements[i].tagName() != "h4") {

            val element = elements[i]
            val elementId = element.id().toLowerCase()
            val elementTagName = element.tagName()

            if (elementId.startsWith("etymology") ||
                elementId.startsWith("pronunciation") ||
                elementId.startsWith("anagrams")) {
                printSection = false
            } else if (printSection || elementTagName == "h3") {
                printSection = true
                val text = element.ownText()

                if (isValidText(text)) {
                    if (elementTagName.startsWith("span") && elements[i - 1].tagName() == "h3") {
                        definitions.appendLine()
                    }
                    definitions.appendLine(text)
                }
            }
            i++
        }
        return if (definitions.isBlank()) null else Card(word.replaceFirst(word[0], word[0].toUpperCase()), definitions.toString())
    }

    private fun isValidText(text: String): Boolean {
        return text.isNotBlank() &&
                text.length > 1 &&
                (text.matches(numRegex) || text.matches(alphaRegex))
    }
}