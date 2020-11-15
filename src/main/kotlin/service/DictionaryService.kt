package service

import domain.Language
import model.Card
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class DictionaryService {
    private val httpClient = HttpClient.newBuilder().build()

    // TODO: make this run parallel in threads?
    fun translate(word: String, language: Language): Card? {
        var card: Card? = null

        if (language == Language.SVENSKA) {
            val request = HttpRequest.newBuilder()
                    .uri(URI.create("https://en.wiktionary.org/w/api.php?titles=$word&action=query&prop=extracts&format=json"))
                    .GET()
                    .build()
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            // TODO: parse http response into appropriate object then try to parse data into a card
            println(response.body())
        } else if (language == Language.DEUTSCH) {
            println("$language IS UNSUPPORTED")
        } else {
            println("$language IS UNSUPPORTED")
        }

        return card
    }
}