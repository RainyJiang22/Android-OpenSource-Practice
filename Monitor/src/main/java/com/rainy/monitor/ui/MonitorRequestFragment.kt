package com.rainy.monitor.ui

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.Fragment
import com.rainy.monitor.base.BaseFragment
import com.rainy.monitor.databinding.FragmentMonitorRequestBinding
import com.rainy.monitor.viewmodel.MonitorDetailsViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
class MonitorRequestFragment :BaseFragment<FragmentMonitorRequestBinding,MonitorDetailsViewModel>() {

    companion object {

        fun newInstance(): MonitorRequestFragment {
            return MonitorRequestFragment()
        }
    }

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {

        viewModel.recordLiveData.observe(viewLifecycleOwner) { httpInformation ->
            if (httpInformation != null) {
                val headersString = httpInformation.getRequestHeadersString(true)
                if (headersString.isBlank()) {
                    binding?.tvHeaders?.visibility = View.GONE
                } else {
                    binding?.tvHeaders?.visibility = View.VISIBLE
                    binding?.tvHeaders?.text = Html.fromHtml(headersString)
                }
                binding?.tvBody?.text = httpInformation.requestBodyFormat
            }
        }
    }
}