package com.kej.mediaplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class LowBatteryBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("onReceive", "onReceive")
    }
}