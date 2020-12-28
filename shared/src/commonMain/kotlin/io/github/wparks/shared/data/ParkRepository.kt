package io.github.wparks.shared.data

import com.russhwolf.settings.Settings
import io.github.wparks.shared.Park
import io.github.wparks.shared.Asset
import io.github.wparks.shared.AssetType
import io.github.wparks.shared.ParkQueries
import io.github.wparks.shared.data.remote.ParkApi

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
            Asset(asset.id.toLong(), asset.parkId.toLong(), asset.typeId.toLong(), asset.subtype,
                asset.size, asset.latitude, asset.longitude)
        }.forEach { parkQueries.insertAssetObject(it) }

        parksInfo.assetTypes
            .map { type ->  AssetType(type.id.toLong(), type.title)}
            .forEach { parkQueries.insertAssetTypeObject(it) }

        settings.putInt(SETTINGS_KEY_CACHE_VERSION, parksInfo.version)
    }

    fun getParkInfo(id: Long): Park {
        return parkQueries.selectParkById(id).executeAsOne()
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

    suspend fun tryUpdateRecentParksCache() {
        if (shouldUpdateParksCache()) fetchParksInfo()
    }

    companion object {
        private const val SETTINGS_KEY_CACHE_VERSION = "cache_version"
    }
}