package com.exoapps.pickuplines.flirtymessages.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.exoapps.pickuplines.flirtymessages.R
import com.exoapps.pickuplines.flirtymessages.adapter.GridAdapter
import com.exoapps.pickuplines.flirtymessages.ads_manager.AdsManager
import com.exoapps.pickuplines.flirtymessages.ads_manager.billing.PurchasePrefs
import com.exoapps.pickuplines.flirtymessages.ads_manager.interfaces.NativeListener
import com.exoapps.pickuplines.flirtymessages.ads_manager.loadTwoInterAds
import com.exoapps.pickuplines.flirtymessages.ads_manager.showTwoInterAd
import com.exoapps.pickuplines.flirtymessages.databinding.FragmentMainMenuActivityBinding
import com.exoapps.pickuplines.flirtymessages.model.Item
import com.exoapps.pickuplines.flirtymessages.utilities.exitDialog
import com.exoapps.pickuplines.flirtymessages.utilities.firebaseAnalytics
import com.exoapps.pickuplines.flirtymessages.utilities.id_inter_main_medium
import com.exoapps.pickuplines.flirtymessages.utilities.id_native_screen
import com.exoapps.pickuplines.flirtymessages.utilities.loadImage
import com.exoapps.pickuplines.flirtymessages.utilities.moreApp
import com.exoapps.pickuplines.flirtymessages.utilities.privacyPolicy
import com.exoapps.pickuplines.flirtymessages.utilities.setupBackPressedCallback
import com.exoapps.pickuplines.flirtymessages.utilities.shareApp
import com.exoapps.pickuplines.flirtymessages.utilities.showRatingDialog
import com.exoapps.pickuplines.flirtymessages.utilities.val_ad_native_main_menu_screen
import com.exoapps.pickuplines.flirtymessages.utilities.val_inter_main_medium

class MainAppFragment : Fragment() {

    private var sharedPrefUtils: PurchasePrefs? = null
    private var _binding: FragmentMainMenuActivityBinding? = null
    private var adsManager: AdsManager? = null
    private var adapterG: GridAdapter? = null

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
        sharedPrefUtils = PurchasePrefs(context ?: return)
        adsManager = AdsManager.appAdsInit(activity ?: return)
        loadInstertital()
        setupBackPressedCallback {
            exitDialog(context = activity?:return@setupBackPressedCallback)
        }
        _binding?.mainLayout?.topLay?.navMenu?.loadImage(
            context ?: return,
            R.drawable.back_btn
        )


        Glide.with(this).load(R.drawable.nave_menu)
            .into(_binding?.mainLayout?.topLay?.navMenu ?: return)
        val items = listOf(
            Item(R.drawable.image_7, getString(R.string.b_w_t), "Birthday"),
            Item(R.drawable.image_4, getString(R.string.f_q), "Funny quotes"),
            Item(R.drawable.image_8, getString(R.string.i_q), "Inspirational"),
            Item(R.drawable.image_6, getString(R.string.l_q), "Life"),
            Item(R.drawable.image_9, getString(R.string.s_ll), "Love"),
            Item(R.drawable.image_5, getString(R.string.p_q), "Positive"),
            Item(R.drawable.image_10, getString(R.string.s_s), "Sorry"),
            Item(R.drawable.image_3, getString(R.string.s_q), "Success"),
            Item(R.drawable.image_1, getString(R.string.m_q), "Motivational"),
            Item(R.drawable.image_1, getString(R.string.f_q_q), "Freedom Quotes"),
            Item(R.drawable.image_1, getString(R.string.f_f_q), "Friendship Quotes"),
            Item(R.drawable.image_1, getString(R.string.h_q), "Hope Quotes"),
            Item(R.drawable.image_2, getString(R.string.a_q), "Attitude Quotes"),
        )

        _binding?.navView?.topView?.setOnClickListener { }
        _binding?.mainLayout?.recyclerViewGrid?.apply {
            adapterG = GridAdapter(items)
            adapter = adapterG
            adapterG?.onClick = {
                adsManager?.let {
                    showTwoInterAd(
                        ads = it,
                        activity = activity ?: return@let,
                        remoteConfigMedium = val_inter_main_medium,
                        remoteConfigNormal = val_inter_main_medium,
                        adIdMedium = id_inter_main_medium,
                        adIdNormal = id_inter_main_medium,
                        tagClass = "main_menu_click",
                        isBackPress = true,
                        layout = _binding?.mainLayout?.adsLay!!
                    ) {
                    }
                }
                val selectedItem = items[it].file
                findNavController().navigate(R.id.DetailActivity, bundleOf("item" to selectedItem))
            }
        }
        _binding?.mainLayout?.topLay?.settingBtn?.setOnClickListener {
            findNavController().navigate(R.id.FragmentPro)
        }
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
        _binding?.navView?.buyBtn?.setOnClickListener {
            _binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            findNavController().navigate(R.id.FragmentPro)
        }
        _binding?.navView?.exitView?.setOnClickListener {
            exitDialog(context = activity?:return@setOnClickListener)
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
                        layoutInflater.inflate(R.layout.ad_unified_privacy, null) as NativeAdView
                    adsManager?.nativeAds()?.nativeViewPolicy(currentNativeAd ?: return, adView)
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