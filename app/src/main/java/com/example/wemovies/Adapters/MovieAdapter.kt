package com.example.wemovies.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wemovies.Models.Movie
import com.example.wemovies.R
import com.example.wemovies.databinding.ItemMovieBinding

class MovieAdapter(
    private val movies: List<Movie>,
    private val onAddToCartClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        with(holder.binding) {
            movieTitle.text = movie.title
            moviePrice.text = "R$ %.2f".format(movie.price)
            Glide.with(movieImage.context).load(movie.image).into(movieImage)

            // Clique no layout fakeado de botão
            addToCartContainer.setOnClickListener {
                onAddToCartClick(movie)

                // Atualiza visual do contador
                val currentCount = cartCountBadge.text.toString().toIntOrNull() ?: 0
                val newCount = currentCount + 1
                cartCountBadge.text = newCount.toString()

                // Muda cor de fundo para indicar "adicionado"
                addToCartContainer.setBackgroundResource(R.drawable.rounded_button_background_success)
            }
        }
    }

    override fun getItemCount(): Int = movies.size
}
