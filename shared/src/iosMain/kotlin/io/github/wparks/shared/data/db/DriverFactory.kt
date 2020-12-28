package io.github.wparks.shared.data.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.wparks.ParkDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(ParkDatabase.Schema, "park_database.db")
    }
}