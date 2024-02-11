package br.edu.ifsp.scl.sdm.dummyproducts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.scl.sdm.dummyproducts.R
import br.edu.ifsp.scl.sdm.dummyproducts.adapter.ProductAdapter
import br.edu.ifsp.scl.sdm.dummyproducts.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.dummyproducts.model.Product

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val productList: MutableList<Product> =
        mutableListOf() // lista de produtos (data source)
    private val productAdapter: ProductAdapter by lazy { // adapter ProductAdapter
        ProductAdapter(this, productList)
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

    }
}