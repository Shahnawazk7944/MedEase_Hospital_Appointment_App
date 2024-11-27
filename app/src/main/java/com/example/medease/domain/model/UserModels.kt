package com.example.medease.domain.model

data class UserProfile(
    val name: String,
    val email: String,
    val phone: String,
    val city: String,
    val pinCode: String,
    val profilePicture: String,
)