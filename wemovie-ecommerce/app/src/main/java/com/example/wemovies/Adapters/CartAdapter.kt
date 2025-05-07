package com.example.wemovies.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wemovies.Models.Movie
import com.example.wemovies.ViewModels.CartItem
import com.example.wemovies.databinding.ItemCartBinding
import com.example.wemovies.databinding.ItemCartFooterBinding
import com.example.wemovies.databinding.ItemCartHeaderBinding

/**
 * Componetizaçao do Layout
 */

// Representa os 3 tipos de conteúdo que serão exibidos no RecyclerView
sealed class CarrinhoItem {
    object Header : CarrinhoItem()
    data class Produto(val cartItem: CartItem) : CarrinhoItem()
    object Footer : CarrinhoItem()
}

class CartAdapter(
    private val onIncrement: (Movie) -> Unit,
    private val onDecrement: (Movie) -> Unit,
    private val onRemove: (Movie) -> Unit,
    private val onCheckout: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<CarrinhoItem>()

    /**
     * Substitui os dados da lista por uma nova, encapsulando com header e footer.
     */
    fun submitList(cartItems: List<CartItem>) {
        val newList = mutableListOf<CarrinhoItem>()
        newList.add(CarrinhoItem.Header)
        newList.addAll(cartItems.map { CarrinhoItem.Produto(it) })
        newList.add(CarrinhoItem.Footer)
        items = newList
        notifyDataSetChanged()
    }

    /**
     * Define o tipo de layout para cada posição, para renderizar corretamente
     * os diferentes tipos de itens na lista (Header, Produto, Footer).
     */
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CarrinhoItem.Header -> 0
            is CarrinhoItem.Produto -> 1
            is CarrinhoItem.Footer -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            0 -> HeaderViewHolder(ItemCartHeaderBinding.inflate(inflater, parent, false))
            1 -> CartViewHolder(ItemCartBinding.inflate(inflater, parent, false))
            2 -> FooterViewHolder(ItemCartFooterBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("ViewType inválido: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is CarrinhoItem.Header -> {
                // Header é estático, então nada a fazer
            }

            is CarrinhoItem.Produto -> {
                val binding = (holder as CartViewHolder).binding
                val cartItem = item.cartItem
                binding.movieTitle.text = cartItem.movie.title
                binding.addedDate.text = "Adicionado em ${cartItem.addedDate}"
                binding.quantityText.text = cartItem.quantity.toString()
                binding.subtotalText.text =
                    "R$ %.2f".format(cartItem.movie.price * cartItem.quantity)
                Glide.with(binding.movieImage.context).load(cartItem.movie.image)
                    .into(binding.movieImage)

                binding.incrementBtn.setOnClickListener { onIncrement(cartItem.movie) }
                binding.decrementBtn.setOnClickListener { onDecrement(cartItem.movie) }
                binding.removeBtn.setOnClickListener { onRemove(cartItem.movie) }
            }

            is CarrinhoItem.Footer -> {
                val binding = (holder as FooterViewHolder).binding
                val total = items.filterIsInstance<CarrinhoItem.Produto>()
                    .sumOf { it.cartItem.movie.price * it.cartItem.quantity }
                binding.totalValue.text = "R$ %.2f".format(total)
                binding.checkoutButton.setOnClickListener { onCheckout() }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    // ViewHolders para cada tipo de item
    inner class HeaderViewHolder(val binding: ItemCartHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class FooterViewHolder(val binding: ItemCartFooterBinding) :
        RecyclerView.ViewHolder(binding.root)
}
