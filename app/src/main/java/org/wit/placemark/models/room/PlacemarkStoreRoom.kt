package org.wit.placemark.room

import android.content.Context
import androidx.room.Room
import org.wit.placemark.models.PlacemarkModel
import org.wit.placemark.models.PlacemarkStore

class PlacemarkStoreRoom(val context: Context) : PlacemarkStore {

  var dao: PlacemarkDao

  init {
    val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
        .fallbackToDestructiveMigration()
        .build()
    dao = database.placemarkDao()
  }

  override fun findAll(): List<PlacemarkModel> {
    return dao.findAll()
  }

  override fun findById(id: Long): PlacemarkModel? {
    return dao.findById(id)
  }

  override fun create(placemark: PlacemarkModel) {
    dao.create(placemark)
  }

  override fun update(placemark: PlacemarkModel) {
    dao.update(placemark)
  }

  override fun delete(placemark: PlacemarkModel) {
    dao.deletePlacemark(placemark)
  }
}