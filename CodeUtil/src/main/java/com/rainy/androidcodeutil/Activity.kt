package com.rainy.androidcodeutil

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View

/**
 * @author jiangshiyu
 * @date 2023/1/31
 */



fun Context.asActivity(): Activity? =
    this as? Activity ?: (this as? ContextWrapper)?.baseContext?.asActivity()