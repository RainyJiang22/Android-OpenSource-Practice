package com.rainy.monitor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rainy.monitor.R
import com.rainy.monitor.databinding.ActivityMonitorBinding
import com.rainy.monitor.utils.viewBinding
import com.rainy.monitor.utils.viewModels
import com.rainy.monitor.viewmodel.MonitorViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */

class MonitorActivity : AppCompatActivity(R.layout.activity_monitor) {

    val binding: ActivityMonitorBinding by viewBinding()

    val viewModel: MonitorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}