package io.github.wparks.shared

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.Unit
import kotlin.collections.Collection

public interface ParkQueries : Transacter {
  public fun selectParksCount(): Query<Long>

  public fun <T : Any> selectParkById(id: Long, mapper: (
    id: Long,
    title: String,
    address: String,
    district: String,
    neighbourhood: String,
    area: Double?,
    water: Double?,
    land: Double?,
    latitude: Double,
    longitude: Double
  ) -> T): Query<T>

  public fun selectParkById(id: Long): Query<Park>

  public fun <T : Any> selectParks(
    limit: Long,
    offset: Long,
    mapper: (
      id: Long,
      title: String,
      address: String,
      district: String,
      neighbourhood: String,
      area: Double?,
      water: Double?,
      land: Double?,
      latitude: Double,
      longitude: Double
    ) -> T
  ): Query<T>

  public fun selectParks(limit: Long, offset: Long): Query<Park>

  public fun <T : Any> selectFilteredParks(
    typeId: Collection<Long>,
    limit: Long,
    offset: Long,
    mapper: (
      id: Long,
      title: String,
      address: String,
      district: String,
      neighbourhood: String,
      area: Double?,
      water: Double?,
      land: Double?,
      latitude: Double,
      longitude: Double
    ) -> T
  ): Query<T>

  public fun selectFilteredParks(
    typeId: Collection<Long>,
    limit: Long,
    offset: Long
  ): Query<Park>

  public fun <T : Any> selectParkAssets(parkId: Long, mapper: (
    id: Long,
    title: String,
    typeId: Long,
    parkId: Long,
    subtype: String?,
    size: String?,
    latitude: Double,
    longitude: Double
  ) -> T): Query<T>

  public fun selectParkAssets(parkId: Long): Query<Asset>

  public fun <T : Any> selectAssetTypes(mapper: (typeId: Long, title: String) -> T): Query<T>

  public fun selectAssetTypes(): Query<SelectAssetTypes>

  public fun insertParkObject(park: Park): Unit

  public fun insertAssetObject(asset: Asset): Unit
}
