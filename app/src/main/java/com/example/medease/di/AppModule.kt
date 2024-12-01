package com.example.medease.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.medease.data.firebase.FirebaseWrapper
import com.example.medease.data.repository.auth.UserAuthRepository
import com.example.medease.data.repository.auth.UserAuthRepositoryImpl
import com.example.medease.data.repository.auth.UserDataStoreRepository
import com.example.medease.data.repository.auth.UserDataStoreRepositoryImpl
import com.example.medease.data.repository.home.UserHomeRepository
import com.example.medease.data.repository.home.UserHomeRepositoryImpl
import com.example.medease.data.util.AuthValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseWrapper(): FirebaseWrapper {
        return FirebaseWrapper()
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.filesDir.resolve("userAuth_preferences.preferences_pb") }
        )

    @Singleton
    @Provides
    fun provideClientAuthRepository(
        firebaseWrapper: FirebaseWrapper,
        dataStore: DataStore<Preferences>
    ): UserAuthRepository {
        return UserAuthRepositoryImpl(firebaseWrapper, dataStore)
    }

    @Singleton
    @Provides
    fun provideUserHomeRepository(
        firebaseWrapper: FirebaseWrapper,
        dataStore: DataStore<Preferences>
    ): UserHomeRepository {
        return UserHomeRepositoryImpl(firebaseWrapper, dataStore)
    }

    @Singleton
    @Provides
    fun provideClientDataStoreRepository(
        dataStore: DataStore<Preferences>
    ): UserDataStoreRepository {
        return UserDataStoreRepositoryImpl(dataStore)
    }

    @Singleton
    @Provides
    fun provideAuthValidator(): AuthValidator {
        return AuthValidator()
    }
}