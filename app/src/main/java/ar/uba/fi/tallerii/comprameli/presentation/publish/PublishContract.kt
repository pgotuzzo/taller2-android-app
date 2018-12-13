package ar.uba.fi.tallerii.comprameli.presentation.publish

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter
import ar.uba.fi.tallerii.comprameli.presentation.widget.list.adapter.SelectableItem

interface PublishContract {
    interface View {
        fun setCategories(categories: List<SelectableItem>)
        fun setPaymentMethods(paymentMethods: List<SelectableItem>)
        fun showAddress(latitude: Double, longitude: Double)
        fun showSubmitButton(show: Boolean)
        fun showLoading(show: Boolean)
        fun dismiss()
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit()
        fun onTitleChanged(text: String?)
        fun onDescriptionChanged(text: String?)
        fun onPriceChanged(text: String?)
        fun onUnitsChanged(text: String?)
        fun onAddressChanged(latitude: Double?, longitude: Double?)
        fun onPaymentMethodsSelected(paymentMethods: List<String>)
        fun onSubmit(imagesUri: List<String>, categories: List<String>, paymentMethods: List<String>)
    }
}