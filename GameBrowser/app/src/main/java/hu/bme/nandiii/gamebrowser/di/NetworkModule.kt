package hu.bme.nandiii.gamebrowser.di

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.nandiii.gamebrowser.data.network.GameApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoshiModule {


    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        Log.w("NandiDebug", "Injecting Moshi")
        return Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    }


}

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit {
        Log.w("NandiDebug", "Injecting Retrofit")
        return Retrofit.Builder()
            .baseUrl("https://www.cheapshark.com")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object GameApiModule {
    @Provides
    @Singleton
    fun provideGameApi(retrofit: Retrofit): GameApi {
        Log.w("NandiDebug", "Injecting GameApi")
        return retrofit.create(GameApi::class.java) as GameApi
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FireStoreModule {
    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}

