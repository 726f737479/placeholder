package com.dev.rosty.placeholder.injection

import android.content.Context
import android.util.Log
import com.dev.rosty.placeholder.common.RxSchedulers
import com.dev.rosty.placeholder.common.RxSchedulersImpl
import com.dev.rosty.placeholder.data.DataSource
import com.dev.rosty.placeholder.data.DataSourceImpl
import com.dev.rosty.placeholder.data.local.CacheManager
import com.dev.rosty.placeholder.data.local.CacheManagerImpl
import com.dev.rosty.placeholder.data.remote.PlaceholderService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {

    @Singleton
    @Provides
    fun provideAppContext() = appContext

    @Singleton
    @Provides
    fun provideDataSource(placeholderService: PlaceholderService,
                          cacheManager: CacheManager,
                          rxSchedulers: RxSchedulers): DataSource {

        return DataSourceImpl(cacheManager, placeholderService, rxSchedulers)
    }

    @Singleton
    @Provides
    fun providePlaceholderService(): PlaceholderService {

        val loggingInterceptor = HttpLoggingInterceptor( { Log.d("Placeholder Api", it) })
                .setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

        val retrofit = Retrofit.Builder().client(okHttpClient)
                .baseUrl(PlaceholderService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(PlaceholderService::class.java)
    }

    @Singleton
    @Provides
    fun provideCacheManager(): CacheManager {
        return CacheManagerImpl()
    }

    @Singleton
    @Provides
    fun provideRxSchedulers(): RxSchedulers {
        return RxSchedulersImpl()
    }
}