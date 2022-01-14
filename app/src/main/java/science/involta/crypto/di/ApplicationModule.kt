package science.involta.crypto.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import science.involta.crypto.api.ApiInterface
import science.involta.crypto.data.local.database.CoinsDatabase
import science.involta.crypto.data.local.preferences.PreferenceStorage
import science.involta.crypto.data.local.preferences.SharedPreferencesStorage
import science.involta.crypto.utils.Constants
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Класс предоставляющий зависимости
 */
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    /**
     * Функция предоставляющая зависимость от OkHttp
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url()
                .newBuilder()
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Функция предоставляющая зависимость от Retrofit
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    /**
     * Функция предоставляющая зависимость от ApiInterface
     */
    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    /**
     * Функция предоставляющая зависимость от CoinsDatabase
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CoinsDatabase {
        return CoinsDatabase.buildDatabase(context)
    }

    /**
     * Функция предоставляющая зависимость от PreferenceStorage
     */
    @Provides
    @Singleton
    fun provideSharedPreferencesStorage(@ApplicationContext context: Context): PreferenceStorage = SharedPreferencesStorage(context)
}