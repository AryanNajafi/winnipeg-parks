package io.github.wparks.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Version(@SerialName("latest_version") val latestVersion: Int)