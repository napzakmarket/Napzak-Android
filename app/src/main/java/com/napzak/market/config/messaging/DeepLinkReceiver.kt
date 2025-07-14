package com.napzak.market.config.messaging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.napzak.market.main.MainActivity

class DeepLinkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val uriString = intent?.getStringExtra("deep_link_uri")
        val uri = uriString?.let { Uri.parse(it) }

        if (uri != null) {
            val activityIntent = Intent(context, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                data = uri
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            context.startActivity(activityIntent)
        }
    }
}
