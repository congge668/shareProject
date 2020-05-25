package com.cong.shareproject.chart

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cong.coordinatorlayoutdemo.utils.UtilHelper
import com.cong.shareproject.R
import com.cong.shareproject.bean.User7DayBean
import com.cong.shareproject.bean.User7day
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.EntryXComparator
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlinx.android.synthetic.main.activity_line_chart.*
import java.text.DecimalFormat
import java.util.*

class LineChartActivity : AppCompatActivity(){

    var mContext:Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)
        mContext = this
        
        var user_7day = UtilHelper.JsonToObject(UtilHelper.getJson(mContext!!,"user.json"), User7DayBean::class.java)
        setChartView(lineChart_user,user_7day.user_7day!!.reversed())
    }

    private fun setChartView(lineChat: LineChart?, user_7day: List<User7day>?) {
        if (lineChat == null){
            return
        }
        lineChat.setDrawGridBackground(false)
        lineChat.description.isEnabled = false
        lineChat.isDragEnabled = false
        lineChat.setScaleEnabled(false)
        lineChat.setNoDataText("暂无数据")
        lineChat.legend?.isEnabled = true//是否显示图例
        //显示位置
//        lineChat.legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
//        lineChat.legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        lineChat.animateXY(2000, 2000)

        //获取X轴
        val xAxis = lineChat.xAxis
        //xAxis.setAvoidFirstLastClipping(true);
        xAxis?.axisMinimum = 0f
        xAxis?.setLabelCount(7, true)
        val floats = floatArrayOf(20.0f, 22f, 25f)
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.setDrawGridLines(false)
        //xAxis.setGridLineWidth(2f);
        xAxis?.axisLineWidth = 3f
        xAxis?.axisLineColor = Color.parseColor("#E3E2E6")
        xAxis?.textSize = 10f
        xAxis?.textColor = Color.parseColor("#000000")
        //xAxis.setGranularity(1f);
        xAxis?.isEnabled = true
        xAxis?.setDrawAxisLine(false)
        xAxis?.setGridDashedLine(DashPathEffect(floats, 10f))
        xAxis?.enableGridDashedLine(20f, 10f, 0f)
        //设置x轴数据
        if (!user_7day.isNullOrEmpty()) {
            xAxis?.valueFormatter = IAxisValueFormatter { value, _ -> user_7day[Math.min(Math.max(value.toInt(), 0), user_7day.size - 1)].display_date }
        }

        val leftAxis = lineChat.axisLeft
        leftAxis?.isEnabled = true
        leftAxis?.textSize = 10f
        leftAxis?.setLabelCount(5, false)
        leftAxis?.axisMinimum = 0f//设置最小值
        leftAxis?.axisMaximum = 50f
        //设置网格线为虚线效果
        leftAxis?.enableGridDashedLine(20f, 10f, 0f)
        leftAxis?.gridColor = Color.parseColor("#E3E2E6")
        //是否绘制0所在的网格线
        leftAxis?.setDrawZeroLine(true)
        leftAxis?.axisLineWidth = 0f
        leftAxis?.axisLineColor = Color.parseColor("#E3E2E6")
        leftAxis?.textColor = Color.parseColor("#000000")
        val rightAxis = lineChat.axisRight
        rightAxis?.isEnabled = false
        rightAxis?.setDrawZeroLine(false)

        //y轴数据格式
        var isStudyZero = true
        var maxNumber = 0f
        if (!user_7day.isNullOrEmpty()) {
            for (item in user_7day) {
                if (item.amount!! > 0) {
                    isStudyZero = false
                }
                if (item.amount!! > maxNumber) {
                    maxNumber = item.amount!!
                }
            }
        }


        if (isStudyZero) {
            leftAxis?.axisMaximum = 50f
        } else {
            leftAxis?.resetAxisMaximum()
            if (maxNumber <= 5) {
                leftAxis?.axisMaximum = 5f
            } else {
                leftAxis?.axisMaximum = maxNumber + 10
            }
        }

        leftAxis?.valueFormatter = IAxisValueFormatter { value, _ ->
            val mFormat = DecimalFormat("###")
            mFormat.format(value.toDouble())
        }

        //设置数据
        val entries = ArrayList<Entry>()
        if (!user_7day.isNullOrEmpty()) {
            for (i in user_7day.indices) {
                val xVal = i.toFloat()
                var yVal = 0f
                yVal = user_7day[i].amount!!
                entries.add(Entry(xVal, yVal))
            }
        }

        // sort by x-value
        Collections.sort(entries, EntryXComparator())
        val dataSet = LineDataSet(entries, "用户情况")
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 3f

        dataSet.setDrawFilled(true)//阴影
        dataSet.fillDrawable = resources.getDrawable(R.drawable.chart_line_style)//阴影
        //dataSet.setCircleColor(Color.parseColor("#0091FF"))
        dataSet.setCircleColorHole(UtilHelper.getColor(mContext, R.color.m_white))
        dataSet.color = UtilHelper.getColor(mContext, R.color.color_0091FF)
        dataSet.fillColor = Color.parseColor("#0091FF")
        dataSet.isHighlightEnabled = false//数据位置是否可以点击
        dataSet.setDrawValues(true)//y轴是否显示数据
        dataSet.valueTextSize = 10f//数据字体大小
        dataSet.valueTextColor = UtilHelper.getColor(mContext, R.color.color_333651)
        dataSet.formSize = 15f//设置当前这条线的图例的大小

        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

        // create a data object with the datasets
        val data = LineData(dataSet)
//        data.setValueFormatter { value, _, _, _ ->
//            val mFormat = DecimalFormat("###")
//            mFormat.format(value.toDouble())
//        }

        //人要整数
        if (!user_7day.isNullOrEmpty()) {
            data.setValueFormatter(object : IValueFormatter {
                override fun getFormattedValue(value: Float, entry: Entry?, dataSetIndex: Int, viewPortHandler: ViewPortHandler?): String {
                    return value.toInt().toString()
                }

            })
        }
        // set data
        lineChat.data = data
        //lineChat.invalidate()
    }
}