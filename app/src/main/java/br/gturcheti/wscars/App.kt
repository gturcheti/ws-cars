package br.gturcheti.wscars

import android.app.Application
import br.gturcheti.wscars.data.model.AppDatabase

class App: Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDatabase(this)
    }
}