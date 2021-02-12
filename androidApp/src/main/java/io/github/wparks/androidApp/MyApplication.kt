package io.github.wparks.androidApp

import android.app.Application
import io.github.wparks.shared.AppContainer
import io.github.wparks.shared.data.db.DbContainer
import io.github.wparks.shared.data.db.DriverFactory

class MyApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        val dbContainer = DbContainer(DriverFactory(this))
        appContainer = AppContainer(dbContainer)
    }
}