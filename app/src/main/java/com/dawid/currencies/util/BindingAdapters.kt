package com.dawid.currencies.util

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.dawid.currencies.R
import com.dawid.currencies.domain.ExchangeRate
import com.dawid.currencies.ui.ExchangeRatesAdapter
import java.util.*


@BindingAdapter("ratesList")
fun submitRatesList(recyclerView: RecyclerView, exchangeRates: List<ExchangeRate>?) {
    val adapter = recyclerView.adapter?.let { it as ExchangeRatesAdapter }
    exchangeRates?.let {
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.grid_slide_from_bottom)
        adapter?.submitList(exchangeRates)
    }
}

@BindingAdapter("exchange_rate")
fun calculateExchangeRate(textView: TextView, exchangeRate: ExchangeRate?) {
    exchangeRate?.let {
        textView.text = String.format(
            textView.context.resources.getString(R.string.base_to_currency_rate),
            Currency.getInstance(it.currencyCode).symbol,
            1/it.value,
            Currency.getInstance(it.base).symbol
        )
    }
}

@BindingAdapter("displayAnimation")
fun displayAnimation(view: LottieAnimationView, exchangeRates: List<ExchangeRate>?) {
    exchangeRates?.let {
        if(exchangeRates.isNotEmpty()) {
            view.pauseAnimation()
            view.visibility = View.GONE
        } else {
            view.loop(true)
            view.playAnimation()
            view.visibility = View.VISIBLE
        }
    }
}