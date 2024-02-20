package com.poetry.on.quotescreaterwakeel.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.poetry.on.quotescreaterwakeel.R
import com.poetry.on.quotescreaterwakeel.adapter.AppLanguageAdapter
import com.poetry.on.quotescreaterwakeel.ads_manager.AdsManager
import com.poetry.on.quotescreaterwakeel.ads_manager.billing.PurchasePrefs
import com.poetry.on.quotescreaterwakeel.ads_manager.interfaces.NativeListener
import com.poetry.on.quotescreaterwakeel.databinding.FragmentLanguageBinding
import com.poetry.on.quotescreaterwakeel.utilities.BaseFragment
import com.poetry.on.quotescreaterwakeel.utilities.IS_FIRST
import com.poetry.on.quotescreaterwakeel.utilities.LANG_CODE
import com.poetry.on.quotescreaterwakeel.utilities.LANG_SCREEN
import com.poetry.on.quotescreaterwakeel.utilities.clickWithThrottle
import com.poetry.on.quotescreaterwakeel.utilities.firebaseAnalytics
import com.poetry.on.quotescreaterwakeel.utilities.id_native_screen
import com.poetry.on.quotescreaterwakeel.utilities.languageData
import com.poetry.on.quotescreaterwakeel.utilities.setLocaleMain
import com.poetry.on.quotescreaterwakeel.utilities.setupBackPressedCallback
import com.poetry.on.quotescreaterwakeel.utilities.val_ad_native_language_screen


class LanguageFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {

    private var positionSelected: String = "en"
    private var isLangScreen: Boolean = false
    private var adsManager: AdsManager? = null
    private var sharedPrefUtils: PurchasePrefs? = null
    private var adapter: AppLanguageAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adsManager = AdsManager.appAdsInit(activity ?: return)
        firebaseAnalytics("language_fragment_open", "language_fragment_open -->  Click")
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        arguments?.let {
            isLangScreen = it.getBoolean(LANG_SCREEN)
        }

        if (isLangScreen) {
            _binding?.backBtn?.visibility = View.GONE
        }
        _binding?.forwardBtn?.clickWithThrottle {
            sharedPrefUtils?.putBoolean(IS_FIRST, true)
            if (!isLangScreen) {
                firebaseAnalytics(
                    "language_fragment_forward_btn_from",
                    "language_fragment_forward_btn_from -->  Click"
                )
                sharedPrefUtils?.putStringLang(LANG_CODE, positionSelected) ?: "en"
                setLocaleMain(positionSelected)
                findNavController().navigateUp()
            } else {
                firebaseAnalytics(
                    "language_fragment_forward_btn_from",
                    "language_fragment_forward_btn_from -->  Click"
                )
                sharedPrefUtils?.putStringLang(LANG_CODE, positionSelected) ?: "en"
                setLocaleMain(positionSelected)
                sharedPrefUtils?.putBoolean(IS_FIRST, true)
                findNavController().navigate(R.id.myMainMenuFragment)
            }
        }

        sharedPrefUtils = PurchasePrefs(context ?: return)
        positionSelected = sharedPrefUtils?.getStringLang(LANG_CODE) ?: "en"
        adapter = AppLanguageAdapter(clickItem = {
            positionSelected = it.country_code
        }, languageData = languageData())
        adapter?.selectLanguage(positionSelected)

        _binding?.title?.text = getString(R.string.set_app_language)
        _binding?.conversationDetail?.adapter = adapter
        _binding?.backBtn?.clickWithThrottle {
            if (!isLangScreen) {
                firebaseAnalytics(
                    "language_fragment_back_press",
                    "language_fragment_back_press -->  Click"
                )
                findNavController().popBackStack()
            }
        }
        setupBackPressedCallback {
            if (!isLangScreen) {
                firebaseAnalytics(
                    "language_fragment_back_press",
                    "language_fragment_back_press -->  Click"
                )
                findNavController().popBackStack()
            }
        }
        loadNative()
    }

    private fun loadNative() {
        adsManager?.nativeAds()?.loadNativeAd(
            activity ?: return,
            val_ad_native_language_screen,
            id_native_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    if (!isAdded && !isVisible && isDetached) {
                        return
                    }
                    _binding?.nativeExitAd?.visibility = View.VISIBLE
                    _binding?.adView?.visibility = View.GONE
                    if (isAdded && isVisible && !isDetached) {
                        val adView = layoutInflater.inflate(
                            R.layout.ad_unified_privacy,
                            null
                        ) as NativeAdView
                        adsManager?.nativeAds()?.nativeViewPolicy(currentNativeAd ?: return, adView)
                        _binding?.nativeExitAd?.removeAllViews()
                        _binding?.nativeExitAd?.addView(adView)
                    }
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.nativeExitAd?.visibility = View.GONE
                    _binding?.adView?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.nativeExitAd?.visibility = View.GONE
                    _binding?.adView?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }
            })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
}