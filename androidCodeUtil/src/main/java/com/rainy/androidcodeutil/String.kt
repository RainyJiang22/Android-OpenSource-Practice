package com.rainy.androidcodeutil

import android.graphics.Color
import android.text.format.Formatter
import androidx.core.util.PatternsCompat
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */

//手机号
const val REGEX_PHONE_EXACT: String =
    "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$"

//银行账户
const val REGEX_ID_CARD_15: String =
    "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$"

//身份证号码
const val REGEX_ID_CARD_18: String =
    "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$"

inline val randomUUIDString: String
    get() = UUID.randomUUID().toString()

fun Long.toFileSizeString(): String =
    Formatter.formatFileSize(application, this)

fun Long.toShortFileSizeString(): String =
    Formatter.formatShortFileSize(application, this)

fun String.parseColor(): Int =
    Color.parseColor(this)

fun String.limitLength(length: Int): String =
    if (this.length <= length) this else substring(0, length)

fun String.isPhone(): Boolean =
    REGEX_PHONE_EXACT.toRegex().matches(this)

fun String.isDomainName(): Boolean =
    PatternsCompat.DOMAIN_NAME.matcher(this).matches()

fun String.isEmail(): Boolean =
    PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

fun String.isIP(): Boolean =
    PatternsCompat.IP_ADDRESS.matcher(this).matches()

fun String.isWebUrl(): Boolean =
    PatternsCompat.WEB_URL.matcher(this).matches()

fun String.isIDCard15(): Boolean =
    REGEX_ID_CARD_15.toRegex().matches(this)

fun String.isIDCard18(): Boolean =
    REGEX_ID_CARD_18.toRegex().matches(this)

fun String.isJson():Boolean =
    try {
        JSONObject(this)
        true
    }catch (e:Exception) {
        false
    }

fun Float.toNumberString(fractionDigits: Int = 2, minIntDigits: Int = 1, isGrouping: Boolean = false, isHalfUp: Boolean = true): String =
    toDouble().toNumberString(fractionDigits, minIntDigits, isGrouping, isHalfUp)

fun Double.toNumberString(fractionDigits: Int = 2, minIntDigits: Int = 1, isGrouping: Boolean = false, isHalfUp: Boolean = true): String =
    (NumberFormat.getInstance() as DecimalFormat).apply {
        isGroupingUsed = isGrouping
        roundingMode = if (isHalfUp) RoundingMode.HALF_UP else RoundingMode.DOWN
        minimumIntegerDigits = minIntDigits
        minimumFractionDigits = fractionDigits
        maximumFractionDigits = fractionDigits
    }.format(this)

