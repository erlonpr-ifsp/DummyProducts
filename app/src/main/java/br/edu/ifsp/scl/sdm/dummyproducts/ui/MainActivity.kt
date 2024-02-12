package br.edu.ifsp.scl.sdm.dummyproducts.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.dummyproducts.R
import br.edu.ifsp.scl.sdm.dummyproducts.adapter.ProductAdapter
import br.edu.ifsp.scl.sdm.dummyproducts.adapter.ProductImageAdapter
import br.edu.ifsp.scl.sdm.dummyproducts.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.dummyproducts.model.Product
import br.edu.ifsp.scl.sdm.dummyproducts.model.ProductList
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedInputStream
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
    private val productAdapter: ProductAdapter by lazy { // instacia adapter ProductAdapter
        ProductAdapter(this, productList)
    }

    private val productImageList: MutableList<Bitmap> =
        mutableListOf() // lista de imagens (data source)
    private val productImageAdapter: ProductImageAdapter by lazy { // instancia adapter ProductImageAdapter
        ProductImageAdapter(this, productImageList)
    }

    // companion object é utilizado para criar membros de classe que são acessíveis diretamente na classe, em vez de em instâncias específicas da classe
    companion object {
        const val PRODUCTS_ENDPOINT = "https://dummyjson.com/products/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb.apply { // define a action bar
            title = getString(R.string.app_name) // define o título da action bar
        })

        amb.productsSp.apply {
            adapter = productAdapter // associa o adapter do ProductAdapter ao spinner
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val size = productImageList.size
                    productImageList.clear() // limpa o data set do data source
                    productImageAdapter.notifyItemRangeRemoved(
                        0,
                        size
                    ) // notifica o adapter que foram removidos todos itens do data set
                    retrieveProductImages(productList[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // não se aplica
                }
            }
        }

        amb.productImagesRv.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity) // define o layout do RecyclerView
            adapter =
                productImageAdapter // associa o adapter ProductImageAdapter ao RecyclerView do layout
        }

        // chama a função retrieveProducts()
        retrieveProducts()

    } // fechamento onCreate

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

    // função para buscar as imagens de produto no arquivo JSON
    private fun retrieveProductImages(product: Product) = Thread {
        product.images.forEach { imageUrl ->
            val imageConnection = URL(imageUrl).openConnection() as HttpURLConnection
            try {
                if (imageConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedInputStream(imageConnection.inputStream).let {
                        val imageBitmap =
                            BitmapFactory.decodeStream(it) // decodifica o input stream no formato binário em um bitmap

                        runOnUiThread {
                            productImageList.add(imageBitmap) // adiciona o Bitmap à lista productImageList
                            productImageAdapter.notifyItemInserted(productImageList.lastIndex) // notifica o adapter que foi adicionado um novo item na última posição da lista productImageList
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
            } finally {
                imageConnection.disconnect()
            }
        }
    }.start()

}