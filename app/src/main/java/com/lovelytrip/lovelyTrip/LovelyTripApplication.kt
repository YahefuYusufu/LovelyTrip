

package com.lovelytrip.lovelyTrip

import android.app.Application
import com.lovelytrip.lovelyTrip.data.AppContainer
import com.lovelytrip.lovelyTrip.data.AppDataContainer

class LovelyTripApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
