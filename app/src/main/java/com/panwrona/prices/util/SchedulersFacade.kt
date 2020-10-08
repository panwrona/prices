package com.panwrona.prices.util

import io.reactivex.rxjava3.core.Scheduler

abstract class SchedulersFacade {

    abstract val io: Scheduler
    abstract val main: Scheduler
}