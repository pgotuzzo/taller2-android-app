package ar.uba.fi.tallerii.comprameli.presentation.search.filter

import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import ar.uba.fi.tallerii.comprameli.presentation.widget.list.adapter.SelectableItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SearchFiltersPresenter(private val mProductsService: ProductsService) :
        BasePresenter<SearchFiltersContract.View>(), SearchFiltersContract.Presenter {

    private val mDisposables = CompositeDisposable()
    private var mProductFilter: ProductFilter = ProductFilter()

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit(productFilter: ProductFilter?) {
        if (productFilter != null) {
            mProductFilter = productFilter
        }

        getView()?.setPrice(mProductFilter.priceMin, mProductFilter.priceMax)
        getView()?.setUnits(mProductFilter.units)
        val disposable = Singles.zip(
                // Categories
                mProductsService.getCategories().map {
                    it.map { cat ->
                        val categoryName = cat.name
                        SelectableItem(categoryName, mProductFilter.categories.contains(categoryName))
                    }
                },
                // Payment Methods
                mProductsService.getPaymentMethods().map {
                    it.map { payment ->
                        val paymentName = payment.name
                        SelectableItem(paymentName, mProductFilter.paymentMethods.contains(paymentName))
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

    override fun onSubmitClick(filtersFormData: SearchFiltersContract.FiltersFormData) {
        mProductFilter = mProductFilter.copy(
                units = if (filtersFormData.minUnits.isNullOrEmpty()) null else filtersFormData.minUnits!!.toInt(),
                priceMin = if (filtersFormData.minPrice.isNullOrEmpty()) null else filtersFormData.minPrice!!.toFloat(),
                priceMax = if (filtersFormData.maxPrice.isNullOrEmpty()) null else filtersFormData.maxPrice!!.toFloat(),
                categories = filtersFormData.categoriesSelected,
                paymentMethods = filtersFormData.paymentMethodsSelected
        )
        getView()?.notifyFilter(mProductFilter)
    }

    private data class InitHolder(val categories: List<SelectableItem>,
                                  val paymentMethods: List<SelectableItem>)

}