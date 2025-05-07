package com.example.wemovies.utils

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * RecycleView Personalisado para exibiçao no Carrinho
 * Usado para quanitdades Maiores de dados e Exibiçoes
 */

class WrapContentRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val expandedHeight = MeasureSpec.makeMeasureSpec(
            // Realiza a Mediçao Geral do Tamanho da tela para Prescisao e Performace
            Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST
        )
        super.onMeasure(widthSpec, expandedHeight)
    }
}