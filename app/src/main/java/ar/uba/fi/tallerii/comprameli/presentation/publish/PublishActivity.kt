package ar.uba.fi.tallerii.comprameli.presentation.publish

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.Editable
import android.widget.LinearLayout.HORIZONTAL
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.publish.di.PublishModule
import ar.uba.fi.tallerii.comprameli.presentation.utils.SimpleTextWatcher
import ar.uba.fi.tallerii.comprameli.presentation.widget.list.adapter.CategoryToggleListAdapter
import ar.uba.fi.tallerii.comprameli.presentation.widget.list.adapter.SelectableItem
import kotlinx.android.synthetic.main.publish_activity.*
import timber.log.Timber
import javax.inject.Inject

class PublishActivity : BaseActivity(), PublishContract.View {

    private object RequestCode {
        const val GET_IMAGE = 1
    }

    private object Cons {
        const val GRID_SPAN_COUNT = 2
    }

    @Inject
    lateinit var mPresenter: PublishContract.Presenter

    private val mComponent by lazy { app.component.plus(PublishModule()) }
    private val mImagesAdapter = ImagesAdapter { getImageFromDevice() }
    private val mCategoriesAdapter = CategoryToggleListAdapter()
    private val mPaymentMethodsAdapter = CategoryToggleListAdapter { _, _ -> onPaymentMethodsSelectionChanged() }
    private val mGeoCoder by lazy { Geocoder(this) }
    private val mHandler by lazy { Handler() }
    private val mCheckAddressRunnable = Runnable {
        addressInputEdit.text?.apply {
            val address = decodeAddressFromInput(this.toString())
            mPresenter.onAddressChanged(address?.latitude, address?.longitude)
        }
    }
    private val mAddressTextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            mHandler.removeCallbacks(mCheckAddressRunnable)
            mHandler.postDelayed(mCheckAddressRunnable, 2000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.publish_activity)
        mComponent.inject(this)
        mPresenter.attachView(this)

        imageList.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        imageList.adapter = mImagesAdapter
        imageList.setHasFixedSize(true)

        categoriesList.layoutManager = StaggeredGridLayoutManager(Cons.GRID_SPAN_COUNT, StaggeredGridLayoutManager.HORIZONTAL)
        categoriesList.adapter = mCategoriesAdapter
        categoriesList.setHasFixedSize(true)

        paymentMethodsList.layoutManager = StaggeredGridLayoutManager(Cons.GRID_SPAN_COUNT, StaggeredGridLayoutManager.HORIZONTAL)
        paymentMethodsList.adapter = mPaymentMethodsAdapter
        paymentMethodsList.setHasFixedSize(true)

        titleInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mPresenter.onTitleChanged(s?.toString())
            }
        })

        descriptionInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mPresenter.onDescriptionChanged(s?.toString())
            }
        })

        priceInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mPresenter.onPriceChanged(s?.toString())
            }
        })

        unitsInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mPresenter.onUnitsChanged(s?.toString())
            }
        })

        addressInputEdit.addTextChangedListener(mAddressTextWatcher)

        submitBtn.setOnClickListener {
            mPresenter.onSubmit(
                    mImagesAdapter.getImages().map { uri -> uri.toString() },
                    mCategoriesAdapter.getItemsSelected(),
                    mPaymentMethodsAdapter.getItemsSelected()
            )
        }

        mPresenter.onInit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.GET_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                mImagesAdapter.addImage(uri)
            }
        }
    }

    override fun setCategories(categories: List<SelectableItem>) {
        mCategoriesAdapter.setItems(categories)
    }

    override fun setPaymentMethods(paymentMethods: List<SelectableItem>) {
        mPaymentMethodsAdapter.setItems(paymentMethods)
    }

    override fun showAddress(latitude: Double, longitude: Double) {
        addressInputEdit.removeTextChangedListener(mAddressTextWatcher)
        addressInputEdit.setText(
                formatAddress(
                        mGeoCoder.getFromLocation(latitude, longitude, 1)[0]
                )
        )
        addressInputEdit.addTextChangedListener(mAddressTextWatcher)
    }

    override fun showSubmitButton(show: Boolean) {
        if (show) submitBtn.show() else submitBtn.hide()
    }

    override fun showLoading(show: Boolean) {
        progressOverlay.enable(show)
    }

    override fun dismiss() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun getImageFromDevice() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, RequestCode.GET_IMAGE)
    }

    private fun onPaymentMethodsSelectionChanged() {
        mPresenter.onPaymentMethodsSelected(mPaymentMethodsAdapter.getItemsSelected())
    }

    private fun decodeAddressFromInput(addressInput: String?): Address? {
        if (addressInput.isNullOrEmpty())
            return null

        val addresses: List<Address> = mGeoCoder.getFromLocationName(addressInput, 1)
        return if (addresses.isNotEmpty()) {
            val address = addresses[0]
            Timber.d("Latitude: %s, Longitude: %s, Direccion: %s", address.latitude, address.longitude, formatAddress(address))
            address
        } else null
    }

    private fun formatAddress(address: Address): String =
            address.featureName + ", " + address.thoroughfare + ", " + address.locality + ", " + address.adminArea + ", " + address.countryName

}