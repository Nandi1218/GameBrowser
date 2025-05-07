package hu.bme.nandiii.gamebrowser.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.nandiii.gamebrowser.domain.usecase.IsEmailValidUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmailUsecaseModule {
    @Singleton
    @Provides
    fun provideEmailUseCases(): IsEmailValidUseCase =
        IsEmailValidUseCase()
}