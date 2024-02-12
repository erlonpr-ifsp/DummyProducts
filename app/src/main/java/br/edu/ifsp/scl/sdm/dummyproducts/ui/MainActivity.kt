package br.edu.ifsp.scl.sdm.dummyproducts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.scl.sdm.dummyproducts.R
import br.edu.ifsp.scl.sdm.dummyproducts.adapter.ProductAdapter
import br.edu.ifsp.scl.sdm.dummyproducts.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.dummyproducts.model.Product
import br.edu.ifsp.scl.sdm.dummyproducts.model.ProductList
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy { // amb refere-se ao arquivo de layout activity_main.xml
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val productList: MutableList<Product> =
        mutableListOf() // lista de produtos (data source)
    private val productAdapter: ProductAdapter by lazy { // adapter ProductAdapter
        ProductAdapter(this, productList)
    }

    // companion object é utilizado para criar membros de classe que são acessíveis diretamente na classe, em vez de em instâncias específicas da classe
    companion object {
        const val PRODUCTS_ENDPOINT = "https://dummyjson.com/products/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        // define quem é a action bar
        setSupportActionBar(amb.mainTb.apply {
            title = getString(R.string.app_name) // define o título da action bar
        })

        // associa o adapter ao spinner
        amb.productsSp.adapter = productAdapter

        // chama a função retrieveProducts()
        retrieveProducts()

    }

    // função para buscar o conteúdo JSON no Web Service e popular o Spinner
    private fun retrieveProducts() =
        Thread { // criação de uma thread secundária para execução do código que importa os dados JSON para o ProductAdapter (Runnable de Thread)
            val productsConnection =
                URL(PRODUCTS_ENDPOINT).openConnection() as HttpURLConnection // conexão é feita por padrão com o método GET

            try {
                if (productsConnection.responseCode == HttpURLConnection.HTTP_OK) {

                    InputStreamReader(productsConnection.inputStream).readText().let {

                        runOnUiThread {
                            // Runnable do runOnUiThread

                            productAdapter.addAll(
                                Gson().fromJson(
                                    it,
                                    ProductList::class.java
                                ).products
                            ) // reflexão do objeto JSON em uma lista de ProductList
                        }


                    }

                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            getString(R.string.request_problem),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } catch (ioe: IOException) {
                runOnUiThread {
                    Toast.makeText(this, getString(R.string.connection_failed), Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (jse: JsonSyntaxException) {
                runOnUiThread {
                    Toast.makeText(this, getString(R.string.response_problem), Toast.LENGTH_SHORT)
                        .show()
                }
            } finally {
                productsConnection.disconnect()
            }
        }.start()

}