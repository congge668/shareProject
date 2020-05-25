package com.cong.shareproject

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.cong.shareproject.chart.LineChartActivity
import com.cong.shareproject.chart.OneBarChartActivity
import com.cong.shareproject.chart.TwoBarChartActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun click(view: View){
        when(view.id){
            R.id.btn_quxian -> {
                    val it = Intent(this@MainActivity, LineChartActivity::class.java)
                    startActivity(it)
            }
            R.id.btn_barChart -> {
                val it = Intent(this@MainActivity, OneBarChartActivity::class.java)
                startActivity(it)
            }
            R.id.btn_barChart_2 -> {
                val it = Intent(this@MainActivity, TwoBarChartActivity::class.java)
                startActivity(it)
            }
        }
    }
}