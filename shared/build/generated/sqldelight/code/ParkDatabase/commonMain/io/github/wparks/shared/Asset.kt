package io.github.wparks.shared

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Asset(
  public val id: Long,
  public val title: String,
  public val typeId: Long,
  public val parkId: Long,
  public val subtype: String?,
  public val size: String?,
  public val latitude: Double?,
  public val longitude: Double?
) {
  public override fun toString(): String = """
  |Asset [
  |  id: $id
  |  title: $title
  |  typeId: $typeId
  |  parkId: $parkId
  |  subtype: $subtype
  |  size: $size
  |  latitude: $latitude
  |  longitude: $longitude
  |]
  """.trimMargin()
}
