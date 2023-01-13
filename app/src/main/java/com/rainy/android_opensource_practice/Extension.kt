package com.rainy.android_opensource_practice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * @author jiangshiyu
 * @date 2023/1/13
 */


fun FragmentActivity.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), msg, duration).show()
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    if (msg.isNotEmpty())
        Toast.makeText(this, msg, duration).show()
}

inline fun <reified T> FragmentActivity.startActivity(bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}


inline fun <reified T> Fragment.startActivity(bundle: Bundle? = null) {
    val intent = Intent(requireContext(), T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}


fun View.invisible() {
    this.visibility = View.INVISIBLE
}


fun View.visible() {
    this.visibility = View.VISIBLE
}


fun View.gone() {
    this.visibility = View.GONE
}

