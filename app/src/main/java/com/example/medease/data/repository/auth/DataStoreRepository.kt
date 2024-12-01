package com.example.medease.data.repository.auth

import arrow.core.Either

interface UserDataStoreRepository {
    suspend fun getRememberMe(): Either<GetRememberMeAndIDPreferencesFailure, Boolean>
    suspend fun getUserId(): Either<GetRememberMeAndIDPreferencesFailure, String>
}
sealed class GetRememberMeAndIDPreferencesFailure {
    data object UnknownError : GetRememberMeAndIDPreferencesFailure()
}
