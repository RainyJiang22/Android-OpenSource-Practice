package com.rainy.androidcodeutil

import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.math.roundToInt

/**
 * @author jiangshiyu
 * @date 2023/1/29
 */


inline val TextView.textString: String get() = text.toString()


fun TextView.isEmpty() = textString.isEmpty()

fun TextView.isNotEmpty() = textString.isNotEmpty()

inline var TextView.isPasswordVisible: Boolean
    get() = transformationMethod != PasswordTransformationMethod.getInstance()
    set(value) {
        transformationMethod = if (value) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
    }

inline var TextView.isFakeBoldText: Boolean
    get() = paint.isFakeBoldText
    set(value) {
        paint.isFakeBoldText = value
    }

/**
 * TextView加入下划线
 */
fun TextView.addUnDerLine() {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.transparentHighLightColor() {
    highlightColor = Color.TRANSPARENT
}


fun TextView.startCountDown(
    lifecycleOwner: LifecycleOwner,
    secondInFuture: Int = 60,
    onTick: TextView.(secondUntilFinished: Int) -> Unit,
    onFinish: TextView.() -> Unit,
): CountDownTimer =
    object : CountDownTimer(secondInFuture * 1000L, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            isEnabled = false
            onTick((millisUntilFinished / 1000f).roundToInt())
        }

        override fun onFinish() {
            isEnabled = true
            this@startCountDown.onFinish()
        }
    }.also { countDownTimer ->
        countDownTimer.start()
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED)) {
            countDownTimer.cancel()
        }
    }

fun TextView.enableWhenOtherTextNotEmpty(vararg textViews: TextView) =
    enableWhenOtherTextChanged(*textViews) { all { it.isNotEmpty() } }

inline fun TextView.enableWhenOtherTextChanged(
    vararg textViews: TextView,
    crossinline block: Array<out TextView>.() -> Boolean,
) {
    isEnabled = block(textViews)
    textViews.forEach { tv ->
        tv.doAfterTextChanged {
            isEnabled = block(textViews)
        }
    }
}

fun TextView.enableWhenChecked(vararg checkBoxes: CheckBox) {

    isEnabled = checkBoxes.all { it.isChecked }

    checkBoxes.forEach { checkBox ->
        checkBox.setOnCheckedChangeListener { compoundButton, b ->
            isEnabled = checkBoxes.all { it.isChecked }
        }
    }
}