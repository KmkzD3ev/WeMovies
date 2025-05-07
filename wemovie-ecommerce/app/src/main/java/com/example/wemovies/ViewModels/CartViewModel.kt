package com.example.wemovies.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wemovies.Models.Movie
import java.text.SimpleDateFormat
import java.util.*

/**
 * Representa um item no carrinho de compras.
 * Contém o filme, a quantidade selecionada e a data de adição.
 */
data class CartItem(
    val movie: Movie,
    var quantity: Int = 1,
    val addedDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
)

/**
 * ViewModel responsável por gerenciar o estado do carrinho de compras.
 * Compartilhado entre as telas para permitir atualização em tempo real.
 */
class CartViewModel : ViewModel() {

    // Lista interna do carrinho (imutável )
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    // Valor total dos itens no carrinho
    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> get() = _totalPrice

    /**
     * Adiciona um filme ao carrinho.
     * Se o filme já existir, incrementa a quantidade.
     */
    fun addToCart(movie: Movie) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentList.find { it.movie.id == movie.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentList.add(CartItem(movie))
        }

        _cartItems.value = currentList
        recalculateTotal()
    }

    /**
     * Remove completamente um filme do carrinho.
     */
    fun removeFromCart(movie: Movie) {
        val updatedList = _cartItems.value?.filter { it.movie.id != movie.id } ?: return
        _cartItems.value = updatedList
        recalculateTotal()
    }

    /**
     * Incrementa a quantidade de um filme no carrinho.
     */
    fun incrementQuantity(movie: Movie) {
        updateQuantity(movie.id, increase = true)
    }

    /**
     * Decrementa a quantidade de um filme no carrinho.
     * Se a quantidade chegar a zero, o item é removido.
     */
    fun decrementQuantity(movie: Movie) {
        updateQuantity(movie.id, increase = false)
    }

    /**
     * Atualiza a quantidade de um item com base no ID do filme.
     */
    private fun updateQuantity(movieId: Int, increase: Boolean) {
        val currentList = _cartItems.value?.toMutableList() ?: return
        val item = currentList.find { it.movie.id == movieId } ?: return

        if (increase) {
            item.quantity++
        } else {
            if (item.quantity > 1) {
                item.quantity--
            } else {
                currentList.remove(item)
            }
        }

        _cartItems.value = currentList
        recalculateTotal()
    }

    /**
     * Retorna o subtotal de um único item com base na quantidade.
     */
    fun getSubtotal(movie: Movie): Double {
        val item = _cartItems.value?.find { it.movie.id == movie.id } ?: return 0.0
        return item.movie.price * item.quantity
    }

    /**
     * Limpa todo o carrinho.
     */
    fun clearCart() {
        _cartItems.value = emptyList()
        _totalPrice.value = 0.0
    }

    /**
     * Recalcula o total do carrinho com base nas quantidades e preços.
     */
    private fun recalculateTotal() {
        _totalPrice.value = _cartItems.value?.sumOf { it.movie.price * it.quantity } ?: 0.0
    }
}
