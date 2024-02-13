package br.edu.ifsp.scl.sdm.dummyproducts.model

import android.content.Context
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.net.HttpURLConnection

// classe DummyJSONAPI segue o padrão Singleton
class DummyJSONAPI(context: Context) { // recebe parâmetro context do tipo Context no construtor
    companion object { // objeto para criar membros estáticos (utilizado para implementar o padrão Singleton)

        const val PRODUCTS_ENDPOINT = "https://dummyjson.com/products/"

        private var INSTANCE: DummyJSONAPI? =
            null // variável INSTANCE armazenará a única instância da classe DummyJSONAPI

        fun getInstance(context: Context) = INSTANCE
            ?: synchronized(this) { // função que retorna a instância única da classe DummyJSONAPI
                INSTANCE ?: DummyJSONAPI(context).also {
                    INSTANCE = it
                }
            }
    }

    // fila de requisições
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext) // utilizou applicationContext para que a fila seja genérica e não apenas para uma única activity
    }

    // adiciona na fila de requisição
    fun <T> addToRequestQueue(request: Request<T>) {
        requestQueue.add(request)
    }

    class ProductListRequest(
        private val responseListener: Response.Listener<ProductList>,
        errorListener: Response.ErrorListener
    ) : Request<ProductList>(Method.GET, PRODUCTS_ENDPOINT, errorListener) {

        override fun parseNetworkResponse(response: NetworkResponse?): Response<ProductList> =
            if (response?.statusCode == HttpURLConnection.HTTP_OK || response?.statusCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
                String(response.data).run {
                    Response.success(
                        Gson().fromJson(this, ProductList::class.java),
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            } else {
                Response.error(VolleyError())
            }


        override fun deliverResponse(response: ProductList?) {
            responseListener.onResponse(response)
        }

    }

}