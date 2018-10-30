package ar.uba.fi.tallerii.comprameli.domain.products

import io.reactivex.Single

interface ProductsService {

    fun getProducts(): Single<List<Product>>

}