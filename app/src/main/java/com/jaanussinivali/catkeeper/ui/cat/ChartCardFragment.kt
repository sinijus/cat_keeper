package com.jaanussinivali.catkeeper.ui.cat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.core.cartesian.series.Line
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jaanussinivali.catkeeper.data.CatDao
import com.jaanussinivali.catkeeper.data.CatKeeperDatabase
import com.jaanussinivali.catkeeper.data.entity.Cat
import com.jaanussinivali.catkeeper.data.entity.Weight
import com.jaanussinivali.catkeeper.databinding.DialogEditChartCardBinding
import com.jaanussinivali.catkeeper.databinding.FragmentChartCardBinding
import kotlin.concurrent.thread

class ChartCardFragment : Fragment() {

    private lateinit var binding: FragmentChartCardBinding
    private var parentFragmentNumber: Int = 0
    private val catDao: CatDao by lazy { CatKeeperDatabase.getDatabase(requireContext()).getCatDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getParentFragmentNumber()
        displayChartCard()
        setChartEditIconListener()
    }

    private fun getParentFragmentNumber() {
        val parentFragment = parentFragment as CatCardsFragment
        parentFragmentNumber = parentFragment.getFragmentNumber()
    }

    private fun displayChartCard() {
        thread {
            val weights = catDao.getWeights(parentFragmentNumber)
            val cat = catDao.getCat(parentFragmentNumber)

            requireActivity().runOnUiThread {
                if (weights.isNotEmpty()) setAndDisplayWeightChart(cat, weights) else binding.anyChartView.visibility = View.GONE
            }
        }
    }

    private fun setAndDisplayWeightChart(cat: Cat, weights: List<Weight>) {
        val anyChartView = binding.anyChartView
        val cartesian = AnyChart.line()
        cartesian.title("")
        cartesian.crosshair().enabled(true)
        cartesian.crosshair().yLabel(true).yStroke(null as Stroke?, null, null, null as String?, null as String?)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.yAxis(0).title("kg")
        cartesian.xAxis(0).labels().padding(0.0)
        val seriesData: MutableList<DataEntry> = ArrayList()
        for (weight in weights) seriesData.add(ValueDataEntry(weight.date, weight.value))

        val series1: Line = cartesian.line(seriesData)
        series1.name(cat.name)
        series1.hovered().markers().enabled(true)
        series1.hovered().markers().type(MarkerType.CIRCLE).size(4.0)
        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(0.0).offsetY(0.0)
        cartesian.legend().fontColor("#111111")
        anyChartView.setChart(cartesian)
    }

    private fun setChartEditIconListener() {
        binding.imageViewWeightChartEditIcon.setOnClickListener {
            Toast.makeText(context, "This feature is in development...", Toast.LENGTH_LONG).show()
//            showEditDialogWeightChart()
        }
    }

    private fun showEditDialogWeightChart() {
        val dialogBinding = DialogEditChartCardBinding.inflate(requireActivity().layoutInflater)
        dialogBinding.textInputWeightLayout.hint = "Weight in kg"
        dialogBinding.buttonDeleteWeightRecords.setOnClickListener {
            Toast.makeText(context, "This feature is in development...", Toast.LENGTH_LONG).show()
        }
        MaterialAlertDialogBuilder(requireContext()).setTitle("Add weight point entry").setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                thread {
                    catDao.insert(
                        Weight(
                            catId = parentFragmentNumber,
                            value = dialogBinding.textInputWeightValue.text.toString().toFloat(),
                            date = ""
                        )
                    )
                }
            }
            //refresh chart
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.show()
    }
}