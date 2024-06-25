package com.example.dogapp.retrofit

import com.google.gson.annotations.SerializedName

data class DogResponse(
    @SerializedName("message") val imageUrl: String,
    @SerializedName("status") val status: String
)

data class BreedListResponse(
    @SerializedName("message") val breeds: Map<String, List<String>>,
    @SerializedName("status") val status: String
)

data class Breed(
    val name: String,
    val imageUrl: String
)