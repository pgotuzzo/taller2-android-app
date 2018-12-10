package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import ar.uba.fi.tallerii.comprameli.domain.products.Product
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.domain.products.Question
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import timber.log.Timber


class ProductDetailsPresenter(private val mProductsService: ProductsService,
                              private val profileService: ProfileService) :
        BasePresenter<ProductDetailsContract.View>(),
        ProductDetailsContract.Presenter {

    private val mDisposables = CompositeDisposable()
    private var mProduct: Product? = null
    private var mIsCurrentUserOwner: Boolean = false

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit(productId: String) {
        val disposable = Singles.zip(
                profileService.getProfile().map { profile -> profile.userId },
                mProductsService.getProductById(productId)
        ) { userId: String, product: Product ->
            mIsCurrentUserOwner = userId == product.seller
            product
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { product -> processProduct(product) },
                        { throwable ->
                            Timber.e(throwable)
                            getView()?.showError()
                        }
                )
        mDisposables.add(disposable)
    }

    override fun onBuyButtonClick() {
        mProduct?.apply {
            getView()?.goCheckOut(this)
        }
    }

    override fun onQuestionButtonClick() {
        mProduct?.let { product ->
            getView()?.also { view ->
                if (mIsCurrentUserOwner) {
                    view.showSelectQuestionToAnswerDialog(
                            product.questions.filter { q -> q.answer == null }.map { q -> fromQuestion(q) }
                    )
                } else {
                    view.showAskQuestionDialog()
                }
            }
        }
    }

    override fun onSendQuestionClick(question: String) {
        mProduct?.productId?.apply {
            val disposable =
                    mProductsService
                            .addQuestionToProduct(this, question)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { product -> processProduct(product) },
                                    { throwable ->
                                        Timber.e(throwable)
                                        getView()?.showError()
                                    })
            mDisposables.add(disposable)
        }
    }

    override fun onSendAnswerClick(questionId: String, answer: String) {
        mProduct?.productId?.apply {
            val disposable =
                    mProductsService
                            .answerQuestion(this, questionId, answer)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { product -> processProduct(product) },
                                    { throwable ->
                                        Timber.e(throwable)
                                        getView()?.showError()
                                    })
            mDisposables.add(disposable)
        }
    }

    override fun onQrButtonClick() {
        mProduct?.also { getView()?.showQR(it.productId) }
    }

    private fun processProduct(product: Product) {
        mProduct = product
        getView()?.apply {
            showProductDetails(fromProduct(product))
            if (!mIsCurrentUserOwner) {
                // stock available
                enableBuyButton(product.units > 0)
                // user can post questions
                enableQuestionButton(false)
            } else {
                enableBuyButton(false)
                if (product.questions.find { q -> q.answer == null } != null) {
                    // questions missing answer
                    enableQuestionButton(true)
                } else {
                    disableQuestionButton()
                }
            }
        }
    }

    private fun fromQuestion(question: Question): ProductDetailsContract.Question =
            ProductDetailsContract.Question(
                    id = question.id,
                    question = question.text,
                    answer = question.answer?.text)

    private fun fromProduct(product: Product): ProductDetailsContract.ProductDetails =
            ProductDetailsContract.ProductDetails(
                    title = product.title,
                    description = product.description,
                    images = product.images,
                    price = product.price,
                    units = product.units,
                    questions = product.questions.map { question -> fromQuestion(question) }
            )

}