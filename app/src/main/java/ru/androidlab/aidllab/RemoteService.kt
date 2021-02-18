package ru.androidlab.aidllab

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.util.Log

class RemoteService : Service() {

    companion object {
        private const val TAG = "RemoteServiceTAG"
    }

    private val binder = object : IRemoteService.Stub() {
        override fun getPid(): Int {
            sayHelloFromCurrentProcessAndThread(TAG)
            return Process.myPid()
        }

        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String
        ) {
            Log.d(TAG, "startProcessing")
            Thread.sleep(1000) // to show how the oneway keyword works
            Log.d(TAG, "endProcessing")
        }
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate")
    }

    override fun onBind(intent: Intent): IBinder {
        // Return the interface
        return binder
    }

}