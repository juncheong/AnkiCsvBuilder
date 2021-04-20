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

    private val httpClient = HttpClient.newBuilder().build()
    private val gson = Gson()

    fun translate(word: String, language: Language): Card? {
        val elements = getFromWiktionaryApi(word)

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

    private fun getFromWiktionaryApi(word: String): Elements {
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

    // TODO: perhaps in the future, it might make sense to return to parsing wiktionary data based on the chosen language
    private fun getDefinition(word: String, elements: Elements, language: Language): Card? {
        val definitions = StringBuilder()
        definitions.append(elements.html())
        return if (definitions.isBlank()) null else Card(word.replaceFirst(word[0], word[0].toUpperCase()), definitions.toString())
    }
}