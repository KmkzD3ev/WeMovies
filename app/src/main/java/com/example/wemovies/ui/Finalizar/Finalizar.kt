package com.example.wemovies.ui.Finalizar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wemovies.MainActivity
import com.example.wemovies.R
import android.widget.TextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Finalizar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finalizar)

        /**
         * Otendo data e Hora para exibiçao na tela de finalizar
         */

        val purchaseDateText = findViewById<TextView>(R.id.purchaseDateText)
        val calendar = java.util.Calendar.getInstance()
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", java.util.Locale("pt", "BR"))
        val formattedDate = sdf.format(calendar.time)

        purchaseDateText.text = "Compra realizada em $formattedDate"


        // Captura a referencia do botão e configura o clique
        val backToHomeButton = findViewById<Button>(R.id.retry_button)
        backToHomeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
