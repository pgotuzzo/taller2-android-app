package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import ar.uba.fi.tallerii.comprameli.domain.products.Product
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles

class ProductDetailsPresenter(private val mProductsService: ProductsService,
                              private val profileService: ProfileService) :
        BasePresenter<ProductDetailsContract.View>(),
        ProductDetailsContract.Presenter {

    private val mDisposables = CompositeDisposable()

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit(productId: String) {
        val disposable = Singles.zip(
                profileService.getProfile().map { profile -> profile.userId },
                mProductsService.getProductById(productId)
        ) { userId: String, product: Product ->
            val questions = product.question.map { question ->
                ProductDetailsContract.Question(
                        question = question.text,
                        answer = question.answer?.text)
            }
            val productDetails = ProductDetailsContract.ProductDetails(
                    title = product.title,
                    description = product.description,
                    images = product.images,
                    price = product.price,
                    units = product.units,
                    questions = questions
            )
            val isCurrentUserOwner = userId == product.seller
            InitHolder(productDetails, isCurrentUserOwner)
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { initHolder ->
                            getView()?.apply {
                                showProductDetails(initHolder.productDetails)
                                enableBuyButton(
                                        // owner check
                                        !initHolder.isCurrentUserOwner &&
                                                // stock available
                                                initHolder.productDetails.units > 0
                                )
                                enableAskQuestionButton(!initHolder.isCurrentUserOwner)
                            }
                        },
                        { getView()?.showError() }
                )
        mDisposables.add(disposable)
    }

    override fun onBuyButtonClick() {
        // TODO - Implement
    }

    override fun onAskQuestionButtonClick() {
        getView()?.showQuestionDialog()
    }

    override fun onSendQuestionClick(question: String?) {
        // TODO - Implement
    }

    inner class InitHolder(val productDetails: ProductDetailsContract.ProductDetails,
                           val isCurrentUserOwner: Boolean)
}