package com.dawid.currencies.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceFragmentCompat
import com.dawid.currencies.CurrenciesApplication
import com.dawid.currencies.R
import com.dawid.currencies.viewmodels.SettingsViewModel
import javax.inject.Inject


class SettingsFragment : PreferenceFragmentCompat() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SettingsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)
    }
    private var listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when(key) {
                "base_currency" -> viewModel.refreshData()
                "refresh_rate" -> {
                    val refreshRate = sharedPreferences.getString(key, "8.0")!!
                    (activity?.application as CurrenciesApplication).scheduleWork(refreshRate.toInt())
                }
            }
            Toast.makeText(this.requireContext(), "Preferences updated", Toast.LENGTH_SHORT).show()
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        (activity?.application as CurrenciesApplication).appComponent.inject(this)
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}


