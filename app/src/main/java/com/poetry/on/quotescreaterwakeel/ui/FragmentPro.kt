package com.poetry.on.quotescreaterwakeel.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.limurse.iap.DataWrappers
import com.limurse.iap.IapConnector
import com.limurse.iap.SubscriptionServiceListener
import com.poetry.on.quotescreaterwakeel.R
import com.poetry.on.quotescreaterwakeel.ads_manager.billing.PurchasePrefs
import com.poetry.on.quotescreaterwakeel.databinding.FragmentPurchaseBinding
import com.poetry.on.quotescreaterwakeel.utilities.Apppurchase
import com.poetry.on.quotescreaterwakeel.utilities.BaseFragment

class FragmentPro :
    BaseFragment<FragmentPurchaseBinding>(FragmentPurchaseBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val iapConnector = IapConnector(
            context = context ?: return, // activity / context
            nonConsumableKeys = arrayListOf(), // pass the list of non-consumables
            consumableKeys = arrayListOf(), // pass the list of consumables
            subscriptionKeys = listOf(
                getString(R.string.monthly_subcription_id),
                getString(R.string.yearly_subcription_id)
            ), // pass the list of subscriptions
            key = context?.getString(R.string.billing_id), // pass your app's license key
            enableLogging = true // to enable / disable logging
        )

        iapConnector.addSubscriptionListener(object : SubscriptionServiceListener {

            override fun onSubscriptionRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                // will be triggered upon fetching owned subscription upon initialization
                PurchasePrefs(context).putBoolean(Apppurchase, true)
            }

            override fun onPricesUpdated(iapKeyPrices: Map<String, List<DataWrappers.ProductDetails>>) {
                Log.d("TAG", "onPricesUpdated: $iapKeyPrices")
                // Update Ui
            }

            override fun onPurchaseFailed(
                purchaseInfo: DataWrappers.PurchaseInfo?,
                billingResponseCode: Int?
            ) {
                Log.d("TAG", "onPricesFaildd: ")
                PurchasePrefs(context).putBoolean(Apppurchase, false)
            }

            override fun onSubscriptionPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                // will be triggered whenever subscription succeeded
                PurchasePrefs(context).putBoolean(Apppurchase, true)
            }
        })



        _binding?.monthlyBuy?.setOnClickListener {
            if (!PurchasePrefs(context).getBoolean(Apppurchase)) {
                iapConnector.subscribe(
                    activity ?: return@setOnClickListener,
                    getString(R.string.monthly_subcription_id)
                )
            } else {
                Toast.makeText(context, "You have already Purchased", Toast.LENGTH_SHORT).show()
            }
        }

        _binding?.yearlyBuy?.setOnClickListener {
            if (!PurchasePrefs(context).getBoolean(Apppurchase)) {
                iapConnector.subscribe(
                    activity ?: return@setOnClickListener,
                    getString(R.string.yearly_subcription_id)
                )
            } else {
                Toast.makeText(context, "You have already Purchased", Toast.LENGTH_SHORT).show()
            }
        }
    }

}