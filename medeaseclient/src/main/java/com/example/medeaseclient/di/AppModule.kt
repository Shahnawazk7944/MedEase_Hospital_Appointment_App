package com.example.medeaseclient.di

import com.example.medeaseclient.data.firebase.FirebaseWrapper
import com.example.medeaseclient.data.util.AuthValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseWrapper(): FirebaseWrapper {
        return FirebaseWrapper()
    }

    @Provides
    fun provideAuthValidator(): AuthValidator {
        return AuthValidator()
    }
}