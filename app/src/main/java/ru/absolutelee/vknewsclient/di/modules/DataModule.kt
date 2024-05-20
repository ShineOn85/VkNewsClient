package ru.absolutelee.vknewsclient.di.modules

import android.content.Context
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.absolutelee.vknewsclient.data.network.ApiService
import ru.absolutelee.vknewsclient.data.repository.NewsFeedRepositoryImpl
import ru.absolutelee.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun provideNewsFeedRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository

    companion object {
        @Provides
        @Singleton
        fun provideApiService(): ApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.vk.com/method/")
                .client(okHttpClient)
                .build()
                .create()
        }

        @Provides
        @Singleton
        fun provideVKPreferencesKeyValueStorage(@ApplicationContext context: Context): VKPreferencesKeyValueStorage {
            return VKPreferencesKeyValueStorage(context)
        }
    }
}