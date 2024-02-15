package com.poetry.on.quotescreaterwakeel.ui

import android.content.Intent
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
import com.poetry.on.quotescreaterwakeel.R
import com.poetry.on.quotescreaterwakeel.ads_manager.AdsManager
import com.poetry.on.quotescreaterwakeel.ads_manager.loadTwoInterAdsSplash
import com.poetry.on.quotescreaterwakeel.ads_manager.showNormalInterAdSingle
import com.poetry.on.quotescreaterwakeel.databinding.FragmentSplashBinding
import com.poetry.on.quotescreaterwakeel.utilities.BaseFragment
import com.poetry.on.quotescreaterwakeel.utilities.DbHelper
import com.poetry.on.quotescreaterwakeel.utilities.IS_FIRST
import com.poetry.on.quotescreaterwakeel.utilities.LANG_CODE
import com.poetry.on.quotescreaterwakeel.utilities.LANG_SCREEN
import com.poetry.on.quotescreaterwakeel.utilities.counter
import com.poetry.on.quotescreaterwakeel.utilities.firebaseAnalytics
import com.poetry.on.quotescreaterwakeel.utilities.id_app_open_screen
import com.poetry.on.quotescreaterwakeel.utilities.id_banner_screen
import com.poetry.on.quotescreaterwakeel.utilities.id_frequency_counter
import com.poetry.on.quotescreaterwakeel.utilities.id_inter_counter
import com.poetry.on.quotescreaterwakeel.utilities.id_inter_main_medium
import com.poetry.on.quotescreaterwakeel.utilities.id_native_screen
import com.poetry.on.quotescreaterwakeel.utilities.inter_frequency_count
import com.poetry.on.quotescreaterwakeel.utilities.isSplash
import com.poetry.on.quotescreaterwakeel.utilities.setLocaleMain
import com.poetry.on.quotescreaterwakeel.utilities.setupBackPressedCallback
import com.poetry.on.quotescreaterwakeel.utilities.val_ad_app_open_screen
import com.poetry.on.quotescreaterwakeel.utilities.val_ad_inter_loading_screen
import com.poetry.on.quotescreaterwakeel.utilities.val_ad_native_intro_screen
import com.poetry.on.quotescreaterwakeel.utilities.val_ad_native_language_screen
import com.poetry.on.quotescreaterwakeel.utilities.val_ad_native_list_data_screen
import com.poetry.on.quotescreaterwakeel.utilities.val_ad_native_loading_screen
import com.poetry.on.quotescreaterwakeel.utilities.val_ad_native_main_menu_screen
import com.poetry.on.quotescreaterwakeel.utilities.val_inter_back_press
import com.poetry.on.quotescreaterwakeel.utilities.val_inter_main_medium
import kotlinx.coroutines.delay


class SplashFragment :
    BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private var dbHelper: DbHelper? = null
    private var adsManager: AdsManager? = null
    private var remoteConfig: FirebaseRemoteConfig? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isSplash = false
        counter = 0
        inter_frequency_count = 0
        adsManager = AdsManager.appAdsInit(activity ?: return)
        dbHelper = DbHelper(context ?: return)
        dbHelper?.getStringData(requireContext(), LANG_CODE, "en")?.let { setLocaleMain(it) }

        loadTwoInterAdsSplash(
            adsManager ?: return,
            activity ?: return,
            remoteConfigNormal = true,
            adIdNormal = getString(R.string.id_fullscreen_splash),
            "splash"
        )

//        initRemoteIds()
//        initRemoteConfig()
        activity?.finish()
        startActivity(Intent(activity ?:return,MainMenuActivity::class.java))
//        observeSplashLiveData()
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
                    if (dbHelper?.getBooleanData(
                            context ?: return@launchWhenCreated,
                            IS_FIRST,
                            false
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
                        activity?.finish()
                        startActivity(Intent(activity ?: return@launchWhenCreated, MainMenuActivity::class.java))
//                        findNavController().navigate(R.id.myMainMenuFragment)
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
