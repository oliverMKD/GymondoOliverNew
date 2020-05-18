package com.oliver.gymondo.app

import android.app.Application
import com.oliver.gymondo.database.modules.DatabaseModule
import com.oliver.gymondo.network.modules.NetworkModule
import com.oliver.gymondo.network.modules.OkHttpClientModule

class GymondoApp : Application() {

    companion object {
        lateinit var instance: GymondoApp
    }

    private lateinit var appComponent: AppComponent

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .databaseModule(DatabaseModule())
            .networkModule(NetworkModule())
            .okHttpClientModule(OkHttpClientModule())
            .build()
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }
}