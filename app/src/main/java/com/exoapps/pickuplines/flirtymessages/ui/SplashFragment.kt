package com.exoapps.pickuplines.flirtymessages.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.exoapps.pickuplines.flirtymessages.R
import com.exoapps.pickuplines.flirtymessages.ads_manager.AdsManager
import com.exoapps.pickuplines.flirtymessages.ads_manager.billing.PurchasePrefs
import com.exoapps.pickuplines.flirtymessages.ads_manager.loadTwoInterAdsSplash
import com.exoapps.pickuplines.flirtymessages.ads_manager.showNormalInterAdSingle
import com.exoapps.pickuplines.flirtymessages.databinding.FragmentSplashBinding
import com.exoapps.pickuplines.flirtymessages.utilities.BaseFragment
import com.exoapps.pickuplines.flirtymessages.utilities.IS_FIRST
import com.exoapps.pickuplines.flirtymessages.utilities.LANG_CODE
import com.exoapps.pickuplines.flirtymessages.utilities.LANG_SCREEN
import com.exoapps.pickuplines.flirtymessages.utilities.counter
import com.exoapps.pickuplines.flirtymessages.utilities.firebaseAnalytics
import com.exoapps.pickuplines.flirtymessages.utilities.id_app_open_screen
import com.exoapps.pickuplines.flirtymessages.utilities.id_banner_screen
import com.exoapps.pickuplines.flirtymessages.utilities.id_frequency_counter
import com.exoapps.pickuplines.flirtymessages.utilities.id_inter_counter
import com.exoapps.pickuplines.flirtymessages.utilities.id_inter_main_medium
import com.exoapps.pickuplines.flirtymessages.utilities.id_native_screen
import com.exoapps.pickuplines.flirtymessages.utilities.inter_frequency_count
import com.exoapps.pickuplines.flirtymessages.utilities.isSplash
import com.exoapps.pickuplines.flirtymessages.utilities.setLocaleMain
import com.exoapps.pickuplines.flirtymessages.utilities.setupBackPressedCallback
import com.exoapps.pickuplines.flirtymessages.utilities.val_ad_app_open_screen
import com.exoapps.pickuplines.flirtymessages.utilities.val_ad_inter_loading_screen
import com.exoapps.pickuplines.flirtymessages.utilities.val_ad_native_intro_screen
import com.exoapps.pickuplines.flirtymessages.utilities.val_ad_native_language_screen
import com.exoapps.pickuplines.flirtymessages.utilities.val_ad_native_list_data_screen
import com.exoapps.pickuplines.flirtymessages.utilities.val_ad_native_loading_screen
import com.exoapps.pickuplines.flirtymessages.utilities.val_ad_native_main_menu_screen
import com.exoapps.pickuplines.flirtymessages.utilities.val_inter_back_press
import com.exoapps.pickuplines.flirtymessages.utilities.val_inter_main_medium
import kotlinx.coroutines.delay


class SplashFragment :
    BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private var dbHelper: PurchasePrefs? = null
    private var adsManager: AdsManager? = null
    private var remoteConfig: FirebaseRemoteConfig? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = PurchasePrefs(context ?: return)
        dbHelper?.getStringData(LANG_CODE, "en")?.let { setLocaleMain(it) }

        isSplash = false
        counter = 0
        inter_frequency_count = 0
        adsManager = AdsManager.appAdsInit(activity ?: return)

        loadTwoInterAdsSplash(
            adsManager ?: return,
            activity ?: return,
            remoteConfigNormal = true,
            adIdNormal = getString(R.string.id_fullscreen_splash),
            "splash"
        )

//        initRemoteIds()
//        initRemoteConfig()
//        activity?.finish()
//        startActivity(Intent(activity ?:return,MainMenuFragment::class.java))
        observeSplashLiveData()
        setupBackPressedCallback {
            //Do Nothing
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    private fun observeSplashLiveData() {
        try {

            lifecycleScope.launchWhenCreated {
                try {
                    delay(6000)
                    firebaseAnalytics("splash_fragment_load", "splash_fragment_load -->  Click")
                    adsManager?.let {
                        showNormalInterAdSingle(
                            it,
                            activity ?: return@let,
                            remoteConfigMedium = val_ad_inter_loading_screen,
                            remoteConfigNormal = val_ad_inter_loading_screen,
                            adIdMedium = "",
                            adIdNormal = "",
                            tagClass = "splash"
                        ) {

                        }
                    }
                    if (dbHelper?.getBoolean(
                            IS_FIRST
                        ) == false
                    ) {
                        firebaseAnalytics(
                            "loading_fragment_load_next_btn_language",
                            "loading_fragment_load_next_btn_language -->  Click"
                        )
                        findNavController().navigate(
                            R.id.LanguageFragment,
                            bundleOf(LANG_SCREEN to true)
                        )
                    } else {
                        firebaseAnalytics(
                            "loading_fragment_load_next_btn_main",
                            "loading_fragment_load_next_btn_main -->  Click"
                        )
//                        activity?.finish()
//                        startActivity(Intent(activity ?: return@launchWhenCreated, MainMenuFragment::class.java))
                        findNavController().navigate(R.id.myMainMenuFragment)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initRemoteIds() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600) // Set the minimum interval for fetching, in seconds
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
// Fetch the remote config values
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(activity ?: return) { task ->
                if (task.isSuccessful) {
                    // Apply the fetched values to your app
                    applyAdIdsFromRemoteConfig(remoteConfig)
                } else {
                    // Handle the error
                    // For example, use default values or log an error message
                }
            }
    }

    private fun applyAdIdsFromRemoteConfig(remoteConfig: FirebaseRemoteConfig) {

        id_inter_counter = remoteConfig.getLong("id_inter_counter").toInt()
        id_frequency_counter = remoteConfig.getLong("id_frequency_counter").toInt()
        id_inter_main_medium = remoteConfig.getString("id_inter_main_medium")
        id_native_screen = remoteConfig.getString("id_native_screen")
        id_app_open_screen = remoteConfig.getString("id_app_open_screen")
        id_banner_screen = remoteConfig.getString("id_banner_screen")

        Log.d("remote_ids", "$id_inter_counter")
        Log.d("remote_ids", "$id_frequency_counter")
        Log.d("remote_ids", id_inter_main_medium)
        Log.d("remote_ids", id_native_screen)
        Log.d("remote_ids", id_app_open_screen)
        Log.d("remote_ids", id_banner_screen)

    }

    private fun initRemoteConfig() {

        try {
            FirebaseApp.initializeApp(context ?: return)
            remoteConfig = Firebase.remoteConfig
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 6
            }
            remoteConfig?.setConfigSettingsAsync(configSettings)
            remoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)
            remoteConfig?.fetchAndActivate()?.addOnCompleteListener(activity ?: return) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("RemoteConfig", "Config params updated: $updated")
                } else {
                    Log.d("RemoteConfig", "Fetch failed")
                }
                val_inter_back_press = remoteConfig!!["val_inter_back_press"].asBoolean()
                val_inter_main_medium = remoteConfig!!["val_inter_main_medium"].asBoolean()
                val_ad_native_main_menu_screen =
                    remoteConfig!!["val_ad_native_main_menu_screen"].asBoolean()
                val_ad_native_loading_screen =
                    remoteConfig!!["val_native_loading_screen"].asBoolean()
                val_ad_native_intro_screen =
                    remoteConfig!!["val_ad_native_intro_screen"].asBoolean()
                val_ad_native_language_screen =
                    remoteConfig!!["val_ad_native_language_screen"].asBoolean()
                val_ad_app_open_screen = remoteConfig!!["val_ad_app_open_screen"].asBoolean()
                val_ad_native_list_data_screen =
                    remoteConfig!!["val_ad_native_list_data_screen"].asBoolean()


                Log.d(
                    "RemoteConfig",
                    "Fetch val_ad_native_list_data_screen -> $val_ad_native_list_data_screen"
                )
                Log.d(
                    "RemoteConfig",
                    "Fetch val_inter_back_press -> $val_inter_back_press"
                )
                Log.d(
                    "RemoteConfig",
                    "Fetch val_inter_back_press -> $val_inter_back_press"
                )
                Log.d(
                    "RemoteConfig",
                    "Fetch val_inter_main_medium -> $val_inter_main_medium"
                )
                Log.d(
                    "RemoteConfig",
                    "Fetch val_ad_native_loading_screen -> $val_ad_native_loading_screen"
                )
                Log.d(
                    "RemoteConfig",
                    "Fetch val_ad_native_main_menu_screen -> $val_ad_native_main_menu_screen"
                )
                Log.d(
                    "RemoteConfig",
                    "Fetch val_ad_native_intro_screen -> $val_ad_native_intro_screen"
                )
                Log.d(
                    "RemoteConfig",
                    "Fetch val_ad_native_language_screen -> $val_ad_native_language_screen"
                )
                Log.d(
                    "RemoteConfig",
                    "Fetch val_ad_app_open_screen -> $val_ad_app_open_screen"
                )


            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
