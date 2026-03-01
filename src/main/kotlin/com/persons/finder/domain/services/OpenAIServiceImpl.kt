package com.persons.finder.domain.services

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.RestClientException

@Service
class OpenAIServiceImpl(
    @Value("\${openai.api.key}")
    private val apiKey: String,
    @Value("\${openai.model:gpt-3.5-turbo}")
    private val model: String
) : OpenAIService {

    private val restTemplate = RestTemplate()
    private val apiUrl = "https://api.openai.com/v1/chat/completions"

    override fun generateSelfIntro(person: com.persons.finder.data.Person): Pair<String, String> {
        return try {
            val prompt = "Generate a short, quirky bio (2-3 sentences) and a brief summary (1 paragraph) for a person named ${person.name} who works as a ${person.jobTitle} and enjoys ${person.hobby}. Return the response in the format: BIO|||SUMMARY"

            val request = ChatCompletionRequest(
                model = model,
                messages = listOf(
                    MessageRequest(
                        role = "user",
                        content = prompt
                    )
                ),
                temperature = 0.9
            )

            val response = callOpenAI(request)
            val responseText = response.choices.firstOrNull()?.message?.content ?: ""
            println("responseText is " + responseText)

            val parts = responseText.split("|||")
            val intro = if (parts.isNotEmpty()) parts[0].trim() else responseText
            val summary = if (parts.size > 1) parts[1].trim() else "A brief introduction about ${person.name}."

            Pair(intro, summary)
        } catch (_: Exception) {
            // Fallback to mock if API call fails
            val intro = "Hi, I'm ${person.name}. I enjoy ${person.hobby} and work as a ${person.jobTitle}."
            val summary = "A brief introduction about ${person.name}."
            Pair(intro, summary)
        }
    }

    override fun getCountryAndCityByCoordinates(latitude: Double, longitude: Double): Pair<String, String> {
        return try {
            val prompt = "Based on the geographic coordinates latitude=$latitude and longitude=$longitude, provide only the country name and city name in the format: COUNTRY|CITY. If the coordinates are in the ocean or an invalid location, use best estimates."

            val request = ChatCompletionRequest(
                model = model,
                messages = listOf(
                    MessageRequest(
                        role = "user",
                        content = prompt
                    )
                ),
                temperature = 0.3
            )

            val response = callOpenAI(request)
            val responseText = response.choices.firstOrNull()?.message?.content ?: ""

            val parts = responseText.split("|")
            val country = if (parts.isNotEmpty()) parts[0].trim() else "Unknown"
            val city = if (parts.size > 1) parts[1].trim() else "Unknown"

            Pair(country, city)
        } catch (_: Exception) {
            // Fallback to mock if API call fails
            Pair("Unknown Country", "Unknown City")
        }
    }

    private fun callOpenAI(request: ChatCompletionRequest): ChatCompletionResponse {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Bearer $apiKey")
        }

        val entity = HttpEntity(request, headers)
        return restTemplate.postForObject(apiUrl, entity, ChatCompletionResponse::class.java)
            ?: throw RestClientException("Empty response from OpenAI API")
    }

    // Data classes for request/response
    data class ChatCompletionRequest(
        val model: String,
        val messages: List<MessageRequest>,
        val temperature: Double = 0.7
    )

    data class MessageRequest(
        val role: String,
        val content: String
    )

    data class ChatCompletionResponse(
        val id: String = "",
        val `object`: String = "",
        val created: Long = 0L,
        val model: String = "",
        val choices: List<ChoiceResponse> = emptyList(),
        val usage: UsageResponse? = null
    )

    data class ChoiceResponse(
        val index: Int = 0,
        val message: MessageResponse = MessageResponse(),
        val finish_reason: String = ""
    )

    data class MessageResponse(
        val role: String = "",
        val content: String = ""
    )

    data class UsageResponse(
        val prompt_tokens: Int = 0,
        val completion_tokens: Int = 0,
        val total_tokens: Int = 0
    )
}
