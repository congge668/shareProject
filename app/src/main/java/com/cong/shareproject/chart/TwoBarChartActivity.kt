package com.cong.shareproject.chart

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cong.coordinatorlayoutdemo.utils.UtilHelper
import com.cong.shareproject.R
import com.cong.shareproject.bean.*
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.activity_bar_chart_two.*

class TwoBarChartActivity : AppCompatActivity(){

    var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart_two)
        mContext = this

        val twoDataBean = UtilHelper.JsonToObject(UtilHelper.getJson(mContext!!,"two_bar.json"), TwoDataBean::class.java)
        setLearnBarChart(twoDataBean.studyDetail!!.memberCount!!,twoDataBean.studyDetail!!.studyHour!!)
    }

    private fun getMaxMemberCount(memberCountList:List<MemberCount>):Float{
        var maxMember = 0f
        for (member in memberCountList){
            if (member.number > maxMember){
                maxMember = member.number.toFloat()
            }
        }
        return maxMember
    }

    private fun getMaxStudyHour(studyHourList:List<StudyHour>):Float{
        var maxMember = 0f
        for (member in studyHourList){
            if (member.number > maxMember){
                maxMember = member.number
            }
        }
        return maxMember
    }

    private fun setLearnBarChart(memberCountList:List<MemberCount>, studyHourList:List<StudyHour>) {
        barChart.description.isEnabled = false
        barChart.setDrawGridBackground(false) //不显示图表网格
        barChart.setDrawValueAboveBar(true) //绘制值在bar上
        barChart.axisRight.isEnabled = true//显示右边坐标系
        barChart.legend.isEnabled = true//显示图例
        barChart.setTouchEnabled(false)//设置是否点击树状图，不允许触碰，就没法在图上拉伸
        barChart.isDragEnabled = false//设置是否可以拖拽，缩放


        //获取X轴
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.setDrawGridLines(false)
        //xAxis.setDrawAxisLine(true)
        xAxis.textColor = resources.getColor(R.color.c_33)
        xAxis.setCenterAxisLabels(true)
        xAxis.axisMinimum = 0f  //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）,要设最小，最大值，避免出现最右边的柱形看不见
        xAxis.axisMaximum = memberCountList.size.toFloat()
        //granularity是x轴在图缩放时值的间隔按比例变化，设为2f,即间隔为2倍
        //xAxis.granularity = 1f  //默认为1f
        xAxis.valueFormatter = IAxisValueFormatter { value, _ -> memberCountList[Math.min(Math.max(value.toInt(), 0), memberCountList.size - 1)].day }

        //下面是不需要的，写上用法备用
        //xAxis.setLabelCount(12,true) //第二个参数表示是否平均分配 如果为true则按比例分为12个点、如果为false则适配X刻度的值来分配点，可能没有12个点

        //获取Y轴
        val leftAxis = barChart.axisLeft
        leftAxis.axisMinimum = 0f
        var maxLeft = getMaxMemberCount(memberCountList)
        if (maxLeft == 0f){
            maxLeft = 5f
        }
        leftAxis.axisMaximum = maxLeft
        leftAxis.spaceTop = 20f
        leftAxis.granularity = 1f
        //绘制虚线
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        //是否绘制0所在的网格线
        leftAxis.setDrawZeroLine(true)
        leftAxis.textSize = 10f
        leftAxis.textColor = resources.getColor(R.color.c_33)
        leftAxis.setLabelCount(5, true)

        val rightAxis = barChart.axisRight
        rightAxis.axisMinimum = 0f
        var maxRight = getMaxStudyHour(studyHourList)
        if (maxRight == 0f){
            maxRight = 1f
        }
        rightAxis.axisMaximum = maxRight
        rightAxis.spaceTop = 20f
        rightAxis.granularity = 1f
        //绘制虚线
        rightAxis.enableGridDashedLine(10f, 10f, 0f)
        //是否绘制0所在的网格线
        rightAxis.setDrawZeroLine(true)
        rightAxis.textSize = 10f
        rightAxis.textColor = resources.getColor(R.color.c_33)
        rightAxis.setLabelCount(5, true)

//        rightAxis.valueFormatter = IAxisValueFormatter { value, _ ->
//            val mFormat = DecimalFormat("###")
//            mFormat.format(value.toDouble())
//        }

//        leftAxis.valueFormatter = IAxisValueFormatter { value, _ ->
//            val mFormat = DecimalFormat("###")
//            mFormat.format(value.toDouble())
//        }

        //图例
        barChart.getLegend().isEnabled = true //如果没法控制图例和图表之间的距离，图例不显示，改为手动添加相应的view显示
         val  legend = barChart.getLegend()
        legend.setForm(Legend.LegendForm.SQUARE)
        legend.setTextSize(11f)
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        //是否绘制在图表里面
        legend.setDrawInside(false)



        barChart.data = generateData(memberCountList,studyHourList)
        barChart.animateY(700)

    }

    //设置数据
    private fun generateData(memberCountList:List<MemberCount>, studyHourList:List<StudyHour>): BarData {

        val entries = ArrayList<BarEntry>()
        for (i in memberCountList.indices) {
            entries.add(BarEntry(i.toFloat(), memberCountList[i].number.toFloat()))
        }
        val d = BarDataSet(entries, "学习人数(单位：个)")
        d.color = resources.getColor(R.color.c_32c5ff)//树状图颜色
        d.axisDependency = YAxis.AxisDependency.LEFT
        //人数为整数
        d.setValueFormatter { value, _, _, _ ->
            value.toInt().toString()
        }

        val entries2 = ArrayList<BarEntry>()
        for (i in studyHourList.indices) {
            entries2.add(BarEntry(i.toFloat(), studyHourList[i].number))
        }
        val d2 = BarDataSet(entries2, "学习时长(单位：小时)")
        d2.color = resources.getColor(R.color.c_73deb3)//树状图颜色
        d2.axisDependency = YAxis.AxisDependency.RIGHT
        d2.setValueFormatter { value, _, _, _ ->
            value.toInt().toString()
        }

        val sets = ArrayList<IBarDataSet>()
        sets.add(d)
        sets.add(d2)
        val groupSpace = 0.3f
        val barSpace = 0.05f //两个柱之间间隔
        val barWidth = (1f-groupSpace)/sets.size -0.05f
        //算式要满足：(barWidth + barSpace) * barAmount + groupSpace = (0.3 + 0.05) * 2 + 0.3 = 1.00  这样才可以x坐标和柱形对齐
        val cd = BarData(sets)
        cd.setValueTextSize(8f)
        cd.barWidth = barWidth//设置树状图宽度
        cd.groupBars(0f,groupSpace, barSpace)
//
//        cd.setValueFormatter { value, _, _, _ ->
//
//            val mFormat = DecimalFormat("###")
//            mFormat.format(value.toDouble());
//        }
        return cd
    }
}