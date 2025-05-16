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
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
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

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    @Mock
    private lateinit var networkCapabilities: NetworkCapabilities

    private lateinit var viewModel: MovieViewModel

    // Observers
    private lateinit var moviesObserver: Observer<List<Movie>>
    private lateinit var hasMoviesObserver: Observer<Boolean>
    private lateinit var isLoadingObserver: Observer<Boolean>
    private lateinit var navigateToErrorObserver: Observer<Unit>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Configurar mocks
        whenever(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        whenever(connectivityManager.activeNetwork).thenReturn(null)
        whenever(connectivityManager.getNetworkCapabilities(any())).thenReturn(networkCapabilities)

        viewModel = MovieViewModel(repository)

        // Inicializar observers
        moviesObserver = mock(Observer::class.java) as Observer<List<Movie>>
        hasMoviesObserver = mock(Observer::class.java) as Observer<Boolean>
        isLoadingObserver = mock(Observer::class.java) as Observer<Boolean>
        navigateToErrorObserver = mock(Observer::class.java) as Observer<Unit>

        // Observar LiveData
        viewModel.movies.observeForever(moviesObserver)
        viewModel.hasMovies.observeForever(hasMoviesObserver)
        viewModel.isLoading.observeForever(isLoadingObserver)
        viewModel.navigateToError.observeForever(navigateToErrorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.movies.removeObserver(moviesObserver)
        viewModel.hasMovies.removeObserver(hasMoviesObserver)
        viewModel.isLoading.removeObserver(isLoadingObserver)
        viewModel.navigateToError.removeObserver(navigateToErrorObserver)
    }

    @Test
    fun `fetchMovies should update LiveData when successful`() = runTest {
        // Arrange
        val mockMovies = listOf(
            Movie(
                id = 1,
                title = "Test Movie",
                image = "image_url",
                price = 9.99,

            )
        )

        whenever(repository.getMovies()).thenReturn(mockMovies)
        whenever(connectivityManager.getNetworkCapabilities(any())).thenReturn(networkCapabilities)
        whenever(networkCapabilities.hasTransport(any())).thenReturn(true)

        // Act
        viewModel.fetchMovies(context)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(isLoadingObserver).onChanged(true)
        verify(moviesObserver).onChanged(mockMovies)
        verify(hasMoviesObserver).onChanged(true)
        verify(isLoadingObserver).onChanged(false)
        verify(navigateToErrorObserver, never()).onChanged(any())
    }

    @Test
    fun `fetchMovies should handle no internet connection`() = runTest {
        // Arrange
        whenever(connectivityManager.getNetworkCapabilities(any())).thenReturn(null)

        // Act
        viewModel.fetchMovies(context)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(navigateToErrorObserver).onChanged(any())
        verify(isLoadingObserver, never()).onChanged(true)
    }

    @Test
    fun `fetchMovies should handle repository error`() = runTest {
        // Arrange
        whenever(repository.getMovies()).thenThrow(RuntimeException("API Error"))
        whenever(connectivityManager.getNetworkCapabilities(any())).thenReturn(networkCapabilities)
        whenever(networkCapabilities.hasTransport(any())).thenReturn(true)

        // Act
        viewModel.fetchMovies(context)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(isLoadingObserver).onChanged(true)
        verify(navigateToErrorObserver).onChanged(any())
        verify(hasMoviesObserver).onChanged(false)
    }
}