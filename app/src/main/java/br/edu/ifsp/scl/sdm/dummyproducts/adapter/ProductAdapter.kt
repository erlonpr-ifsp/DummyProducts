package br.edu.ifsp.scl.sdm.dummyproducts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.scl.sdm.dummyproducts.model.Product

class ProductAdapter(
    // variáveis do construtor da classe
    private val activityContext: Context, // contexto
    private val productList: MutableList<Product> // lista do JSON Products
) : ArrayAdapter<Product>(
    activityContext,
    android.R.layout.simple_list_item_1, // android.R.layout.simple_list_item_1 refere-se a um layout de lista padrão fornecido pelo Android que contém um único TextView
    productList
) {


    // viewHolder
    private data class ProductHolder(val productTitleTv: TextView) // aponta para um TextView


    // método getView é responsável por pegar um item do productList e transformar em uma célula do tipo simple_list_item_1
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // verifica se o convertView é nulo para analisar a necessidade de inflar ou não uma nova célula ou reaproveitar o mesmo convertView
        val productView = convertView ?: LayoutInflater.from(activityContext).inflate(
                android.R.layout.simple_list_item_1, // layout pré-definido no Android que consiste em um TextView para exibir um único item na lista
                parent, // é o ViewGroup no qual a nova visualização será eventualmente anexada. No contexto de um adaptador de lista, isso geralmente é a ListView ou RecyclerView à qual o adaptador está associado
                false // Indica que a nova visualização não deve ser anexada ao parent durante a inflação
            ).apply {
                // criação do ViewHolder para a célula que acabou de ser inflada
                tag =
                    ProductHolder(findViewById(android.R.id.text1)) // tag recebe o TextView do R.layout.simple_list_item_1
                // ProductHolder é um contêiner usado para armazenar a referência ao TextView que exibirá o título do produto na lista
                // tag da productView é definido como uma instância de ProductHolder
                // ProductHolder está sendo associado à productView como uma espécie de "etiqueta" (tag)
                // ao associar o ProductHolder à productView usando a propriedade tag, é possível manter uma referência ao TextView mesmo quando a View é reciclada
                // Isso evita a necessidade de buscar novamente o TextView na hierarquia de visualização
            }

        // casting da View para ViewHolder
        (productView.tag as ProductHolder).productTitleTv.text = productList[position].title

        return productView // productView é uma instância de View que representa a visualização de um item na lista
    }

}