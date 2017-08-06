package org.swecha.freedombox

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import android.os.Looper.loop
import android.support.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    internal var intents = HashMap<Int, List<Intent?>>()

    fun getLaunchIntent(packageNames: List<String>): List<Intent> {
        return packageNames.map { packageName ->
            this.packageManager.getLaunchIntentForPackage(packageName)}
    }

    fun getWebIntent(vararg urls: String): List<Intent> {
        return urls.map { url ->  Intent(Intent.ACTION_VIEW, Uri.parse(url))}
    }

    fun launchApp(intents: List<Intent?>) {
        val intent = intents.firstOrNull{ intent -> intent != null }
        if (intent != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "App not installed",
                    Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        intents = hashMapOf(
                R.id.talk to getLaunchIntent(listOf("im.vector.alpha")),
                R.id.internet to getWebIntent("https://www.startpage.com"),
                R.id.library to getWebIntent("http://10.42.0.1:8080"),
                R.id.video to getWebIntent("http://10.42.0.1:8096"),
                R.id.napanta to getLaunchIntent(listOf("com.napanta.farmer.app")),
                R.id.plantix to getLaunchIntent(listOf("com.peat.GartenBank")),
                R.id.audio to getLaunchIntent(listOf("com.sound.ampache",
                        "com.antoniotari.reactiveampacheapp"))
        )
        intents.forEach { id, list -> initButton(id) }
    }

    fun initButton(id: Int) {
        val button = findViewById(id)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                    launchApp(intents[v.id]!!)
                }
            }
        )
    }
}
