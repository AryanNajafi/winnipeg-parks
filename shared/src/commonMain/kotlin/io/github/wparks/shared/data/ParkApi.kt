package io.github.wparks.shared.data

import io.ktor.client.*
import io.ktor.client.request.*

class ParkApi(private val client: HttpClient) {

    suspend fun fetchParks(): List<Park> {
        return client.get {
            url("$baseUrl/parks.json")
        }
    }

    suspend fun fetchParkAssets(): List<Asset> {
        return client.get {
            url("$baseUrl/assets.json")
        }
    }

    companion object {
        private const val baseUrl = "https://winnipeg-parks.web.app"
    }
}