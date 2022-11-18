package br.gturcheti.wscars.ui.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import br.gturcheti.wscars.PushLeadService
import br.gturcheti.wscars.R
import br.gturcheti.wscars.services.dto.CarsDTO
import br.gturcheti.wscars.services.wsApi
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    val SERVICE_TASK_TAG = "PushLeadService"
    override fun onStart() { //ESSE CÓDIGO DE ALARM INFELIZMENTE NÃO ESTÁ STARTANDO O SERVIÇO DE PUSH DOS LEADS
        super.onStart()
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val alarmIntent = PendingIntent.getService(this, 1000, intent, PendingIntent.FLAG_IMMUTABLE )
        Log.d(SERVICE_TASK_TAG, "onCreate: $alarmIntent ")
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 17)
            set(Calendar.MINUTE, 26)
        }
        Log.d(SERVICE_TASK_TAG, "onCreate: $calendar ")

        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }
}