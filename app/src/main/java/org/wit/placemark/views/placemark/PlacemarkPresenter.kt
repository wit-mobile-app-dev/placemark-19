package org.wit.placemark.views.placemark

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.placemark.helpers.checkLocationPermissions
import org.wit.placemark.helpers.createDefaultLocationRequest
import org.wit.placemark.helpers.isPermissionGranted
import org.wit.placemark.helpers.showImagePicker
import org.wit.placemark.models.Location
import org.wit.placemark.models.PlacemarkModel
import org.wit.placemark.views.*

class PlacemarkPresenter(view: BaseView) : BasePresenter(view) {

  var map: GoogleMap? = null
  var placemark = PlacemarkModel()
  var defaultLocation = Location(52.245696, -7.139102, 15f)
  var edit = false;
  var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
  val locationRequest = createDefaultLocationRequest()

  init {
    if (view.intent.hasExtra("placemark_edit")) {
      edit = true
      placemark = view.intent.extras?.getParcelable<PlacemarkModel>("placemark_edit")!!
      view.showPlacemark(placemark)
    } else {
      if (checkLocationPermissions(view)) {
        doSetCurrentLocation()
      }
    }
  }

  @SuppressLint("MissingPermission")
  fun doSetCurrentLocation() {
    locationService.lastLocation.addOnSuccessListener {
      locationUpdate(Location(it.latitude, it.longitude))
    }
  }

  @SuppressLint("MissingPermission")
  fun doResartLocationUpdates() {
    var locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult != null && locationResult.locations != null) {
          val l = locationResult.locations.last()
          locationUpdate(Location(l.latitude, l.longitude))
        }
      }
    }
    if (!edit) {
      locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }
  }


  override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    if (isPermissionGranted(requestCode, grantResults)) {
      doSetCurrentLocation()
    } else {
      locationUpdate(defaultLocation)
    }
  }

  fun doConfigureMap(m: GoogleMap) {
    map = m
    locationUpdate(placemark.location)
  }

  fun locationUpdate(location: Location) {
    placemark.location = location
    placemark.location.zoom = 15f
    map?.clear()
    val options = MarkerOptions().title(placemark.title).position(LatLng(placemark.location.lat, placemark.location.lng))
    map?.addMarker(options)
    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placemark.location.lat, placemark.location.lng), placemark.location.zoom))
    view?.showLocation(placemark.location)
  }


  fun doAddOrSave(title: String, description: String) {
    placemark.title = title
    placemark.description = description
    doAsync {
      if (edit) {
        app.placemarks.update(placemark)
      } else {
        app.placemarks.create(placemark)
      }
      uiThread {
        view?.finish()
      }
    }
  }

  fun doCancel() {
    view?.finish()
  }

  fun doDelete() {
    doAsync {
      app.placemarks.delete(placemark)
      uiThread {
        view?.finish()
      }
    }
  }

  fun doSelectImage() {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
    }
  }

  fun doSetLocation() {
    view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(placemark.location.lat, placemark.location.lng, placemark.location.zoom))
  }

  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      IMAGE_REQUEST -> {
        placemark.image = data.data.toString()
        view?.showPlacemark(placemark)
      }
      LOCATION_REQUEST -> {
        val location = data.extras?.getParcelable<Location>("location")!!
        placemark.location = location
        locationUpdate(location)
      }
    }
  }
}