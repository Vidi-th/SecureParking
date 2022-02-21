package com.protel.secureparking

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.protel.secureparking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.vehicle.setOnClickListener{
            val intent = Intent(this,VehicleActivity::class.java)
            startActivity(intent)
        }
        binding.location.setOnClickListener{
            val intent = Intent(this, GateActivity::class.java)
            startActivity(intent)
        }
        findViewById<View>(R.id.notif).setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel)
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "n")
                .setContentTitle("Kendaraan Anda Keluar dari Tempat Parkir")
                .setContentText("Tap untuk informasi lebih lanjut")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)

            val managerCompat = NotificationManagerCompat.from(this)

            /**
             * -------- this is click event for notification to open application -----------
             */
            val contentIntent = PendingIntent.getActivity(
                this, 0,
                Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.setContentIntent(contentIntent)
            /**
             * ----------------------------------------------------------------------------------
             */
            managerCompat.notify(999, builder.build())
        }
    }
}