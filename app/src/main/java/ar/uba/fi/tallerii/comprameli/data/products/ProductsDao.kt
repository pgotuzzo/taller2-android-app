package ar.uba.fi.tallerii.comprameli.data.products

import io.reactivex.Single

interface ProductsDao {

    fun getProducts(): Single<List<Product>>

}