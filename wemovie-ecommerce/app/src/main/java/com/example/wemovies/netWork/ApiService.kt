package com.example.wemovies.netWork

import com.example.wemovies.Models.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("movies")
    suspend fun getMovies(): Response<MoviesResponse>

    companion object {
        const val BASE_URL = "https://wefit-movies.vercel.app/api/"
    }
}
