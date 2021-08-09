package io.github.wparks.shared

import com.russhwolf.settings.Settings
import io.github.wparks.shared.data.ParkRepository
import io.github.wparks.shared.data.db.DbContainer
import io.github.wparks.shared.data.remote.ParkApi
import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

class AppContainer(dbContainer: DbContainer, dispatcher: CoroutineDispatcher) {

    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val parkApi = ParkApi(httpClient)

    private val settings = Settings()

    val repository = ParkRepository(parkApi, dbContainer.database.parkQueries, settings, dispatcher)
}