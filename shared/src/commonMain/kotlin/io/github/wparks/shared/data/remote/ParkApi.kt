package io.github.wparks.shared.data.remote

import io.github.wparks.shared.data.ParkInfo
import io.github.wparks.shared.data.Version
import io.ktor.client.*
import io.ktor.client.request.*

class ParkApi(private val client: HttpClient) {

    suspend fun fetchParksInfo(): ParkInfo {
        return client.get {
            url("$baseUrl/v1/parks.json")
        }
    }

    suspend fun fetchVersion(): Version {
        return client.get {
            url("$baseUrl/v1/version.json")
        }
    }

    companion object {
        private const val baseUrl = "https://winnipeg-parks.web.app"
    }
}