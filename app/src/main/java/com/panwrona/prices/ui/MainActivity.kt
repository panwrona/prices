package com.panwrona.prices.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.panwrona.prices.R
import com.panwrona.prices.entity.Direction
import com.panwrona.prices.entity.Price
import com.panwrona.prices.repository.PricesRepository
import com.panwrona.prices.util.AppSchedulersFacade
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), MainView {

    private var presenter: MainPresenter? = null
    private val repository: PricesRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = if(PresenterProvider.getPresenter() == null) {
            MainPresenter(repository, AppSchedulersFacade())
        } else {
            PresenterProvider.getPresenter()
        }
        presenter?.attachView(this)
        btnEUR.setOnClickListener {
            presenter?.changeMarket("EUR")
        }
        btnGOLD.setOnClickListener {
            presenter?.changeMarket("GOLD")
        }
        btnPLN.setOnClickListener {
            presenter?.changeMarket("PLN")
        }
    }

    override fun onStart() {
        super.onStart()
        presenter?.startObserving()
    }

    override fun onStop() {
        super.onStop()
        presenter?.stopObserving()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
        presenter?.destroy()
        PresenterProvider.setPresenter(presenter)
        presenter = null
    }

    override fun updatePrice(price: Price) {
        when(price.direction) {
            Direction.DOWN -> txtPrice.setTextColor(Color.RED)
            Direction.UP -> txtPrice.setTextColor(Color.GREEN)
            Direction.UNKNOWN -> txtPrice.setTextColor(Color.BLACK)
        }
        txtPrice.text = "Market: ${price.marketName}\nprice: ${price.value}"
    }
}