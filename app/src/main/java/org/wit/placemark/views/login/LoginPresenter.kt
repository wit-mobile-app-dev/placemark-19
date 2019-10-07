package org.wit.placemark.views.login

import org.wit.placemark.views.BasePresenter
import org.wit.placemark.views.BaseView
import org.wit.placemark.views.VIEW

class LoginPresenter(view: BaseView) : BasePresenter(view) {

  fun doLogin(email: String, password: String) {
    view?.navigateTo(VIEW.LIST)
  }

  fun doSignUp(email: String, password: String) {
    view?.navigateTo(VIEW.LIST)
  }
}