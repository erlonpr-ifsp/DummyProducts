package br.edu.ifsp.scl.sdm.dummyproducts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.scl.sdm.dummyproducts.model.Product

class ProductAdapter(
    private val activityContext: Context, // contexto
    private val productList: MutableList<Product> // lista do JSON Products
) : ArrayAdapter<Product>(activityContext, android.R.layout.simple_list_item_1, productList) {


    // viewHolder
    private data class ProductHolder(val productTitleTv: TextView) // aponta para um TextView


    // método getView é responsável por pegar um item do productList e transformar em uma célula do tipo simple_list_item_1
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // verifica se o convertView é nulo para analisar a necessidade de inflar ou não uma nova célula
        val productView = convertView ?: LayoutInflater.from(activityContext)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        return productView
    }

}