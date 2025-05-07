package com.example.wemovies.ViewModels

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wemovies.Error.ErrorActivity
import com.example.wemovies.Models.Movie
import com.example.wemovies.NetWork.RetrofitInstance
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    // LiveData com a lista de filmes recebidos da API
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    // LiveData para informar se existem filmes disponíveis
    private val _hasMovies = MutableLiveData<Boolean>()
    val hasMovies: LiveData<Boolean> = _hasMovies

    // LiveData para controle de carregamento
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // leveData para Eventos de  Error
    private val _navigateToError = MutableLiveData<Unit>()
    val navigateToError: LiveData<Unit> = _navigateToError


    /**
     * Função responsável por fazer a chamada para a API
     * e atualizar os dados das LiveData conforme a resposta.
     */
    fun fetchMovies(context: Context) {

        if (!isInternetAvailable(context)) {
            Log.e("MovieViewModel", "Sem conexão com a internet.")
            _navigateToError.value = Unit


            return
        }
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMovies()
                if (response.isSuccessful && response.body() != null) {
                    val movieList = response.body()!!.movies
                    _movies.value = movieList
                    _hasMovies.value = movieList.isNotEmpty()
                }

                else {
                    _hasMovies.value = false
                }
            } catch (e: Exception) {
                _hasMovies.value = false
                Log.e("MovieViewModel", "Erro ao buscar filmes", e)
                _navigateToError.value = Unit

            } finally {
                _isLoading.value = false
            }
        }
    }


    //Monitoramento Rede/Internet
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }






}
