package io.github.wparks.androidApp.ui.park

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.wparks.androidApp.ui.home.HomeActivity

class ParkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val parkId = intent.getLongExtra(HomeActivity.INTENT_KEY_PARK_ID, -1)
    }

}