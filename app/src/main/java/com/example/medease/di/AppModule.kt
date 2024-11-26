package com.example.medease.di

import com.example.medease.data.firebase.FirebaseWrapper
import com.example.medease.data.util.AuthValidator
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