package br.edu.ifsp.scl.sdm.dummyproducts.model

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

// classe DummyJSONAPI segue o padrão Singleton
class DummyJSONAPI(context: Context) { // recebe parâmetro context do tipo Context no construtor
    companion object { // objeto para criar membros estáticos (utilizado para implementar o padrão Singleton)
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
    fun <T> addRequestQueue(request: Request<T>) {
        requestQueue.add(request)
    }

}