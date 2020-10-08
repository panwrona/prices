package com.panwrona.prices.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class AppSchedulersFacade: SchedulersFacade() {
    override val io: Scheduler = Schedulers.io()
    override val main: Scheduler = AndroidSchedulers.mainThread()
}