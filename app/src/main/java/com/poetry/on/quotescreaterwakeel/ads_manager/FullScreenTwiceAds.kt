package com.poetry.on.quotescreaterwakeel.ads_manager

import android.app.Activity
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.poetry.on.quotescreaterwakeel.ads_manager.FullScreenAdsTwo.mInterstitialAd
import com.poetry.on.quotescreaterwakeel.ads_manager.interfaces.AdMobAdListener
import com.poetry.on.quotescreaterwakeel.ads_manager.interfaces.AdsListener
import com.poetry.on.quotescreaterwakeel.utilities.counter
import com.poetry.on.quotescreaterwakeel.utilities.firebaseAnalytics
import com.poetry.on.quotescreaterwakeel.utilities.id_frequency_counter
import com.poetry.on.quotescreaterwakeel.utilities.id_inter_counter
import com.poetry.on.quotescreaterwakeel.utilities.inter_frequency_count


const val TAG = "TwoInterAds"
fun loadTwoInterAds(
    ads: AdsManager,
    activity: Activity,
    remoteConfigMedium: Boolean,
    remoteConfigNormal: Boolean,
    adIdMedium: String,
    adIdNormal: String,
    tagClass: String,
) {
    Log.d(TAG, "loadTwoInterAds->adIdMedium: $adIdMedium")
    Log.d(TAG, "loadTwoInterAds->adIdNormal: $adIdNormal")
    Log.d(TAG, "loadTwoInterAds: counter $inter_frequency_count $id_frequency_counter")
    if (!AdsManager.isNetworkAvailable(activity)) {
        return
    }
    if (inter_frequency_count >= id_frequency_counter) {
        return
    }
    ads.fullScreenAds().loadFullScreenAd(activity = activity,
        addConfig = remoteConfigMedium,
        fullScreenAdId = adIdMedium,
        adsListener = object : AdsListener {
            override fun adFailed() {
                Log.d(TAG, "adFailed: medium inter failed")
                //load normal ad
                firebaseAnalytics("inter_medium_failed_$tagClass", "interLoaded")
                ads.fullScreenAdsTwo().loadFullScreenAdTwo(activity = activity,
                    addConfig = remoteConfigNormal,
                    fullScreenAdId = adIdNormal,
                    adsListener = object : AdsListener {
                        override fun adFailed() {
                            Log.d(TAG, "adFailed: normal inter failed")
                            firebaseAnalytics("inter_normal_failed_$tagClass", "interLoaded")
                        }

                        override fun adLoaded() {
                            Log.d(TAG, "adLoaded: normal inter load")
                            firebaseAnalytics("inter_normal_loaded_$tagClass", "interLoaded")
                        }

                        override fun adNotFound() {
                            Log.d(TAG, "adNotFound: normal not found")
                            firebaseAnalytics("inter_normal_not_found_$tagClass", "interLoaded")
                        }
                    })
            }

            override fun adNotFound() {
                firebaseAnalytics("inter_medium_not_found_$tagClass", "interLoaded")
                Log.d(TAG, "adNotFound: medium not found")
                ads.fullScreenAdsTwo().loadFullScreenAdTwo(activity = activity,
                    addConfig = remoteConfigNormal,
                    fullScreenAdId = adIdNormal,
                    adsListener = object : AdsListener {
                        override fun adFailed() {
                            Log.d(TAG, "adFailed: normal inter failed")
                            firebaseAnalytics("inter_normal_failed_$tagClass", "interLoaded")


                        }

                        override fun adLoaded() {
                            Log.d(TAG, "adLoaded: normal inter load")
                            firebaseAnalytics("inter_normal_loaded_$tagClass", "interLoaded")

                        }

                        override fun adNotFound() {
                            Log.d(TAG, "adNotFound: normal not found")
                            firebaseAnalytics("inter_normal_not_found_$tagClass", "interLoaded")

                        }

                        override fun adAlreadyLoaded() {

                        }
                    })
            }

            override fun adLoaded() {
                Log.d(TAG, "adLoaded: medium inter load")
                firebaseAnalytics("inter_medium_loaded_$tagClass", "interLoaded")
            }
        })
}


fun showTwoInterAd(
    ads: AdsManager,
    activity: Activity,
    remoteConfigMedium: Boolean,
    remoteConfigNormal: Boolean,
    adIdMedium: String,
    adIdNormal: String,
    tagClass: String,
    isBackPress: Boolean,
    layout: ConstraintLayout,
    function: () -> Unit,
) {
    Log.d(TAG, "showTwoInterAd->adIdMedium: $adIdMedium")
    Log.d(TAG, "showTwoInterAd->adIdNormal: $adIdNormal")
    Log.d(TAG, "showTwoInterAd->adIdNormal: $id_inter_counter")
    if (mInterstitialAd == null && !remoteConfigMedium && !remoteConfigNormal) {
        return
    }
    if (!AdsManager.isNetworkAvailable(activity)) {
        return
    }
    if (inter_frequency_count >= id_frequency_counter) {
        function.invoke()
        return
    }

    if (id_inter_counter != counter) {
        counter++
        Log.d(TAG, "showTwoInterAd->adIdNormalSkip: $counter")
        function.invoke()
        return
    } else {
        counter = 0
        Log.d(TAG, "showTwoInterAd->adIdNormalCounter: $counter")
    }
//    layout.visibility = View.VISIBLE
//    Handler().postDelayed({
//        layout.visibility = View.GONE
        ads.fullScreenAds().showAndLoad(activity, remoteConfigMedium, object : AdMobAdListener {
            override fun fullScreenAdShow() {
                inter_frequency_count++
                Log.d(TAG, "fullScreenAdShow: medium inter ad show")
                firebaseAnalytics("inter_medium_show_$tagClass", "inter_Show")
            }

            override fun fullScreenAdDismissed() {
                Log.d(TAG, "fullScreenAdDismissed: medium inter dismiss")
                firebaseAnalytics("inter_medium_dismiss_$tagClass", "inter_Show")
                loadTwoInterAds(
                    ads = ads,
                    activity = activity,
                    remoteConfigMedium = remoteConfigMedium,
                    remoteConfigNormal = remoteConfigNormal,
                    adIdMedium = adIdMedium,
                    adIdNormal = adIdNormal,
                    tagClass = tagClass
                )
                function.invoke()
            }

            override fun fullScreenAdFailedToShow() {
                showNormalInterAd(
                    ads,
                    activity,
                    remoteConfigNormal,
                    tagClass,
                    remoteConfigMedium,
                    adIdMedium,
                    adIdNormal
                ) {

                    function.invoke()
                }
                Log.d(TAG, "fullScreenAdFailedToShow: medium inter failed to show")
                firebaseAnalytics("inter_medium_failed_show_$tagClass", "inter_Show")


            }

            override fun fullScreenAdNotAvailable() {
                showNormalInterAd(
                    ads,
                    activity,
                    remoteConfigNormal,
                    tagClass,
                    remoteConfigMedium,
                    adIdMedium,
                    adIdNormal
                ) {

                    function.invoke()
                }

                Log.d(TAG, "fullScreenAdNotAvailable: medium inter not available")
                firebaseAnalytics("inter_medium_not_Found_$tagClass", "inter_Show")

            }

        }, "", object : AdsListener {
        })
//    }, 1000)
}

fun showTwoInterAdFirst(
    ads: AdsManager,
    activity: Activity,
    remoteConfigMedium: Boolean,
    remoteConfigNormal: Boolean,
    adIdMedium: String,
    adIdNormal: String,
    tagClass: String,
    isBackPress: Boolean,
    layout: ConstraintLayout,
    function: () -> Unit,
) {
    Log.d(TAG, "showTwoInterAd->adIdMedium: $adIdMedium")
    Log.d(TAG, "showTwoInterAd->adIdNormal: $adIdNormal")
    Log.d(TAG, "showTwoInterAd->adIdNormal: $id_inter_counter")

    if (!AdsManager.isNetworkAvailable(activity) && !remoteConfigMedium && !remoteConfigNormal) {
        return
    }
//    if (inter_frequency_count >= id_frequency_counter) {
//        return
//    }
    layout.visibility = View.VISIBLE
    Handler().postDelayed({
        layout.visibility = View.GONE
        ads.fullScreenAds().showAndLoad(activity, remoteConfigMedium, object : AdMobAdListener {
            override fun fullScreenAdShow() {
            inter_frequency_count++
                Log.d(TAG, "fullScreenAdShow: medium inter ad show")
                firebaseAnalytics("inter_medium_show_$tagClass", "inter_Show")
            }

            override fun fullScreenAdDismissed() {
                Log.d(TAG, "fullScreenAdDismissed: medium inter dismiss")
                firebaseAnalytics("inter_medium_dismiss_$tagClass", "inter_Show")
                loadTwoInterAds(
                    ads = ads,
                    activity = activity,
                    remoteConfigMedium = remoteConfigMedium,
                    remoteConfigNormal = remoteConfigNormal,
                    adIdMedium = adIdMedium,
                    adIdNormal = adIdNormal,
                    tagClass = tagClass
                )
                function.invoke()
            }

            override fun fullScreenAdFailedToShow() {
                showNormalInterAd(
                    ads,
                    activity,
                    remoteConfigNormal,
                    tagClass,
                    remoteConfigMedium,
                    adIdMedium,
                    adIdNormal
                ) {

                    function.invoke()
                }
                Log.d(TAG, "fullScreenAdFailedToShow: medium inter failed to show")
                firebaseAnalytics("inter_medium_failed_show_$tagClass", "inter_Show")


            }

            override fun fullScreenAdNotAvailable() {
                showNormalInterAd(
                    ads,
                    activity,
                    remoteConfigNormal,
                    tagClass,
                    remoteConfigMedium,
                    adIdMedium,
                    adIdNormal
                ) {
                    function.invoke()
                }
                Log.d(TAG, "fullScreenAdNotAvailable: medium inter not available")
                firebaseAnalytics("inter_medium_not_Found_$tagClass", "inter_Show")
            }

        }, adIdMedium, object : AdsListener {

        })
    }, 1000)
}

private fun showNormalInterAd(
    ads: AdsManager,
    activity: Activity,
    remoteConfigNormal: Boolean,
    tagClass: String,
    remoteConfigMedium: Boolean,
    adIdMedium: String,
    adIdNormal: String,
    function: () -> Unit,
) {
    Log.d(TAG, "showNormalInterAd->adIdMedium: $adIdMedium")
    Log.d(TAG, "showNormalInterAd->adIdNormal: $adIdNormal")
    if (!AdsManager.isNetworkAvailable(activity)) {
        return
    }
    ads.fullScreenAdsTwo().showAndLoadTwo(activity, remoteConfigNormal, object : AdMobAdListener {
        override fun fullScreenAdShow() {
            inter_frequency_count++
            Log.d(TAG, "fullScreenAdShow: normal inter ad show")
            firebaseAnalytics("inter_normal_show_$tagClass", "inter_Show")

        }

        override fun fullScreenAdDismissed() {
            Log.d(TAG, "fullScreenAdDismissed: normal inter dismiss")
            firebaseAnalytics("inter_normal_dismisss_$tagClass", "inter_Show")
            loadTwoInterAds(
                ads = ads,
                activity = activity,
                remoteConfigMedium = remoteConfigMedium,
                remoteConfigNormal = remoteConfigNormal,
                adIdMedium = adIdMedium,
                adIdNormal = adIdNormal,
                tagClass = tagClass
            )
            function.invoke()
        }

        override fun fullScreenAdFailedToShow() {
            Log.d(TAG, "fullScreenAdFailedToShow: normal inter failed to show")
            firebaseAnalytics("inter_normal_failed_show_$tagClass", "inter_Show")
            function.invoke()

        }

        override fun fullScreenAdNotAvailable() {
            Log.d(TAG, "fullScreenAdNotAvailable: normal inter not available")
            firebaseAnalytics("inter_normal_not_Found_$tagClass", "inter_Show")
            function.invoke()
        }

    }, adIdNormal, object : AdsListener {

    })
}

