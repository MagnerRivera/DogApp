package com.example.dogapp.retrofit

import com.example.dogapp.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET(Constants.GET_ALL)
    fun getAllBreeds(): Call<BreedListResponse>

    @GET(Constants.GET_RANDOM)
    fun getBreedImage(@Path("breed") breed: String): Call<DogResponse>
}