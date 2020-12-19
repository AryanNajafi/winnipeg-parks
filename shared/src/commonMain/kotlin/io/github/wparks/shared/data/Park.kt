package io.github.wparks.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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