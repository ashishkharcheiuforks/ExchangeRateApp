package com.dawid.currencies.ui


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import com.dawid.currencies.CurrenciesApplication
import com.dawid.currencies.R
import com.dawid.currencies.databinding.FragmentExchangeRateDetailBinding
import com.dawid.currencies.viewmodels.ExchangeRateDetailViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.dawid.currencies.util.getDateFromFloat
import com.github.mikephil.charting.components.XAxis
import org.joda.time.format.DateTimeFormat
import kotlinx.android.synthetic.main.fragment_exchange_rate_detail.*
import javax.inject.Inject

class ExchangeRateDetailFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ExchangeRateDetailViewModel
    private lateinit var binding: FragmentExchangeRateDetailBinding
    private val formatter: ValueFormatter = object : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return getDateFromFloat(value).toString(DateTimeFormat.forPattern("d MMM"))
        }
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as CurrenciesApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val exchangeRate = ExchangeRateDetailFragmentArgs.fromBundle(arguments!!).exchangeRate
        val transitionName  = ExchangeRateDetailFragmentArgs.fromBundle(arguments!!).transitionName

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeRateDetailViewModel::class.java)
        viewModel.start(exchangeRate)

        binding = FragmentExchangeRateDetailBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.textView3.transitionName = transitionName

        viewModel.exchangeRateHistory.observe(this, Observer {
            if(it.isNotEmpty()) setupChart(viewModel.getChartData().sortedBy { it.x })
        })
        return binding.root
    }

    private fun setupChart(chartData: List<Entry>) {
        val chartDataSet = LineDataSet(chartData, "xxx")
        chartDataSet.color = R.color.colorAccent
        chartDataSet.valueTextColor = R.color.colorAccent
        chartDataSet.setDrawFilled(true)
        chart.data = LineData(chartDataSet)
        chart.xAxis.setLabelCount(7, false)
        chart.xAxis.granularity = 86400000f //=1day
        chart.xAxis.setAvoidFirstLastClipping(true)
        chart.xAxis.setDrawAxisLine(true);
        chart.xAxis.setDrawGridLines(true);
        chart.xAxis.position = XAxis.XAxisPosition.TOP
        chart.axisRight.isEnabled = false
        chart.xAxis.valueFormatter = formatter
        chart.invalidate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

}
