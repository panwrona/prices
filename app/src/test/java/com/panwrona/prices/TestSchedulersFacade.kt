package com.panwrona.prices

import com.panwrona.prices.util.SchedulersFacade
import io.reactivex.rxjava3.schedulers.TestScheduler

class TestSchedulersFacade(scheduler: TestScheduler): SchedulersFacade() {
    override val io = scheduler
    override val main = scheduler
}