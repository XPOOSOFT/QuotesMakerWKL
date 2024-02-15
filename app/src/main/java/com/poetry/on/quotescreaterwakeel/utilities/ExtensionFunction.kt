package com.poetry.on.quotescreaterwakeel.utilities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.poetry.on.quotescreaterwakeel.R
import com.poetry.on.quotescreaterwakeel.ads_manager.NativeAds.isDebug
import com.poetry.on.quotescreaterwakeel.model.LanguageModel
import java.util.Locale
import kotlin.random.Random

const val SHARED_PREFS = "pref"
const val Apppurchase = "apppurchase"
var counter = 0
var isSplash = true
var val_inter_back_press = false
var val_inter_main_medium = true

var val_ad_native_loading_screen = true
var val_ad_native_intro_screen = true
var val_ad_native_language_screen = true
var val_ad_native_main_menu_screen = true
var val_ad_native_list_data_screen = true

var val_ad_inter_loading_screen = true
var val_ad_inter_main_menu_screen_back = true

var val_ad_app_open_screen = true

var inter_frequency_count = 0
var id_frequency_counter = 10
var id_inter_counter = 3
var id_inter_main_medium = if (isDebug()) "ca-app-pub-3940256099942544/1033173712" else "ca-app-pub-9263479717968951/1635117858"
var id_native_screen = ""
var id_app_open_screen = ""
var id_banner_screen = ""

const val IS_FIRST = "is_First"
const val LANG_CODE = "language_code"
const val LANG_SCREEN = "LANG_SCREEN"

fun Fragment.languageData(): ArrayList<LanguageModel> {
    val list = arrayListOf<LanguageModel>()
    list.add(LanguageModel(getString(R.string.english), "en", R.drawable.us_icon, false))
    list.add(LanguageModel(getString(R.string.denmake), "nl", R.drawable.denmark_icon, false))
    list.add(LanguageModel(getString(R.string.france), "de", R.drawable.france_icon, false))
    list.add(LanguageModel(getString(R.string.germany), "br", R.drawable.germany_icon, false))
    list.add(LanguageModel(getString(R.string.india), "fr", R.drawable.india_icon, false))
    list.add(LanguageModel(getString(R.string.italian), "ca", R.drawable.italy_icon, false))
    list.add(LanguageModel(getString(R.string.spain), "ca", R.drawable.spain_icon, false))

    return list
}


fun Fragment.setupBackPressedCallback(
    onBackPressedAction: () -> Unit,
) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedAction.invoke()
            }
        }
    )
}


fun Context.shareApp() {
    val intentShare = Intent()
    intentShare.action = "android.intent.action.SEND"
    intentShare.putExtra(
        "android.intent.extra.TEXT", """
     Anti Theft app Download at: 
     https://play.google.com/store/apps/details?id=$packageName
     """.trimIndent()
    )
    intentShare.type = "text/plain"
    try {
        startActivity(Intent.createChooser(intentShare, "Share via"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.moreApp() {

    try {
        startActivity(
            Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://play.google.com/store/apps/developer?id=Master+of+Door+Lock+%26+Zipper+Lock+Screen")
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.privacyPolicy() {
    try {
        startActivity(
            Intent(
                "android.intent.action.VIEW",
                Uri.parse("http://sites.google.com/view/goldscreenlockzipperlock/heartzipperlock")
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.rateUs() {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse("market://details?id=" + this.packageName)
    try {
        startActivity(i)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

fun View.clickWithThrottle(throttleTime: Long = 100L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < throttleTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

val toast: Toast? = null

fun showToast(context: Context, msg: String) {
    toast?.cancel()
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Context.setLocale(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources: Resources = resources
    val configuration: Configuration = resources.configuration
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

fun Fragment.setLocaleMain(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources: Resources = resources
    val configuration: Configuration = resources.configuration
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

var ratingDialog: AlertDialog? = null
fun showRatingDialog(
    context: Activity?,
    onPositiveButtonClick: (Float, AlertDialog) -> Unit,
) {
    val dialogView = context?.layoutInflater?.inflate(R.layout.rating_dialog, null)
    ratingDialog = AlertDialog.Builder(context ?: return).create()
    ratingDialog?.setView(dialogView)
    ratingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val ratingBar = dialogView?.findViewById<RatingBar>(R.id.ratingBar)
    val rateUs = dialogView?.findViewById<TextView>(R.id.rate_us)
//    val rateUsNo = dialogView?.findViewById<TextView>(R.id.rate_us_no)

    ratingBar?.setOnRatingBarChangeListener { it, fl, b ->
        if(b) {
            if (fl >= 1F && fl < 3F) {
                ratingDialog?.dismiss()
                showToast(context, context.getString(R.string.thanks_txt))
            } else if (fl in 3F..5F) {
                ratingDialog?.dismiss()
                context.rateUs()
            }

        }
    }
    rateUs?.setOnClickListener {
        ratingDialog?.dismiss()
    }

//    rateUsNo?.setOnClickListener {
//        ratingDialog?.dismiss()
//    }

    ratingDialog?.show()

}

private var ratingService: AlertDialog? = null



fun Activity.setDarkMode(isDarkMode: Boolean) {
    if (isDarkMode)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    else
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}

fun ImageView.loadImage(context: Context?, resourceId: Int) {
    Glide.with(context ?: return)
        .load(resourceId)
        .into(this)
}



fun firebaseAnalytics(Item_id: String, Item_name: String) {
    try {
        val firebaseAnalytics = Firebase.analytics

        val bundle = Bundle().apply {
            //        putString(FirebaseAnalytics.Param.ITEM_ID, Item_id)
            putString(FirebaseAnalytics.Param.ITEM_NAME, Item_name)
        }
        firebaseAnalytics.logEvent(Item_id, bundle)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



fun ClosedRange<Int>.random(): Int {
    return Random.nextInt(start, endInclusive + 1)
}

fun generateRandomNumberInRange(min: Int, max: Int): Int {
    return run {
        // Use the Kotlin extension function for generating random numbers
        (min..max).random()
    }
}


