package com.rainy.monitor.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rainy.monitor.Monitor
import com.rainy.monitor.R
import com.rainy.monitor.adapter.MonitorAdapter
import com.rainy.monitor.databinding.ActivityMonitorBinding
import com.rainy.monitor.db.HttpInformation
import com.rainy.monitor.utils.viewBinding
import com.rainy.monitor.utils.viewModels
import com.rainy.monitor.viewmodel.MonitorViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */

class MonitorActivity : AppCompatActivity(R.layout.activity_monitor) {

    private val binding: ActivityMonitorBinding by viewBinding()

    private val viewModel: MonitorViewModel by viewModels()

    private val monitorAdapter by lazy { MonitorAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tvToolbarTitle.text = getString(R.string.monitor)

        binding.recyclerView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = monitorAdapter
        }

        monitorAdapter.clickListener = object : MonitorAdapter.OnClickListener {
            override fun onClick(position: Int, model: HttpInformation) {

            }
        }
    }

    private fun initData() {
        viewModel.allRecordLiveData.observe(this) {
            monitorAdapter.setData(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_monitor_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> {
                Monitor.clearCache()
                Monitor.clearNotification()
            }
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

}