package com.example.wemovies.Repository

import com.example.wemovies.Models.Movie

/**
 * Contrato para acesso a dados de filmes.
 *
 * Essa interface define o comportamento esperado de qualquer classe que deseje
 * prover uma fonte de dados de [Movie]s — seja a partir de uma API, banco de dados
 * local ou qualquer outro meio.
 *
 * Ao utilizar essa abstração, é possível injetar implementações concretas via
 * frameworks de injeção de dependência (como Hilt), o que facilita a testabilidade
 * e promove o princípio de inversão de dependência (D do SOLID).
 */
interface MovieRepository {

    /**
     * Obtém uma lista de filmes.
     *
     * A operação é `suspend` pois envolve chamadas assíncronas, normalmente à
     * rede ou ao banco de dados.
     *
     * @return Lista de filmes disponíveis.
     * @throws Exception se ocorrer qualquer erro durante a obtenção dos dados.
     */
    suspend fun getMovies(): List<Movie>
}
