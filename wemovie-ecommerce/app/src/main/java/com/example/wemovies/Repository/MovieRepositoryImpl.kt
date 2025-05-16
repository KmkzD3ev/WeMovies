package com.example.wemovies.Repository

import com.example.wemovies.Models.Movie
import com.example.wemovies.netWork.RetrofitInstance
import javax.inject.Inject

/**
 * Implementação concreta da interface [MovieRepository].
 *
 * Essa classe é responsável por buscar os dados de filmes a partir de uma chamada
 * à API, utilizando Retrofit. Ela deve ser utilizada com injeção de dependência
 * (Com Hilt) para facilitar testes e modularização do código.
 *
 * @throws Exception caso a requisição falhe ou o corpo da resposta seja nulo.
 */
class MovieRepositoryImpl @Inject constructor() : MovieRepository {

    /**
     * Realiza uma chamada à API para obter uma lista de filmes.
     *
     * @return Uma lista de [Movie] se a requisição for bem-sucedida.
     * @throws Exception se a resposta não for bem-sucedida ou o corpo estiver nulo.
     */
    override suspend fun getMovies(): List<Movie> {
        val response = RetrofitInstance.api.getMovies()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.movies
        }
        throw Exception("Erro ao buscar filmes.")
    }
}
