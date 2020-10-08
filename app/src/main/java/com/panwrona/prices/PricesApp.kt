package com.panwrona.prices

import android.app.Application
import com.panwrona.prices.repository.PricesRepository
import com.panwrona.prices.repository.PricesRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PricesApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PricesApp)
            modules(appModule)
        }

    }

    private val appModule = module {
        single<PricesRepository> { PricesRepositoryImpl()}
    }
}