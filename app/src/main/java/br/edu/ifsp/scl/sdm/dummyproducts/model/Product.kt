package br.edu.ifsp.scl.sdm.dummyproducts.model

data class Product(
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val id: Int,
    val images: List<String>,
    val price: Int,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String
){
    override fun toString(): String {
        return title // retorno da função toString será somente o valor do campo title
    }
}