package com.rainy.monitor.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.rainy.monitor.R
import com.rainy.monitor.adapter.MonitorFragmentAdapter
import com.rainy.monitor.databinding.ActivityMonitorDetailsBinding
import com.rainy.monitor.utils.FormatUtils
import com.rainy.monitor.utils.viewBinding
import com.rainy.monitor.viewmodel.MonitorDetailsViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
class MonitorDetailsActivity : AppCompatActivity(R.layout.activity_monitor_details) {

    private val binding: ActivityMonitorDetailsBinding by viewBinding()

    private val monitorDetailViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MonitorDetailsViewModel(application, intent.getLongExtra(KEY_ID, 0)) as T
            }
        }).get(MonitorDetailsViewModel::class.java).apply {
            recordLiveData.observe(this@MonitorDetailsActivity) { httpInformation ->
                binding.tvToolbarTitle.text =
                    String.format("%s  %s", httpInformation.method, httpInformation.path)
            }
        }
    }

    private val keyId by lazy {
        return@lazy intent.getLongExtra(KEY_ID, 0)
    }

    companion object {

        private const val KEY_ID = "keyId"

        fun navTo(context: Context, id: Long) {
            val intent = Intent(context, MonitorDetailsActivity::class.java)
            intent.putExtra(KEY_ID, id)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val monitorFragmentAdapter = MonitorFragmentAdapter(this)
        binding.viewPager.adapter = monitorFragmentAdapter
        binding.viewPager.offscreenPageLimit = monitorFragmentAdapter.itemCount
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = monitorFragmentAdapter.getTitle(position)
            }
        tabLayoutMediator.attach()

        monitorDetailViewModel.recordLiveData.observe(this) {
            binding.tvToolbarTitle.text = String.format("%s  %s", it.method, it.path)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_monitor_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {
                val httpInformation = monitorDetailViewModel.recordLiveData.value
                if (httpInformation != null) {
                    share(FormatUtils.getShareText(httpInformation))
                }
            }
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    /**
     * 分享
     */
    private fun share(content: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, content)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, null))
    }

}