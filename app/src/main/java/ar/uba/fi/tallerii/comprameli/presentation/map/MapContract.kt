package ar.uba.fi.tallerii.comprameli.presentation.map

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface MapContract {

    data class Marker(val longitude: Double,
                      val latitude: Double,
                      val productId: String,
                      val productName: String,
                      val productImage: String?)

    interface View {
        fun addMarkers(markers: List<Marker>)
    }

    interface Presenter : MvpPresenter<View> {
        fun onMapIdle(longitude: Float, latitude: Float, radius: Float)
    }
}