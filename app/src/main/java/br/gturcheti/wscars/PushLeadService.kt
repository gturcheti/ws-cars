package br.gturcheti.wscars

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import br.gturcheti.wscars.data.model.Lead
import br.gturcheti.wscars.data.model.LeadDao
import br.gturcheti.wscars.services.wsApi
import kotlinx.coroutines.*
import retrofit2.Response

class PushLeadService : Service() {

    val SERVICE_TASK_TAG = "PushLeadService"
    private lateinit var leads: List<Lead>

    override fun onCreate() {
        super.onCreate()
        Log.d(SERVICE_TASK_TAG, "onStartCommand: CREATED")
        val serviceJob = Job()
        val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

        serviceScope.launch {
            dao().getAllLeads().collect { result ->
                leads = result.toList()
                sendLeads(leads)
                Log.i("PUSHSERVICE", "onStartCommand: LIST $leads")
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    suspend fun sendLeads(leads: List<Lead>) {
        try {
            wsApi.sendLeads(leads).let { response ->
                Log.d(SERVICE_TASK_TAG, "sendLeads: $response")
            }
        } catch (e: Exception) {
            Log.d(SERVICE_TASK_TAG, "sendLeads Exception: $e")
        }
    }

    private fun dao(): LeadDao {
        val app = applicationContext as App
        return app.db.leadDao()
    }
}