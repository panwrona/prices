package com.panwrona.prices.ui

object PresenterProvider {

    private var presenter: MainPresenter? = null

    fun setPresenter(presenter: MainPresenter?) {
        this.presenter = presenter
    }

    fun getPresenter(): MainPresenter? {
        return presenter
    }
}