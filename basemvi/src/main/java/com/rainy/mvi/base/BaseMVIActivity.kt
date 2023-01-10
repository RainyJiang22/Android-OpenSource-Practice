package com.rainy.mvi.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * @author jiangshiyu
 * @date 2023/1/10
 */
abstract class BaseMVIActivity<T : ViewBinding> : AppCompatActivity() {

    private lateinit var _binding: T

    protected val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(_binding.root)

        initViews()
        initContent()
    }

    abstract fun initContent()

    abstract fun initViews()

    protected abstract fun getViewBinding(): T
}