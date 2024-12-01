package com.example.medeaseclient.data.repository.auth

import arrow.core.Either

interface ClientDataStoreRepository {
    suspend fun getRememberMe(): Either<GetRememberMeAndIDPreferencesFailure, Boolean>
    suspend fun getClientId(): Either<GetRememberMeAndIDPreferencesFailure, String>
}

sealed class GetRememberMeAndIDPreferencesFailure {
    data object UnknownError : GetRememberMeAndIDPreferencesFailure()
}
