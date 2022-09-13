package com.rainy.monitor.ui

import android.os.Bundle
import com.rainy.monitor.base.BaseFragment
import com.rainy.monitor.databinding.FragmentMonitorOverviewBinding
import com.rainy.monitor.utils.FormatUtils
import com.rainy.monitor.viewmodel.MonitorDetailsViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
class MonitorOverviewFragment :BaseFragment<FragmentMonitorOverviewBinding,MonitorDetailsViewModel>() {

    companion object {
        fun newInstance(): MonitorOverviewFragment {
            return MonitorOverviewFragment()
        }

    }

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        viewModel.recordLiveData.observe(viewLifecycleOwner) { information ->
            information?.apply {
                binding?.tvUrl?.text = url
                binding?.tvMethod?.text = method
                binding?.tvProtocol?.text = protocol
                binding?.tvStatus?.text = status.toString()
                binding?.tvResponse?.text = responseSummaryText
                binding?.tvSsl?.text = if (isSsl) "Yes" else "No"
                binding?.tvTlsVersion?.text = responseTlsVersion
                binding?.tvCipherSuite?.text = responseCipherSuite
                binding?.tvRequestTime?.text = requestDateFormatLong
                binding?.tvResponseTime?.text = responseDateFormatLong
                binding?.tvDuration?.text = durationFormat
                binding?.tvRequestSize?.text = FormatUtils.formatBytes(requestContentLength)
                binding?.tvResponseSize?.text = FormatUtils.formatBytes(responseContentLength)
                binding?.tvTotalSize?.text = totalSizeFormat
            }
        }

    }
}