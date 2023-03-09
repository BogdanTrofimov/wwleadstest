package com.example.wwleadstest


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wwleadstest.databinding.ActivitySplashScreenBinding
import com.example.wwleadstest.utils.Constatns.GAME_PASS
import com.example.wwleadstest.utils.Constatns.URL_KEY
import com.example.wwleadstest.utils.Constatns.WEB_LINK
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings


class SplashScreen : AppCompatActivity() {


    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private var isFirstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRemote()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initConfig()
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstLoad) {
            initView(
                isLoadGame = remoteConfig.getBoolean(GAME_PASS),
                url = remoteConfig.getString(WEB_LINK)
            )
        }
    }

    private fun initRemote() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0

        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.rc_defaults)
    }

    private fun initConfig() {
        if (isNetworkConnected(this)) {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        isFirstLoad = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            initView(
                                isLoadGame = remoteConfig.getBoolean(GAME_PASS),
                                url = remoteConfig.getString(WEB_LINK)
                            )
                        }, 1500)
                    } else {
                        Toast.makeText(this, getString(R.string.fetch_error), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        } else {
            loadToast()
            Handler(Looper.getMainLooper()).postDelayed({
                loadGame()
            }, 1500)
        }
    }

    fun initView(isLoadGame: Boolean, url: String?) {
        if (isLoadGame) {
            loadGame()
        } else {
            loadWebView(url)
        }
    }

    private fun loadGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    private fun loadToast() {
        Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
    }

    private fun loadWebView(url: String?) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(URL_KEY, url)
        startActivity(intent)
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }
}
