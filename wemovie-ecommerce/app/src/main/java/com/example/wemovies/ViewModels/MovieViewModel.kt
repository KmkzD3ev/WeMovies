package com.example.wemovies.ViewModels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wemovies.Models.Movie
import com.example.wemovies.Repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsável por gerenciar os dados relacionados aos filmes,
 * obtendo informações a partir do repositório [MovieRepository].
 *
 * Utiliza LiveData para expor os dados e controlar estados de carregamento
 * e erro. Compatível com testes unitários usando Mockito e JUnit.
 *
 *
 * Injeçao de dependecia usando  Hilt (Distribuiçao de dados)
 */
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository

) : ViewModel() {

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
     *
     * @param context Contexto necessário para verificar conectividade.
     *
     *
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
                val response = repository.getMovies()
                _movies.value = response
                _hasMovies.value = response.isNotEmpty()

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
