package com.example.teacherkma.ui.process

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import com.example.teacherkma.R
import com.example.teacherkma.databinding.FragmentHomeBinding
import com.example.teacherkma.ui.home.HomeViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.ArrayList
import com.example.teacherkma.databinding.ProcessFragmentBinding

class ProcessFragment : Fragment() {

    private var _binding: ProcessFragmentBinding? = null
    private val binding get() = _binding!!
    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var processViewModel: ProcessViewModel

    private lateinit var chart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        processViewModel =
            ViewModelProvider(this).get(ProcessViewModel::class.java)

        _binding = ProcessFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        chart = binding.pieChart1

        chart.description.isEnabled = false
        chart.centerText = generateCenterText()
        chart.setCenterTextSize(10f)

        // radius of the center hole in percent of maximum radius

        // radius of the center hole in percent of maximum radius
        chart.holeRadius = 45f
        chart.transparentCircleRadius = 50f

        val l = chart.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        chart.data = generatePieData()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateCenterText(): SpannableString? {
        val s = SpannableString("Revenues\nQuarters 2015")
        s.setSpan(RelativeSizeSpan(2f), 0, 8, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 8, s.length, 0)
        return s
    }

    private fun generatePieData(): PieData? {
        val count = 4
        val entries1 = ArrayList<PieEntry>()
        for (i in 0 until count) {
            entries1.add(PieEntry((Math.random() * 60 + 40).toFloat(), "Quarter " + (i + 1)))
        }
        val ds1 = PieDataSet(entries1, "Quarterly Revenues 2015")
        ds1.setColors(*ColorTemplate.COLORFUL_COLORS)
        ds1.sliceSpace = 2f
        ds1.valueTextColor = Color.BLACK
        ds1.valueTextSize = 12f
        return PieData(ds1)
    }

}