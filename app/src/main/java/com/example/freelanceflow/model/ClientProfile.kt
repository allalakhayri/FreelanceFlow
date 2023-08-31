package com.example.freelanceflow.model

import android.net.Uri

data class ClientProfile(
    val profileImageUri: Uri?,
    val name: String,
    val mobileNumber: String,
    val streetAddress: String,
    val city: String,
    val language: String,
    val gender: String
)
{
    // Add a no-argument constructor
    constructor() : this(null, "", "", "", "", "", "")
}