package ar.uba.fi.tallerii.comprameli.presentation.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.map.di.MapComponent
import ar.uba.fi.tallerii.comprameli.presentation.map.di.MapModule
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.ProductDetailsActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.map_activity.*
import javax.inject.Inject

class MapActivity : BaseActivity(), MapContract.View {

    object RequestCode {
        const val PERMISSIONS = 100
    }

    @Inject
    lateinit var mPresenter: MapContract.Presenter

    val mComponent: MapComponent by lazy { app.component.plus(MapModule()) }

    var mGoogleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity)
        mComponent.inject(this)
        mPresenter.attachView(this)

        map.onCreate(savedInstanceState)

        map.getMapAsync { googleMap ->
            mGoogleMap = googleMap
            // User stop interacting with the map
            mGoogleMap?.setOnCameraIdleListener { onMapIdle() }

            mGoogleMap?.setOnInfoWindowClickListener { marker ->
                goProductDetails(marker.tag as String)
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap?.isMyLocationEnabled = true
            } else {
                ActivityCompat.requestPermissions(this@MapActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), RequestCode.PERMISSIONS)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        map.onStart()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }

    override fun onPause() {
        map.onPause()
        super.onPause()
    }

    override fun onStop() {
        map.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mPresenter.detachView()
        map.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == RequestCode.PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap?.isMyLocationEnabled = true
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    override fun addMarkers(markers: List<MapContract.Marker>) {
        mGoogleMap?.apply {
            markers.forEach {
                val options = MarkerOptions()
                options.position(LatLng(it.latitude.toDouble(), it.longitude.toDouble()))
                options.title(it.productName)
                val marker = addMarker(options)
                marker.tag = it.productId
            }
        }
    }

    private fun onMapIdle() {
        mGoogleMap?.apply {
            if (cameraPosition.zoom >= 12) {
                // Zoom is high enough to start requesting for products
                // top left
                val distance = FloatArray(2)
                Location.distanceBetween(
                        projection.visibleRegion.farLeft.latitude, projection.visibleRegion.farLeft.longitude,
                        projection.visibleRegion.nearRight.latitude, projection.visibleRegion.nearRight.longitude,
                        distance
                )
                val maxDistance = Math.max(distance[0], distance[1]) / 2
                val centre = cameraPosition.target
                mPresenter.onMapIdle(centre.longitude.toFloat(), centre.latitude.toFloat(), maxDistance)
            }
        }
    }

    private fun goProductDetails(productId: String) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra(ProductDetailsActivity.INTENT_BUNDLE_EXTRA_PRODUCT_ID, productId)
        startActivity(intent)
    }

}