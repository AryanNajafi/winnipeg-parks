package io.github.wparks.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParkInfo(val parks: List<Park>,
                    val assets: List<Asset>,
                    @SerialName("asset_types") val assetTypes: List<AssetType>,
                    val version: Int)

@Serializable
data class Park(val id: Int,
                val title: String,
                val address: String,
                val district: String,
                val neighbourhood: String,
                @SerialName("area_total") val totalArea: Double,
                @SerialName("area_land") val landArea: Double,
                @SerialName("area_water") val waterArea: Double,
                @SerialName("lat") val latitude: Double,
                @SerialName("lng") val longitude: Double,)

@Serializable
data class Asset(val id: Int,
                 @SerialName("type_id") val typeId: Int,
                 @SerialName("park_id") val parkId: Int,
                 val subtype: String?,
                 val size: String?,
                 @SerialName("lat") val latitude: Double,
                 @SerialName("lng") val longitude: Double,)

@Serializable
data class AssetType(val id: Int, val title: String)
