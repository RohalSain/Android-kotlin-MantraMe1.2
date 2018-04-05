package com.developer.rohal.mantra


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fragment_premium.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


/**
 * A simple [Fragment] subclass.
 */
class fragmentPremium : Fragment() {
    //
    protected var mMonths = arrayOf(" ", "2 Feb", "3 Feb", "4 Feb", "5 Feb", "6 Feb", "7 Feb", "8 Feb", "9 Feb", "10 Feb")
    //
    var chart: BarChart? = null
    var BARENTRY: ArrayList<BarEntry>? = null
    var BarEntryLabels: ArrayList<IBarDataSet>? = null
    var Bardataset: BarDataSet? = null
    var BARDATA: BarData? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_fragment_premium, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val entries = ArrayList<BarEntry>()
        chart1.getDescription().setText(" ")
        chart1.setDrawGridBackground(false)
        chart1.setDrawBarShadow(false);
        //chart1.setHighlightFullBarEnabled(true)
        val l = chart1.getLegend()
        l.setWordWrapEnabled(true)
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        val rightAxis = chart1.getAxisRight()
        //rightAxis.setDrawGridLines(false)
        //rightAxis.setAxisMinimum(0f)
        val leftAxis = chart1.getAxisLeft()
        leftAxis.setDrawGridLines(false)
        leftAxis.setAxisMinimum(0f)
        val xAxis = chart1.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setAxisMinimum(0f)
        xAxis.setGranularity(1f)
        xAxis.setValueFormatter(IAxisValueFormatter { value, axis -> mMonths[value.toInt() % mMonths.size] })
        val data = CombinedData()
        data.setData(generateBarData())
        xAxis.setAxisMaximum(data.getXMax() + 0.50f);
        chart1.setData(data.barData);
        chart1.invalidate();
        chart1.animateY(3000)
    }

    private fun getBarEnteries(entries: ArrayList<BarEntry>): ArrayList<BarEntry> {
        entries.add(BarEntry(1f, 10f))
        entries.add(BarEntry(2f, 20f))
        entries.add(BarEntry(3f, 30f))
        entries.add(BarEntry(4f, 50f))
        entries.add(BarEntry(5f, 60f))
        entries.add(BarEntry(6f, 70f))
        entries.add(BarEntry(7f, 80f))
        entries.add(BarEntry(8f, 90f))
        return entries
    }

    private fun generateBarData(): BarData {

        var entries = ArrayList<BarEntry>()
        entries = getBarEnteries(entries)
        val set1 = BarDataSet(entries, "Bar")
        set1.setColor(Color.rgb(105, 105, 105));
        //set1.setColors(*ColorTemplate.MATERIAL_COLORS)
        //set1.valueTextColor = Color.rgb(60, 220, 78)
        set1.valueTextSize = 10f
        // set1.axisDependency = YAxis.AxisDependency.LEFT
        val barWidth = 0.8 // x2 dataset
        val d = BarData(set1)
        d.barWidth = barWidth.toFloat()
        return d

    }
    /*
     val entries = ArrayList<BarEntry>()
        chart1.getDescription().setText(" ")
        entries.add(BarEntry(1f, 150f))
        entries.add(BarEntry(2f, 80f))
        entries.add(BarEntry(3f, 40f))
        entries.add(BarEntry(4f, 10f))
        entries.add(BarEntry(5f, 10f))
        entries.add(BarEntry(6f, 80f))
        entries.add(BarEntry(7f, 40f))
        entries.add(BarEntry(8f, 10f))
        entries.add(BarEntry(9f, 10f))
        val set = BarDataSet(entries, "")
        val data = BarData(set)
        data.barWidth = 0.6f // set custom bar width
        chart1.setDrawGridBackground(false)
        chart1.setHighlightFullBarEnabled(true)
        val xAxis = chart1.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setAxisMinimum(0f)
        xAxis.setGranularity(1f)
        xAxis.setValueFormatter(IAxisValueFormatter { value, axis -> mMonths[value.toInt() % mMonths.size] })
        chart1.getXAxis().setDrawGridLines(false); // disable grid lines for the XAxis
        chart1.getAxisLeft().setDrawGridLines(true); // disable grid lines for the left YAxis
        chart1.getAxisRight().setDrawGridLines(false);
        chart1?.setData(data)
        chart1?.setFitBars(false) // make the x-axis fit exactly all bars
        chart1?.invalidate()
     */
    }

