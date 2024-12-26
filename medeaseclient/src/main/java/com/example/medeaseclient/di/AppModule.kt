package com.example.medeaseclient.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.medeaseclient.data.firebase.FirebaseWrapper
import com.example.medeaseclient.data.repository.auth.ClientAuthRepository
import com.example.medeaseclient.data.repository.auth.ClientAuthRepositoryImpl
import com.example.medeaseclient.data.repository.auth.ClientDataStoreRepository
import com.example.medeaseclient.data.repository.auth.ClientDataStoreRepositoryImpl
import com.example.medeaseclient.data.repository.home.ClientHomeRepository
import com.example.medeaseclient.data.repository.home.ClientHomeRepositoryImpl
import com.example.medeaseclient.data.util.Validator
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
            produceFile = { context.filesDir.resolve("clientAuth_preferences.preferences_pb") }
        )

    @Singleton
    @Provides
    fun provideClientAuthRepository(
        firebaseWrapper: FirebaseWrapper,
        dataStore: DataStore<Preferences>
    ): ClientAuthRepository {
        return ClientAuthRepositoryImpl(firebaseWrapper, dataStore)
    }

    @Singleton
    @Provides
    fun provideClientHomeRepository(
        firebaseWrapper: FirebaseWrapper,
        dataStore: DataStore<Preferences>
    ): ClientHomeRepository {
        return ClientHomeRepositoryImpl(firebaseWrapper, dataStore)
    }
    @Singleton
    @Provides
    fun provideClientDataStoreRepository(
        dataStore: DataStore<Preferences>
    ): ClientDataStoreRepository {
        return ClientDataStoreRepositoryImpl(dataStore)
    }

    @Singleton
    @Provides
    fun provideAuthValidator(): Validator {
        return Validator()
    }
}