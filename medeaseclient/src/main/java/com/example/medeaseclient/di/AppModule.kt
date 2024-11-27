package com.example.medeaseclient.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.medeaseclient.data.firebase.FirebaseWrapper
import com.example.medeaseclient.data.repository.ClientAuthRepository
import com.example.medeaseclient.data.repository.ClientAuthRepositoryImpl
import com.example.medeaseclient.data.util.AuthValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseWrapper(): FirebaseWrapper {
        return FirebaseWrapper()
    }

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.filesDir.resolve("clientAuth_preferences.preferences_pb") }
        )

    @Provides
    fun provideClientAuthRepository(
        firebaseWrapper: FirebaseWrapper,
        dataStore: DataStore<Preferences>
    ): ClientAuthRepository {
        return ClientAuthRepositoryImpl(firebaseWrapper, dataStore)
    }

    @Provides
    fun provideAuthValidator(): AuthValidator {
        return AuthValidator()
    }
}