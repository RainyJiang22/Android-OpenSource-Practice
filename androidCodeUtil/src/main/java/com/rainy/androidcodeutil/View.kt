package com.rainy.androidcodeutil

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author jiangshiyu
 * @date 2023/1/31
 */

val View.rootWindowInsetsCompat: WindowInsetsCompat? by viewTags(R.id.tag_root_window_insets) {
    ViewCompat.getRootWindowInsets(this)
}

val View.windowInsetsControllerCompat: WindowInsetsControllerCompat? by viewTags(R.id.tag_window_insets_controller) {
    ViewCompat.getWindowInsetsController(this)
}

fun View.doOnApplyWindowInsets(action: (View, WindowInsetsCompat) -> WindowInsetsCompat) =
    ViewCompat.setOnApplyWindowInsetsListener(this, action)

fun <T> viewTags(key: Int) = object : ReadWriteProperty<View, T?> {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: View, property: KProperty<*>) =
        thisRef.getTag(key) as? T

    override fun setValue(thisRef: View, property: KProperty<*>, value: T?) =
        thisRef.setTag(key, value)
}

@Suppress("UNCHECKED_CAST")
fun <T> viewTags(key: Int, block: View.() -> T) = ReadOnlyProperty<View, T> { thisRef, _ ->
    if (thisRef.getTag(key) == null) {
        thisRef.setTag(key, block(thisRef))
    }
    thisRef.getTag(key) as T
}