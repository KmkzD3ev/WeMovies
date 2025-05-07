package com.example.wemovies.ui.LoadState

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wemovies.Error.ErrorActivity
import com.example.wemovies.MainActivity
import com.example.wemovies.R
import com.example.wemovies.ViewModels.MovieViewModel
import com.example.wemovies.utils.DialogHelper

class LoadState : AppCompatActivity() {

    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_load_state)




        // Exibe loading e observa os dados
        DialogHelper.showLoadingDialog(this)

        viewModel.movies.observe(this) { movies ->
            DialogHelper.dismissLoadingDialog()

            movies?.let {
                if (it.isNotEmpty()) {
                    Log.d("LoadState", "🎬 Filmes recebidos:\n" + it.joinToString("\n") { m -> "${m.title} - R$ ${m.price}" })
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            } ?: run {
                Log.e("LoadState", "❌ LiveData movies é nulo!")
            }
        }

        viewModel.navigateToError.observe(this) {
            DialogHelper.dismissLoadingDialog()
            startActivity(Intent(this, ErrorActivity::class.java))
            finish()
        }

        // Dispara a busca
        viewModel.fetchMovies(this)
    }



}
