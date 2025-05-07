package hu.bme.nandiii.gamebrowser.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.nandiii.gamebrowser.data.network.GameApi
import hu.bme.nandiii.gamebrowser.data.network.retrofit.IGameRepository
import hu.bme.nandiii.gamebrowser.data.network.retrofit.RetrofitGameRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameRepositoryModule {

    @Provides
    @Singleton
    fun provideGameRepository(gameApi: GameApi, moshi: Moshi, retrofit: Retrofit): IGameRepository =
        RetrofitGameRepository(moshi, retrofit, gameApi)

}