package com.cong.shareproject.chart

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cong.coordinatorlayoutdemo.utils.UtilHelper
import com.cong.shareproject.R
import com.cong.shareproject.bean.AreaData
import com.cong.shareproject.bean.AreaDataBean
import com.cong.shareproject.bean.User7DayBean
import com.cong.shareproject.bean.User7day
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.activity_bar_chart_one.*
import kotlinx.android.synthetic.main.activity_line_chart.*
import java.text.DecimalFormat

class OneBarChartActivity : AppCompatActivity(){

    var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart_one)
        mContext = this

        val areaDataBean = UtilHelper.JsonToObject(UtilHelper.getJson(mContext!!,"area_user.json"), AreaDataBean::class.java)
        setModelData(areaDataBean.areaData!!)

    }

    private fun setModelData(phoneData: List<AreaData>) {
        modelBarChart.description.isEnabled = false
        modelBarChart.setDrawGridBackground(false)
        modelBarChart.setDrawValueAboveBar(true)
        modelBarChart.isHighlightFullBarEnabled = true
        modelBarChart.axisRight.isEnabled = false//不现实右边坐标系
        modelBarChart.legend.isEnabled = true//不显示图例
        //获取X轴
        val xAxis = modelBarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.setDrawGridLines(false)
        xAxis.textColor = resources.getColor(R.color.m_charcoal_grey)
        xAxis.labelCount = phoneData.size
        xAxis.valueFormatter = IAxisValueFormatter { value, _ -> phoneData[Math.min(Math.max(value.toInt(), 0), phoneData.size - 1)].source }
        //获取Y轴
        val leftAxis = modelBarChart.axisLeft
        leftAxis.axisMinimum = 0f
        leftAxis.spaceTop = 20f
        leftAxis.granularity = 1f
        //绘制虚线
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        //是否绘制0所在的网格线
        leftAxis.setDrawZeroLine(true)
        leftAxis.textSize = 10f
        leftAxis.textColor = resources.getColor(R.color.m_charcoal_grey)
        leftAxis.setLabelCount(7, false)

        leftAxis.valueFormatter = IAxisValueFormatter { value, _ ->
            val mFormat = DecimalFormat("###")
            mFormat.format(value.toDouble())
        }
        modelBarChart.data = generateData(phoneData)
        modelBarChart.setFitBars(true)
        modelBarChart.setTouchEnabled(false)//设置是否点击树状图
        modelBarChart.isDragEnabled = false//设置是否可以拖拽，缩放
        modelBarChart.animateY(700)
    }

    //设置数据
    private fun generateData(phoneData: List<AreaData>): BarData {
        val entries = ArrayList<BarEntry>()
        for (i in 0 until phoneData.size) {
            entries.add(BarEntry(i.toFloat(), phoneData[i].num!!.toFloat()))
        }
        val d = BarDataSet(entries, "区会员")
        d.color = Color.parseColor("#0A99FD")//树状图颜色
        val sets = ArrayList<IBarDataSet>()
        sets.add(d)
        val cd = BarData(sets)
        cd.setValueFormatter { value, _, _, _ ->
            val mFormat = DecimalFormat("###")
            mFormat.format(value.toDouble())// + getString(R.string.people);
        }
        cd.barWidth = 0.2f//设置树状图宽度
        return cd
    }
}