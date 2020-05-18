package com.oliver.gymondo.database.modules

import android.content.Context
import com.oliver.gymondo.app.AppModule
import com.oliver.gymondo.database.GymondoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class DatabaseModule {

    @Provides
    @Singleton
    fun gymondoDatabase(context: Context): GymondoDatabase {
        return GymondoDatabase.getInstance(context)
    }
}