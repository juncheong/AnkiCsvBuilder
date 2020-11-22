package service

import com.google.gson.Gson
import domain.Language
import model.Card
import model.WiktionaryResponse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


class DictionaryService {
    private val httpClient = HttpClient.newBuilder().build()
    private val gson = Gson()

    // TODO: make this run parallel in threads?
    fun translate(word: String, language: Language): Card? {
        var card: Card? = null

        when (language) {
            Language.SVENSKA -> {
                val request = HttpRequest.newBuilder()
                        .uri(URI.create("https://en.wiktionary.org/w/api.php?titles=$word&action=query&prop=extracts&format=json"))
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

                // TODO: break apart the html data
                println(htmlResponse.toString())
            }
            Language.DEUTSCH -> {
                println("$language IS UNSUPPORTED")
            }
            else -> {
                println("$language IS UNSUPPORTED")
            }
        }

        return card
    }
}