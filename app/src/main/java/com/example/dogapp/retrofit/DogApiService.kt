package com.example.dogapp.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("api/breeds/list/all")
    fun getAllBreeds(): Call<BreedListResponse>

    @GET("api/breed/{breed}/images/random")
    fun getBreedImage(@Path("breed") breed: String): Call<DogResponse>
}