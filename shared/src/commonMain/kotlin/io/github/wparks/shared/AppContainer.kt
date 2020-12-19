package io.github.wparks.shared

import io.github.wparks.shared.data.ParkApi
import io.github.wparks.shared.data.ParkRepository
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

class AppContainer {

    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val parkApi = ParkApi(httpClient)

    val repository = ParkRepository(parkApi)
}