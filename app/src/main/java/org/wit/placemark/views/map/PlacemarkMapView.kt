package org.wit.placemark.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import org.wit.placemark.R
import kotlinx.android.synthetic.main.activity_placemark_map.*
import kotlinx.android.synthetic.main.content_placemark_maps.*
import org.wit.placemark.helpers.readImageFromPath
import org.wit.placemark.models.PlacemarkModel

class PlacemarkMapView : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

  lateinit var presenter: PlacemarkMapPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark_map)
    setSupportActionBar(toolbar)
    presenter = PlacemarkMapPresenter(this)

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      presenter.doPopulateMap(it)
    }
  }

  fun showPlacemark(placemark: PlacemarkModel) {
    currentTitle.text = placemark.title
    currentDescription.text = placemark.description
    currentImage.setImageBitmap(readImageFromPath(this, placemark.image))
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doMarkerSelected(marker)
    return true
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}
