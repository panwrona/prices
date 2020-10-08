package com.panwrona.prices.ui

import com.panwrona.prices.entity.Direction
import com.panwrona.prices.entity.Price
import com.panwrona.prices.repository.PricesRepository
import com.panwrona.prices.util.SchedulersFacade
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class MainPresenter(
    private val pricesRepository: PricesRepository,
    schedulers: SchedulersFacade
) {

    private var disposable: Disposable? = null
    private val clicks = BehaviorSubject.create<String>()
    private var view: MainView? = null

    private val observable: Observable<Price>

    init {
        observable = clicks.switchMap {
            pricesRepository.prices(it).toObservable().scan { old, new ->
                when {
                    old.value < new.value -> new.copy(direction = Direction.UP)
                    old.value > new.value -> new.copy(direction = Direction.DOWN)
                    else -> new
                }
            }
        }
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .replay(1)
            .autoConnect()
            .doOnNext {
                view?.updatePrice(it)
            }
    }

    fun startObserving() {
        disposable = observable.subscribe()
    }

    fun stopObserving() {
        disposable?.dispose()
    }

    fun attachView(view: MainView) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    fun changeMarket(market: String) {
        clicks.onNext(market)
    }

    fun destroy() {
        disposable?.dispose()
    }
}