package io.github.wparks.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Asset(val id: Int,
                 val typeId: Int,
                 val parkId: Int,
                 val subType: String,
                 val size: String,
                 @SerialName("lat") val latitude: Double,
                 @SerialName("lng") val longitude: Double,)