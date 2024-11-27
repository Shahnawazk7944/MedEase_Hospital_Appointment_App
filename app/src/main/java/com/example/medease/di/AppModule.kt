package com.example.medease.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.medease.data.firebase.FirebaseWrapper
import com.example.medease.data.repository.UserAuthRepository
import com.example.medease.data.repository.UserAuthRepositoryImpl
import com.example.medease.data.util.AuthValidator
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
            produceFile = { context.filesDir.resolve("userAuth_preferences.preferences_pb") }
        )

    @Provides
    fun provideClientAuthRepository(
        firebaseWrapper: FirebaseWrapper,
        dataStore: DataStore<Preferences>
    ): UserAuthRepository {
        return UserAuthRepositoryImpl(firebaseWrapper, dataStore)
    }

    @Provides
    fun provideAuthValidator(): AuthValidator {
        return AuthValidator()
    }
}