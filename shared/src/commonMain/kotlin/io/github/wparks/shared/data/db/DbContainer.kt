package io.github.wparks.shared.data.db

import io.github.wparks.ParkDatabase

class DbContainer(driverFactory: DriverFactory) {
    private val driver = driverFactory.createDriver()
    val database = ParkDatabase(driver)
}