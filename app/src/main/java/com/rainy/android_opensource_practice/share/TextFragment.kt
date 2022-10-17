package com.rainy.android_opensource_practice.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.rainy.android_opensource_practice.databinding.FragmentTextBinding
import kotlinx.coroutines.flow.collect

/**
 * @author jiangshiyu
 * @date 2022/10/17
 */
class TextFragment : Fragment() {


    private val mBinding: FragmentTextBinding by lazy {
        FragmentTextBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            LocalEventBus.events.collect {
                mBinding.tvTime.text = it.timestamp.toString()
            }
        }
    }


}