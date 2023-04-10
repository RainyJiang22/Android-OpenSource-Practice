package com.rainy.android_opensource_practice


import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    var mViewBind: VB? = null

    val mViewModel: VM by lazy {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val clazz = types[1] as Class<VM>
        ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(clazz)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //如果是8.0系统的手机，并且认为是透明主题的Activity
        if (Build.VERSION.SDK_INT == 26 && this.isTranslucentOrFloating()) {
            //通过反射取消方向的设置，这样绕开系统的检查，避免闪退
            val result: Boolean = this.fixOrientation()
        }
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        initContentView()
        intent.apply {
            extras?.apply {
                onBundle(this)
            }
        }
        initView(savedInstanceState)
    }

    private fun initContentView() {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val aClass = types[0] as Class<VB>
        try {
            mViewBind =
                aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
                    .invoke(null, getLayoutInflater()) as VB?
            super.setContentView(mViewBind?.root)
        } catch (e: Error) {
            e.printStackTrace();
        }
    }


    //通过反射判断是否是透明页面
    open fun isTranslucentOrFloating(): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable")
                .getField("Window")[null] as IntArray
            val ta = obtainStyledAttributes(styleableRes)
            val m =
                ActivityInfo::class.java.getMethod(
                    "isTranslucentOrFloating",
                    TypedArray::class.java
                )
            m.isAccessible = true
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }

    //通过反射将方向设置为 SCREEN_ORIENTATION_UNSPECIFIED，绕开系统的检查
    open fun fixOrientation(): Boolean {
        try {
            val field = Activity::class.java.getDeclaredField("mActivityInfo")
            field.isAccessible = true
            val o = field[this] as ActivityInfo
            o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            field.isAccessible = false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewBind = null
    }


    fun binding(block: VB?.() -> Unit) {
        block.invoke(mViewBind)
    }


    abstract fun onBundle(bundle: Bundle)

    abstract fun createObserver()

    abstract fun initView(savedInstanceState: Bundle?)

    open fun showLoading(message: String) {}

    open fun dismissLoading() {}
}