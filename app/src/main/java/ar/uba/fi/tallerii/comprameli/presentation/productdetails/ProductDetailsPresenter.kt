package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProductDetailsPresenter(private val mProductsService: ProductsService) : BasePresenter<ProductDetailsContract.View>(),
        ProductDetailsContract.Presenter {

    private val mDisposables = CompositeDisposable()

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit(productId: String) {
        val disposable = mProductsService
                .getProductById(productId)
                .map { product ->
                    val questions = product.question.map {
                        question -> ProductDetailsContract.Question(
                            question = question.text,
                            answer = question.answer?.text)
                    }
                    ProductDetailsContract.ProductDetails(
                            title = product.title,
                            description = product.description,
                            images = product.images,
                            price = product.price,
                            units = product.units,
                            questions = questions
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { details -> getView()?.showProductDetails(details) },
                        { getView()?.showError() }
                )
        mDisposables.add(disposable)
    }



}