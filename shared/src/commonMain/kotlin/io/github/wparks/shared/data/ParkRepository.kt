package io.github.wparks.shared.data

import com.russhwolf.settings.Settings
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.github.wparks.shared.Asset
import io.github.wparks.shared.Park
import io.github.wparks.shared.ParkQueries
import io.github.wparks.shared.data.remote.ParkApi
import kotlinx.coroutines.flow.Flow

class ParkRepository(private val api: ParkApi,
                     private val parkQueries: ParkQueries,
                     private val settings: Settings) {

    private suspend fun fetchParksInfo() {
        val parksInfo = api.fetchParksInfo()
        parksInfo.parks.map { park ->
            Park(park.id.toLong(), park.title, park.address, park.district, park.neighbourhood,
                park.totalArea, park.waterArea, park.landArea, park.latitude, park.longitude)
        }.forEach { parkQueries.insertParkObject(it) }

        parksInfo.assets.map { asset ->
            val title = parksInfo.assetTypes.first { it.id == asset.typeId }.title
            Asset(asset.id.toLong(), title, asset.typeId.toLong(), asset.parkId.toLong(),
                asset.subtype, asset.size, asset.latitude, asset.longitude)
        }.forEach { parkQueries.insertAssetObject(it) }

        settings.putInt(SETTINGS_KEY_CACHE_VERSION, parksInfo.version)
    }

    fun loadParkInfo(id: Long): Park {
        return parkQueries.selectParkById(id).executeAsOne()
    }

    fun loadParkAssets(parkId: Long): Flow<List<Asset>> {
        return parkQueries.selectParkAssets(parkId).asFlow().mapToList()
    }

    fun loadParks(page: Long, filters: List<Long> = emptyList()): Flow<List<Park>> {
        return (if (filters.isEmpty()) parkQueries.selectParks(PAGE_SIZE, page * PAGE_SIZE)
                else parkQueries.selectFilteredParks(filters, PAGE_SIZE, page * PAGE_SIZE))
            .asFlow().mapToList()
    }

    fun loadAssetTypes(): Flow<List<AssetType>> {
        return parkQueries.selectAssetTypes(mapper = { typeId, title ->
            AssetType(typeId.toInt(), title) }).asFlow().mapToList()
    }

    private suspend fun shouldUpdateParksCache(): Boolean {
        val parksCount = parkQueries.selectParksCount().executeAsOne()
        val cacheVersion = settings.getInt(SETTINGS_KEY_CACHE_VERSION, 0)
        if (parksCount == 0L || cacheVersion == 0) {
            return true
        }
        val dbVersion = api.fetchVersion()
        return dbVersion.latestVersion > cacheVersion
    }

    suspend fun tryUpdateParksCache() {
        if (shouldUpdateParksCache()) fetchParksInfo()
    }

    companion object {
        private const val SETTINGS_KEY_CACHE_VERSION = "cache_version"
        private const val PAGE_SIZE = 20L
    }
}