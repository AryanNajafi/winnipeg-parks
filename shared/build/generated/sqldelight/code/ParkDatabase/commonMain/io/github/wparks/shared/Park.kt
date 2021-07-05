package io.github.wparks.shared

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Park(
  public val id: Long,
  public val title: String,
  public val address: String,
  public val district: String,
  public val neighbourhood: String,
  public val area: Double?,
  public val water: Double?,
  public val land: Double?,
  public val latitude: Double?,
  public val longitude: Double?
) {
  public override fun toString(): String = """
  |Park [
  |  id: $id
  |  title: $title
  |  address: $address
  |  district: $district
  |  neighbourhood: $neighbourhood
  |  area: $area
  |  water: $water
  |  land: $land
  |  latitude: $latitude
  |  longitude: $longitude
  |]
  """.trimMargin()
}
