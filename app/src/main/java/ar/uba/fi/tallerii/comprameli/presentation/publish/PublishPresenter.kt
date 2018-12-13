package ar.uba.fi.tallerii.comprameli.presentation.publish

import ar.uba.fi.tallerii.comprameli.domain.products.ProductData
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import ar.uba.fi.tallerii.comprameli.presentation.widget.list.adapter.SelectableItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PublishPresenter(private val mProductsService: ProductsService) :
        BasePresenter<PublishContract.View>(), PublishContract.Presenter {

    private val mDisposables = CompositeDisposable()
    private var mTitle: String? = null
    private var mDescription: String? = null
    private var mPrice: Float? = null
    private var mUnits: Int? = null
    private var mLongitude: Double? = null
    private var mLatitude: Double? = null
    private var mPaymentsMethods: List<String> = ArrayList()
    private var mIsProcessing: Boolean = false

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit() {
        // Hide submit button
        getView()?.showSubmitButton(false)
        // Fetch existing categories and payment methods
        val disposable = Singles.zip(
                // Categories
                mProductsService.getCategories().map {
                    it.map { cat ->
                        val categoryName = cat.name
                        SelectableItem(categoryName, false)
                    }
                },
                // Payment Methods
                mProductsService.getPaymentMethods().map {
                    it.map { payment ->
                        val paymentName = payment.name
                        SelectableItem(paymentName, false)
                    }
                }
        ) { categories: List<SelectableItem>, paymentMethods: List<SelectableItem> ->
            InitHolder(categories, paymentMethods)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getView()?.setCategories(it.categories)
                    getView()?.setPaymentMethods(it.paymentMethods)
                }, {
                    Timber.e(it)
                })

        mDisposables.add(disposable)
    }

    override fun onTitleChanged(text: String?) {
        mTitle = text
        validateInputs()
    }

    override fun onDescriptionChanged(text: String?) {
        mDescription = text
        validateInputs()
    }

    override fun onPriceChanged(text: String?) {
        mPrice = try {
            text?.toFloat()
        } catch (e: NumberFormatException) {
            null
        }
        validateInputs()
    }

    override fun onUnitsChanged(text: String?) {
        mUnits = try {
            text?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
        validateInputs()
    }

    override fun onAddressChanged(latitude: Double?, longitude: Double?) {
        mLongitude = longitude
        mLatitude = latitude
        validateInputs()
    }

    override fun onPaymentMethodsSelected(paymentMethods: List<String>) {
        mPaymentsMethods = paymentMethods
        validateInputs()
    }

    override fun onSubmit(imagesUri: List<String>, categories: List<String>, paymentMethods: List<String>) {
        if (!mIsProcessing) {
            mIsProcessing = true
            val newProduct = ProductData(
                    title = mTitle!!,
                    description = mDescription!!,
                    price = mPrice!!,
                    units = mUnits!!,
                    images = imagesUri,
                    categories = categories,
                    paymentMethods = paymentMethods,
                    longitude = mLongitude!!,
                    latitude = mLatitude!!
            )
            submitProduct(newProduct)
        }
    }

    private fun validateInputs() {
        getView()?.apply {
            val show = !mTitle.isNullOrEmpty() &&
                    !mDescription.isNullOrEmpty() &&
                    mPrice != null &&
                    mUnits != null &&
                    mLatitude != null && mLongitude != null &&
                    mPaymentsMethods.isNotEmpty()
            if (show) {
                showAddress(mLatitude!!, mLongitude!!)
            }
            showSubmitButton(show)
        }
    }

    private fun submitProduct(productData: ProductData) {
        getView()?.apply {
            // Update UI
            showSubmitButton(false)
            showLoading(true)
        }
        val disposable = mProductsService
                .createProduct(productData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    getView()?.apply {
                        mIsProcessing = false
                        showLoading(false)
                        dismiss()
                    }
                }

        mDisposables.add(disposable)
    }

    private data class InitHolder(val categories: List<SelectableItem>,
                                  val paymentMethods: List<SelectableItem>)

}