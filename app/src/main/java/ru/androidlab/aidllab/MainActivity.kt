package ru.androidlab.aidllab

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Process.myPid
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivityTAG"
    }

    private var remoteService: IRemoteService? = null
    private var connected: Boolean = false
    private val textView by lazy { findViewById<TextView>(R.id.tv) }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d(TAG, "IRemoteService onServiceConnected")

            remoteService = IRemoteService.Stub.asInterface(binder)
            connected = true
            textView.text = "connected ${remoteService?.pid}"
            Log.d(TAG, "currentPid: ${myPid()} and remotePid:" + remoteService?.getPid())
            remoteService?.basicTypes(1, 5L, true, 0.5F, 0.8, "asd")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "IRemoteService onServiceDisconnected")

            textView.text = "disconnected"
            remoteService = null
            connected = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        Intent(this, RemoteService::class.java).run {
            bindService(this, serviceConnection, Service.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()

        unbindService(serviceConnection)
    }
}