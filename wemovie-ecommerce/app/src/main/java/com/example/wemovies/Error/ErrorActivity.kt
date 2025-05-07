package com.example.wemovies.Error

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wemovies.MainActivity
import com.example.wemovies.R
import com.example.wemovies.ui.LoadState.LoadState

/**
 * Activity com Tratamento de Erros Condicionada
 */

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_error)



        val retryButton = findViewById<Button>(R.id.retry_button)
        val source = intent.getStringExtra("source")

        if (source == "cart") {
            retryButton.text = "Voltar para Home"
            retryButton.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        } else {
            retryButton.text = "Tentar novamente"
            retryButton.setOnClickListener {
                startActivity(Intent(this, LoadState::class.java))
                finish()
            }
        }
    }
}
