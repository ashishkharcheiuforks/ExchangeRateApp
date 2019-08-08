package com.dawid.currencies.ui


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dawid.currencies.CurrenciesApplication
import kotlinx.android.synthetic.main.fragment_calculator_dialog.*
import com.dawid.currencies.R
import com.dawid.currencies.databinding.FragmentCalculatorDialogBinding
import com.dawid.currencies.viewmodels.CalculatorViewModel
import javax.inject.Inject


class CalculatorDialogFragment : DialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var currencies: Array<String>
    private var base: String = ""
    private lateinit var binding: FragmentCalculatorDialogBinding
    private lateinit var viewModel: CalculatorViewModel

    override fun onAttach(context: Context) {
        (context.applicationContext as CurrenciesApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currencies = resources.getStringArray(R.array.currencies)
        base = arguments?.getString("baseCurrency")!!
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Dialog)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CalculatorViewModel::class.java)
        binding = FragmentCalculatorDialogBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.exchangeRate.observe(this, Observer {
            viewModel.calculateResult()
        })

        binding.amountEditTxt.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.isNullOrEmpty()) {
                    viewModel.amount = s.toString().toDouble()
                    viewModel.calculateResult()
                } else {
                    viewModel.amount = 0.0
                    viewModel.calculateResult()
                }
            }
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        setupSpinner()
        baseCurrency.text = base
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter<String>(context!!, R.layout.support_simple_spinner_dropdown_item, currencies)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        currencySpinner.adapter = adapter
        currencySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.currency.postValue(currencies[position])
            }
        }
    }

    companion object {
        fun newInstance(baseCurr: String) : CalculatorDialogFragment {
            val calculatorDialogFragment = CalculatorDialogFragment()
            val bundle = Bundle()
            bundle.putString("baseCurrency", baseCurr)
            calculatorDialogFragment.arguments = bundle
            return calculatorDialogFragment
        }
    }
}
