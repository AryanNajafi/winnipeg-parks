package io.github.wparks.shared

import kotlin.Long
import kotlin.String

public data class SelectAssetTypes(
  public val typeId: Long,
  public val title: String
) {
  public override fun toString(): String = """
  |SelectAssetTypes [
  |  typeId: $typeId
  |  title: $title
  |]
  """.trimMargin()
}
