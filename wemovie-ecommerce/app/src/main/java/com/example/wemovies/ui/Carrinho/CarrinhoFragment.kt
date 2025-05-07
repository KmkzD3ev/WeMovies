package com.example.wemovies.ui.Carrinho

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wemovies.Adapters.CartAdapter
import com.example.wemovies.ViewModels.CartViewModel
import com.example.wemovies.databinding.FragmentCarrinhoBinding
import com.example.wemovies.Error.ErrorActivity
import com.example.wemovies.ui.Finalizar.Finalizar

class CarrinhoFragment : Fragment() {

    private var _binding: FragmentCarrinhoBinding? = null
    private val binding get() = _binding!!

    // ViewModel compartilhado com a Activity, armazena os itens e o total do carrinho
    private val cartViewModel: CartViewModel by activityViewModels()

    // Adapter com suporte a Header, Itens e Footer integrados no RecyclerView
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inicializa ViewBinding de forma segura
        _binding = FragmentCarrinhoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ViewCompat.setOnApplyWindowInsetsListener(binding.cartRecyclerView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, systemBars.bottom + 94) // espaço pro botão
            insets
        }


        // Inicializa o adapter e injeta ações dos botões
        cartAdapter = CartAdapter(
            onIncrement = { movie -> cartViewModel.incrementQuantity(movie) },
            onDecrement = { movie -> cartViewModel.decrementQuantity(movie) },
            onRemove = { movie -> cartViewModel.removeFromCart(movie) },
            onCheckout = { navegarParaFinalizar() }
        )

        // RecyclerView é o único componente da tela. Lista completa: Header, Produtos, Footer.
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
            setHasFixedSize(true)
        }

        // Observa a lista de produtos no carrinho
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            if (items.isNullOrEmpty()) {
                // Se o carrinho estiver vazio, navega para a tela de erro
                startActivity(
                    Intent(requireContext(), ErrorActivity::class.java).putExtra("source", "cart")
                )
                requireActivity().finish()
            } else {
                // Envia os dados para o Adapter, que vai inserir Header + Footer também
                cartAdapter.submitList(items.toList())
            }
        }
    }

    /**
     * Navega para a tela de finalização de pedido
     */
    private fun navegarParaFinalizar() {
        startActivity(Intent(requireContext(), Finalizar::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
