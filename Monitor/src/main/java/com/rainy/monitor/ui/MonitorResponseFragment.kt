package com.rainy.monitor.ui

import android.os.Bundle
import android.text.Html
import android.view.View
import com.rainy.monitor.base.BaseFragment
import com.rainy.monitor.databinding.FragmentMonitorResponseBinding
import com.rainy.monitor.viewmodel.MonitorDetailsViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
class MonitorResponseFragment : BaseFragment<FragmentMonitorResponseBinding,MonitorDetailsViewModel>() {

    companion object {

        fun newInstance(): MonitorResponseFragment {
            return MonitorResponseFragment()
        }
    }

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {

        viewModel.recordLiveData.observe(viewLifecycleOwner) { information ->
            val headersString = information.getResponseHeadersString(true)
            if (headersString.isBlank()) {
                binding?.tvHeaders?.visibility = View.GONE
            } else {
                binding?.tvHeaders?.visibility = View.VISIBLE
                binding?.tvHeaders?.text = Html.fromHtml(headersString)
            }
            binding?.tvBody?.text = information.responseBodyFormat
        }
    }
}