package com.example.medeaseclient.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientProfile(
    val hospitalName: String? = null,
    val hospitalEmail: String? = null,
    val hospitalPhone: String? = null,
    val hospitalCity: String? = null,
    val hospitalPinCode: String? = null,
)