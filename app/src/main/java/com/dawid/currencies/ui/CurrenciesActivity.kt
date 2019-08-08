package com.dawid.currencies.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.dawid.currencies.CurrenciesApplication
import com.dawid.currencies.R
import kotlinx.android.synthetic.main.activity_currencies.*

class CurrenciesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies)
        val navController = findNavController(R.id.my_nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener {
            it.onNavDestinationSelected(navController)
            true
        }
        toolbar.titleMarginStart = 72

        floatingActionButton.setOnClickListener {
            val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
            val baseCurr = sharedPreferences.getString("base_currency", "EUR")
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val dialog: DialogFragment = CalculatorDialogFragment.newInstance(baseCurr)
            dialog.show(fragmentTransaction, "dialog")
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.settings_fragment -> {
                    floatingActionButton.hide()
                    toolbar.menu.setGroupVisible(R.id.main_menu, false)
                    toolbar.title = "Preferences"
                }
                R.id.exchangeRateDetailFragment -> {
                    floatingActionButton.show()
                    toolbar.menu.setGroupVisible(R.id.main_menu, true)
                    toolbar.title = "Currencies"
                }
                R.id.overviewFragment -> {
                    floatingActionButton.show()
                    toolbar.menu.setGroupVisible(R.id.main_menu, true)
                    toolbar.title = "Currencies"
                }
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true

    }
}
