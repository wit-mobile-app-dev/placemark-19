package org.wit.placemark.views.placemarklist

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.placemark.activities.PlacemarkMapsActivity
import org.wit.placemark.views.placemark.PlacemarkView
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel

class PlacemarkListPresenter(val view: PlacemarkListView) {

  var app: MainApp

  init {
    app = view.application as MainApp
  }

  fun getPlacemarks() = app.placemarks.findAll()

  fun doAddPlacemark() {
    view.startActivityForResult<PlacemarkView>(0)
  }

  fun doEditPlacemark(placemark: PlacemarkModel) {
    view.startActivityForResult(view.intentFor<PlacemarkView>().putExtra("placemark_edit", placemark), 0)
  }

  fun doShowPlacemarksMap() {
    view.startActivity<PlacemarkMapsActivity>()
  }
}