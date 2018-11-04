package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProductDetailsPresenter(private val mProductsService: ProductsService) : BasePresenter<ProductDetailsContract.View>(),
        ProductDetailsContract.Presenter {

    // FIXME - Remove - BEGIN
    companion object {
        val MOCK_QUESTION_1 = ProductDetailsContract.Question(question = "Estas cerca del microcentro?", inquirer = "Mabel Lescano", answer = null)
        val MOCK_QUESTION_2 = ProductDetailsContract.Question(
                question = "Esta en buenas condiciones? Me preocupan las marcas que veo que tienen en las fotos. No parece nuevo",
                inquirer = "Don Ramon",
                answer = "A decir verdad, tiene unos pocos de dias de uso. Las marcas que ves son insignificantes. Quedate tranquilo que la garantia te cubre por los proximos 6 meses. Si algo sale mal podes pedir reembolso. Gracias por tu consulta. Saludos!"
        )
        val MOCK_QUESTION_3 = ProductDetailsContract.Question(question = "Tenes stock?", inquirer = "Pablo Gotuzzo", answer = "Si. Oferta tranquilo!")
    }
    // FIXME - Remove - END

    private val mDisposables = CompositeDisposable()

    override fun onInit(productId: String) {
        val disposable = mProductsService
                .getProductById(productId)
                .map { product ->
                    ProductDetailsContract.ProductDetails(
                            title = product.title,
                            description = product.description,
                            images = product.images,
                            price = product.price,
                            units = product.units,
                            // FIXME - Replace with real logic - BEGIN
                            questions = listOf(MOCK_QUESTION_1, MOCK_QUESTION_2, MOCK_QUESTION_3)
                            // FIXME - Replace with real logic - END
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

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

}