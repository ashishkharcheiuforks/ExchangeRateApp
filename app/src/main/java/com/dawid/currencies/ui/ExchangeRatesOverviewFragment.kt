package com.dawid.currencies.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dawid.currencies.R
import com.dawid.currencies.databinding.CurrencyRateItemBinding
import com.dawid.currencies.databinding.FragmentExchangeRatesOverviewBinding
import com.dawid.currencies.domain.ExchangeRate
import com.dawid.currencies.viewmodels.OverviewViewModel
import kotlinx.android.synthetic.main.currency_rate_item.view.textView
import com.dawid.currencies.CurrenciesApplication
import kotlinx.android.synthetic.main.fragment_exchange_rates_overview.*
import javax.inject.Inject


class ExchangeRatesOverviewFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    var clickedItem: TextView? = null
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(OverviewViewModel::class.java)
    }
    private lateinit var binding: FragmentExchangeRatesOverviewBinding

    override fun onAttach(context: Context) {
        (context.applicationContext as CurrenciesApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentExchangeRatesOverviewBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.ratesGrid.apply {
            postponeEnterTransition()
            viewTreeObserver
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
        }

        binding.ratesGrid.adapter = ExchangeRatesAdapter(ExchangeRatesAdapter.OnClickListener { view, item ->
            clickedItem = view
            viewModel.displayExchangeRateDetails(item)
        })

        viewModel.navigateToSelectedExchangeRateDetails.observe(this, Observer {
            if(it != null) {
                val txt = clickedItem!!
                val extras = FragmentNavigatorExtras(
                   txt to txt.transitionName
                )
                val bundle = Bundle()
                bundle.putParcelable("exchangeRate", it)
                bundle.putString("transitionName", txt.transitionName)
                findNavController().navigate(R.id.action_overviewFragment_to_exchangeRateDetailFragment, bundle, null, extras)
                viewModel.onDisplayExchangeRateDetailsCompleted()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.ratesGrid.adapter = null
    }
}


class ExchangeRatesAdapter(private val onClickListener: OnClickListener) : ListAdapter<ExchangeRate, ExchangeRatesAdapter.CurrencyRateViewHolder>(CurrencyRateDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateViewHolder {
        return CurrencyRateViewHolder(CurrencyRateItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CurrencyRateViewHolder, position: Int) {
        val exchangeRate = getItem(position)
        holder.itemView.textView.transitionName = "currency$position"
        holder.bind(exchangeRate)
        holder.itemView.setOnClickListener { onClickListener.onClick(holder.itemView.textView, exchangeRate) }
    }

    object CurrencyRateDiffCallback : DiffUtil.ItemCallback<ExchangeRate>() {
        override fun areItemsTheSame(oldItem: ExchangeRate, newItem: ExchangeRate): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ExchangeRate, newItem: ExchangeRate): Boolean {
            return oldItem.value == newItem.value
        }

    }

    class CurrencyRateViewHolder(private val itemBinding: CurrencyRateItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root)  {
        fun bind(exchangeRate: ExchangeRate) {
            itemBinding.exchangeRate = exchangeRate
            itemBinding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (view: TextView, exchangeRate: ExchangeRate) -> Unit) {
        fun onClick(view: TextView, exchangeRate: ExchangeRate) = clickListener(view, exchangeRate)
    }
}
