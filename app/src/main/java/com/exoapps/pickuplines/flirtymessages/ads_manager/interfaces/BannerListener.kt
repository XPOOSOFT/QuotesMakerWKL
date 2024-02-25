package com.exoapps.pickuplines.flirtymessages.ads_manager.interfaces

import com.google.android.gms.ads.LoadAdError


interface BannerListener {

    fun bannerAdLoaded() {

    }

    fun bannerAdNoInternet() {

    }

    fun bannerAdFailed(loadAdError: LoadAdError) {

    }

}