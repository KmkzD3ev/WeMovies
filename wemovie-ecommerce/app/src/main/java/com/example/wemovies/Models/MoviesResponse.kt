package com.example.wemovies.Models


import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("products")
    val movies: List<Movie>
)
