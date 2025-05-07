package hu.bme.nandiii.gamebrowser.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.nandiii.gamebrowser.data.auth.AuthService
import hu.bme.nandiii.gamebrowser.data.auth.FirebaseAuthService
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Singleton
    @Provides
    fun provideAuthService(auth: FirebaseAuth): AuthService =
        FirebaseAuthService(auth)
}












