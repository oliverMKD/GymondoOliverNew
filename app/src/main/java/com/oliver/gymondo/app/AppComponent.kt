package com.oliver.gymondo.app

import com.oliver.gymondo.database.GymondoDatabase
import com.oliver.gymondo.database.modules.DatabaseModule
import com.oliver.gymondo.exercise.repositories.GymondoRepository
import com.oliver.gymondo.network.GymondoApiService
import com.oliver.gymondo.network.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface AppComponent {

    fun gymondoApiService(): GymondoApiService

    fun gymondoDatabase(): GymondoDatabase

    fun inject(repository: GymondoRepository)
}