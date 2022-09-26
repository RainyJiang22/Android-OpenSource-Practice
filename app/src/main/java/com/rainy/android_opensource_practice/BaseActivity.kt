package com.rainy.android_opensource_practice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

abstract class BaseActivity<VM:BaseViewModel,VB:ViewBinding> : BaseVmVbActivity<VM,VB>() {
    override fun createObserver() {
    }

    override fun dismissLoading() {
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun showLoading(message: String) {
    }


}