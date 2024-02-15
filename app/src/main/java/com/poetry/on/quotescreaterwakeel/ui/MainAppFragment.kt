package com.poetry.on.quotescreaterwakeel.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.poetry.on.quotescreaterwakeel.R
import com.poetry.on.quotescreaterwakeel.ads_manager.AdsManager
import com.poetry.on.quotescreaterwakeel.ads_manager.interfaces.NativeListener
import com.poetry.on.quotescreaterwakeel.ads_manager.loadTwoInterAds
import com.poetry.on.quotescreaterwakeel.databinding.FragmentMainMenuActivityBinding
import com.poetry.on.quotescreaterwakeel.utilities.DbHelper
import com.poetry.on.quotescreaterwakeel.utilities.firebaseAnalytics
import com.poetry.on.quotescreaterwakeel.utilities.id_inter_main_medium
import com.poetry.on.quotescreaterwakeel.utilities.id_native_screen
import com.poetry.on.quotescreaterwakeel.utilities.loadImage
import com.poetry.on.quotescreaterwakeel.utilities.moreApp
import com.poetry.on.quotescreaterwakeel.utilities.privacyPolicy
import com.poetry.on.quotescreaterwakeel.utilities.setupBackPressedCallback
import com.poetry.on.quotescreaterwakeel.utilities.shareApp
import com.poetry.on.quotescreaterwakeel.utilities.showRatingDialog
import com.poetry.on.quotescreaterwakeel.utilities.val_ad_native_main_menu_screen
import com.poetry.on.quotescreaterwakeel.utilities.val_inter_main_medium

class MainAppFragment : Fragment() {

    private var sharedPrefUtils: DbHelper? = null
    private var _binding: FragmentMainMenuActivityBinding? = null
    private var adsManager: AdsManager? = null
    private var isActivated = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMainMenuActivityBinding.inflate(layoutInflater)
        Log.d("calling", "onCreateView: load main fragment")
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics("main_menu_fragment_open", "main_menu_fragment_open -->  Click")
        sharedPrefUtils = DbHelper(context ?: return)
        adsManager = AdsManager.appAdsInit(activity ?: return)
        loadInstertital()
        setupBackPressedCallback {

        }
        _binding?.mainLayout?.topLay?.navMenu?.loadImage(
            context ?: return,
            R.drawable.back_btn
        )


        _binding?.mainLayout?.topLay?.navMenu?.setOnClickListener {
            _binding?.drawerLayout?.openDrawer(GravityCompat.START)

        }
        _binding?.navView?.navigationMain?.setOnClickListener { }
        _binding?.navView?.languageView?.setOnClickListener {
            _binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            findNavController().navigate(R.id.LanguageFragment)
        }
        _binding?.navView?.rateUsView?.setOnClickListener {
            _binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            showRatingDialog(activity, onPositiveButtonClick = { it, _dialog ->
            })
        }
        _binding?.navView?.shareAppView?.setOnClickListener {
            requireContext().shareApp()
            _binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }
        _binding?.navView?.privacyView?.setOnClickListener {
            requireContext().privacyPolicy()
            _binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }
        _binding?.navView?.moreAppView?.setOnClickListener {
            requireContext().moreApp()
            _binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }
        loadNative()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    private fun loadInstertital() {
        loadTwoInterAds(
            ads = adsManager ?: return,
            activity = activity ?: return,
            remoteConfigMedium = val_inter_main_medium,
            remoteConfigNormal = val_inter_main_medium,
            adIdMedium = id_inter_main_medium,
            adIdNormal = id_inter_main_medium,
            tagClass = "main_app_fragment"
        )
    }

    private fun loadNative() {
        adsManager?.nativeAds()?.loadNativeAd(activity ?: return,
            val_ad_native_main_menu_screen,
            id_native_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    if (!isAdded && !isVisible && isDetached) {
                        return
                    }
                    _binding?.mainLayout?.nativeExitAd?.visibility = View.VISIBLE
                    _binding?.mainLayout?.adView?.visibility = View.GONE
                    val adView =
                        layoutInflater.inflate(R.layout.ad_unified_media, null) as NativeAdView
                    adsManager?.nativeAds()?.nativeViewMedia(currentNativeAd ?: return, adView)
                    _binding?.mainLayout?.nativeExitAd?.removeAllViews()
                    _binding?.mainLayout?.nativeExitAd?.addView(adView)
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.mainLayout?.nativeExitAd?.visibility = View.GONE
                    _binding?.mainLayout?.adView?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.mainLayout?.nativeExitAd?.visibility = View.GONE
                    _binding?.mainLayout?.adView?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }
            })

    }

}