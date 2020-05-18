package com.oliver.gymondo.network.modules

import android.content.Context
import com.oliver.gymondo.app.AppModule
import com.oliver.gymondo.network.connectivity.ConnectivityInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class OkHttpClientModule {

    companion object {
        private const val HTTP_CACHE = "HttpCache"
        private const val CONNECTION_TIMEOUT = 100L
        private const val READ_TIMEOUT = 100L
    }

    @Provides
    @Singleton
    fun okHttpClient(
        cache: Cache,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(ConnectivityInterceptor(context))
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun cache(cacheFile: File): Cache {
        return Cache(cacheFile, (10 * 1000 * 1000).toLong())
    }

    @Provides
    @Singleton
    fun cacheFile(context: Context): File {
        val file = File(context.cacheDir, HTTP_CACHE)

        file.mkdirs()
        return file
    }

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor { message: String -> Timber.d(message) }

        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
}
