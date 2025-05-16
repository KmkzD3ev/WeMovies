package com.example.wemovies

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.wemovies.Models.Movie
import com.example.wemovies.Repository.MovieRepository
import com.example.wemovies.ViewModels.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var context: Context

    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    @Mock
    private lateinit var networkCapabilities: NetworkCapabilities

    private lateinit var viewModel: MovieViewModel

    @Mock
    private lateinit var moviesObserver: Observer<List<Movie>>

    @Mock
    private lateinit var hasMoviesObserver: Observer<Boolean>

    @Mock
    private lateinit var navigateToErrorObserver: Observer<Unit>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Criação do mock de context e retorno do connectivityManager simulado
        context = mock()
        whenever(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)

        // Mocks da cadeia de chamadas de conectividade
        whenever(connectivityManager.activeNetwork).thenReturn(mock())
        whenever(connectivityManager.getNetworkCapabilities(any())).thenReturn(networkCapabilities)
        whenever(networkCapabilities.hasTransport(any())).thenReturn(true)

        viewModel = MovieViewModel(repository)

        viewModel.movies.observeForever(moviesObserver)
        viewModel.hasMovies.observeForever(hasMoviesObserver)
        viewModel.navigateToError.observeForever(navigateToErrorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.movies.removeObserver(moviesObserver)
        viewModel.hasMovies.removeObserver(hasMoviesObserver)
        viewModel.navigateToError.removeObserver(navigateToErrorObserver)
    }

    @Test
    fun `fetchMovies should update LiveData when successful`() = runTest {
        val mockMovies = listOf(
            Movie(
                id = 1,
                title = "Test Movie",
                image = "image_url",
                price = 9.99,
            )
        )

        whenever(repository.getMovies()).thenReturn(mockMovies)

        val isLoadingObserver = mock<Observer<Boolean>>()
        viewModel.isLoading.observeForever(isLoadingObserver)

        viewModel.fetchMovies(context)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(isLoadingObserver).onChanged(true)
        verify(moviesObserver).onChanged(mockMovies)
        verify(hasMoviesObserver).onChanged(true)
        verify(isLoadingObserver).onChanged(false)
        verify(navigateToErrorObserver, never()).onChanged(any())

        viewModel.isLoading.removeObserver(isLoadingObserver)
    }

    @Test
    fun `fetchMovies should handle no internet connection`() = runTest {
        whenever(connectivityManager.getNetworkCapabilities(any())).thenReturn(null)

        viewModel.fetchMovies(context)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(navigateToErrorObserver).onChanged(any())
        verify(mock<Observer<Boolean>>(), never()).onChanged(any())
    }

    @Test
    fun `fetchMovies should handle repository error`() = runTest {
        whenever(repository.getMovies()).thenThrow(RuntimeException("API Error"))
        whenever(connectivityManager.getNetworkCapabilities(any())).thenReturn(networkCapabilities)
        whenever(networkCapabilities.hasTransport(any())).thenReturn(true)

        val isLoadingObserver = mock<Observer<Boolean>>()
        viewModel.isLoading.observeForever(isLoadingObserver)

        viewModel.fetchMovies(context)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(isLoadingObserver).onChanged(true)
        verify(navigateToErrorObserver).onChanged(any())
        verify(hasMoviesObserver).onChanged(false)
        verify(isLoadingObserver).onChanged(false)
        verify(moviesObserver, never()).onChanged(any())

        viewModel.isLoading.removeObserver(isLoadingObserver)
    }
}
