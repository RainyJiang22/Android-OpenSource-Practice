package com.rainy.androidcodeutil

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * @author jiangshiyu
 * @date 2023/1/29
 */

/**
 * 剪切板工具
 */

fun CharSequence.copyToClipBoard(label: CharSequence? = null) =
    (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .setPrimaryClip(ClipData.newPlainText(label, this))


/*
fun Uri.copyToClipboard(label: CharSequence? = null) =
    (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .setPrimaryClip(ClipData.newUri(contentResolver, label, this))
*/


fun Intent.copyToClipBoard(label: CharSequence? = null) =
    (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .setPrimaryClip(ClipData.newIntent(label, this))


/**
 * 从剪切板获取文字
 */
fun getTextFromClipBoard(): CharSequence? =
    (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .primaryClip?.takeIf { it.itemCount == 0 }?.getItemAt(0)?.coerceToText(application)

fun clearClipBoard() =
    (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .setPrimaryClip(ClipData.newPlainText(null, ""))

fun doOnClipboardChanged(listener: ClipboardManager.OnPrimaryClipChangedListener): ClipboardManager.OnPrimaryClipChangedListener =
    (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .addPrimaryClipChangedListener(listener).let { listener }

fun ClipboardManager.OnPrimaryClipChangedListener.cancel() =
    (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .removePrimaryClipChangedListener(this)