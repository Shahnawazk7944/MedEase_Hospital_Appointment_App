package com.example.medeaseclient.domain.model

data class ClientProfile(
    val hospitalName: String,
    val hospitalEmail: String,
    val hospitalPhone: String,
    val hospitalCity: String,
    val hospitalPinCode: String,
    val profilePicture: String,
)