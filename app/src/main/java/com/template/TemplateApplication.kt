package com.template

import android.app.Application
import dev.zacsweers.metro.createGraphFactory
import com.template.di.AppGraph
import timber.log.Timber

class TemplateApplication : Application() {
    lateinit var appGraph: AppGraph
        private set

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        appGraph = createGraphFactory<AppGraph.Factory>().create(this)
    }
}
