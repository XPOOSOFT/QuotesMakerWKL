package com.exoapps.pickuplines.flirtymessages.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.exoapps.pickuplines.flirtymessages.R
import com.exoapps.pickuplines.flirtymessages.ads_manager.AdsManager
import com.exoapps.pickuplines.flirtymessages.ads_manager.interfaces.NativeListener
import com.exoapps.pickuplines.flirtymessages.ads_manager.showTwoInterAd
import com.exoapps.pickuplines.flirtymessages.databinding.ActivityDetailBinding
import com.exoapps.pickuplines.flirtymessages.utilities.BaseFragment
import com.exoapps.pickuplines.flirtymessages.utilities.copyText
import com.exoapps.pickuplines.flirtymessages.utilities.id_inter_main_medium
import com.exoapps.pickuplines.flirtymessages.utilities.id_native_screen
import com.exoapps.pickuplines.flirtymessages.utilities.shareText
import com.exoapps.pickuplines.flirtymessages.utilities.speakText
import com.exoapps.pickuplines.flirtymessages.utilities.val_ad_native_main_menu_screen
import com.exoapps.pickuplines.flirtymessages.utilities.val_inter_main_medium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class DetailActivity :
    BaseFragment<ActivityDetailBinding>(ActivityDetailBinding::inflate),
    TextToSpeech.OnInitListener {

    private var item: String? = null
    private var textAdapter: DetailAdapter? = null
    private val textList = mutableListOf<String>()
    private var textToSpeech: TextToSpeech? = null
    private var adsManager: AdsManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            item = it.getString("item")
        }
        textToSpeech = TextToSpeech(context, this)
        adsManager = AdsManager.appAdsInit(activity ?: return)
        Glide.with(this).load(R.drawable.back_btn).into(binding?.topLay?.navMenu ?: return)
        binding?.topLay?.title?.text = item
        GlobalScope.launch(Dispatchers.IO) {
            readTextFileFromAssets(item ?: return@launch)
//            readTextFileFromAssets("Motivational Quotes")
        }
        _binding?.topLay?.settingBtn?.setOnClickListener {
            textToSpeech?.stop()
            findNavController().navigate(R.id.FragmentPro)
        }
        _binding?.topLay?.navMenu?.setOnClickListener {
            findNavController().navigateUp()
        }
        loadNative()
    }

    private fun readTextFileFromAssets(file: String) {
        try {
            val inputStream = context?.assets?.open("$file.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                textList.add(line.orEmpty())
            }
            inputStream?.close()
            reader.close()

            GlobalScope.launch(Dispatchers.Main) {
                textAdapter = DetailAdapter(textList)
                binding?.recyclerViewLinear?.layoutManager =
                    LinearLayoutManager(context)
                binding?.recyclerViewLinear?.adapter = textAdapter
                textAdapter?.onClick = { text, position ->
                    Log.d("click_tag", "onViewCreated: $text $position")
                    when (position) {
                        0 -> {
                            adsManager?.let {
                                showTwoInterAd(
                                    ads = it,
                                    activity = activity ?: return@let,
                                    remoteConfigMedium = val_inter_main_medium,
                                    remoteConfigNormal = val_inter_main_medium,
                                    adIdMedium = id_inter_main_medium,
                                    adIdNormal = id_inter_main_medium,
                                    tagClass = "detail_item_click",
                                    isBackPress = true,
                                    layout = _binding?.adsLay!!
                                ) {
                                }
                            }
                            copyText(text)
                        }

                        1 -> {
                            shareText(text)
                        }

                        2 -> {
                            if (textToSpeech != null)
                                speakText(text, textToSpeech!!)
                        }

                        3 -> {
                        }
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // TextToSpeech engine initialized successfully
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }

    private fun loadNative() {
        adsManager?.nativeAds()?.loadNativeAd(activity ?: return,
            val_ad_native_main_menu_screen,
            id_native_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    if (isAdded && isVisible && !isDetached) {
                        _binding?.nativeExitAd?.visibility = View.VISIBLE
                        _binding?.adView?.visibility = View.GONE
                        val adView =
                            layoutInflater.inflate(R.layout.ad_unified_privacy, null) as NativeAdView
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

}
