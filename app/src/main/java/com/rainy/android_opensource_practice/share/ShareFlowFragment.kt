package com.rainy.android_opensource_practice.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rainy.android_opensource_practice.databinding.FragmentSharedFlowBinding

/**
 * @author jiangshiyu
 * @date 2022/10/17
 */
class ShareFlowFragment : Fragment() {

    private val mViewModel by viewModels<ShareFlowViewModel>()

    private val mBinding: FragmentSharedFlowBinding by lazy {
        FragmentSharedFlowBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            btnStart.setOnClickListener {
                mViewModel.startRefresh()
            }
            btnStop.setOnClickListener {
                mViewModel.stopRefresh()
            }
        }
    }

}