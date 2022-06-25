package pe.idat.proyecto.pizzeria.activities.commom

import android.app.Application

class MiApp : Application() {

    companion object {
        lateinit var instance: MiApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}