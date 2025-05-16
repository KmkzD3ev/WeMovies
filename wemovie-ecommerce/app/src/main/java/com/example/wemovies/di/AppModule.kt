package com.example.wemovies.di


import com.example.wemovies.Repository.MovieRepository
import com.example.wemovies.Repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de injeção de dependência do Hilt para fornecer implementações
 * relacionadas a repositórios e serviços do domínio da aplicação.
 *
 * Este módulo é instalado no [SingletonComponent], o que significa que
 * as dependências fornecidas aqui existirão enquanto a aplicação estiver viva.
 *
 * A principal função deste módulo é informar ao Hilt como criar instâncias de
 * interfaces. Aqui, especificamente, associamos a interface [MovieRepository]
 * à sua implementação concreta [MovieRepositoryImpl].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Liga a interface [MovieRepository] à sua implementação [MovieRepositoryImpl].
     *
     * Isso permite que o Hilt injete [MovieRepository] em qualquer lugar do app,
     * fornecendo automaticamente uma instância de [MovieRepositoryImpl].
     *
     * @return Uma instância de [MovieRepositoryImpl] sempre que [MovieRepository] for requisitado.
     */
    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository
}