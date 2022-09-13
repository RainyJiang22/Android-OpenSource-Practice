package com.rainy.monitor.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rainy.monitor.ui.MonitorOverviewFragment
import com.rainy.monitor.ui.MonitorRequestFragment
import com.rainy.monitor.ui.MonitorResponseFragment

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
internal class MonitorFragmentAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                MonitorOverviewFragment.newInstance()
            }
            1 -> {
                MonitorRequestFragment.newInstance()
            }
            2 -> {
                MonitorResponseFragment.newInstance()
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    fun getTitle(position: Int): String {
        return when (position) {
            0 -> {
                "Overview"
            }
            1 -> {
                "Request"
            }
            2 -> {
                "Response"
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }
}