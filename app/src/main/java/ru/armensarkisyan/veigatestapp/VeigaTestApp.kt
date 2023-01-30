package ru.armensarkisyan.veigatestapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.armensarkisyan.veigatestapp.common.di.appModule
import ru.armensarkisyan.veigatestapp.common.di.viewModelsModule


class VeigaTestApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger(level = Level.ERROR)
            androidContext(this@VeigaTestApp)
            modules(listOf(appModule, viewModelsModule))
        }

    }
}