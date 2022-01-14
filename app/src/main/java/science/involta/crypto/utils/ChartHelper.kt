package science.involta.crypto.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import science.involta.crypto.R
import java.text.SimpleDateFormat
import java.util.*

data class ChartData(val xAxisValues: List<String>, val entries: List<Entry>)

object ChartHelper {
    fun drawHistoricalLineChart(view: View, symbol: String, historyList: List<DoubleArray>) {
        (view as? LineChart)?.let { lineChart ->
            lineChart.description.isEnabled = true
            lineChart.setDrawGridBackground(false)

            val chartData = getChartData(historyList)

            val lineDataSet = LineDataSet(chartData.entries, "Изменение цены для $symbol")
            lineDataSet.setDrawCircles(false)
            lineDataSet.color = ContextCompat.getColor(lineChart.context, R.color.line_chart_color)
            lineDataSet.valueTextColor = ContextCompat.getColor(lineChart.context, R.color.line_chart_text_color)

            val xAxis = lineChart.xAxis
            xAxis.granularity = 1F
            xAxis.textColor = ContextCompat.getColor(lineChart.context, R.color.line_chart_text_color)
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            val formatter = object: ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return if (value.toInt() < chartData.xAxisValues.size) chartData.xAxisValues[value.toInt()] else ""
                }
            }

            xAxis.valueFormatter = formatter
            xAxis.labelRotationAngle = 90F

            val yAxisRight = lineChart.axisRight
            yAxisRight.isEnabled = false

            val yAxisLeft = lineChart.axisLeft
            yAxisLeft.isEnabled = true
            yAxisLeft.granularity = 1F
            yAxisLeft.textColor = ContextCompat.getColor(lineChart.context, R.color.line_chart_text_color)

            val data = LineData(lineDataSet)

            lineChart.data = data
            lineChart.animateX(1500)

            val legend = lineChart.legend
            legend.textColor = ContextCompat.getColor(lineChart.context, R.color.line_chart_text_color)
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER

            lineChart.invalidate()
        }
    }

    private fun getChartData(historyList: List<DoubleArray>): ChartData {
        val xAxisValues = arrayListOf<String>() //список меток для оси X
        val entries = arrayListOf<Entry>()

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        historyList.forEachIndexed { index, entry ->
            //получаем дату
            val date = Date(entry[0].toLong())
            //приводим дату к нужному для нас формату
            val label = simpleDateFormat.format(date)

            xAxisValues.add(label)
            entries.add(Entry(index.toFloat(), entry[1].toFloat()))
        }

        return ChartData(xAxisValues, entries)
    }
}