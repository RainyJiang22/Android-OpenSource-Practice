package com.rainy.androidcodeutil

import android.os.Build

/**
 * @author jiangshiyu
 * @date 2023/1/30
 */
//判断厂商品牌


inline val isXiaomiRom: Boolean
    get() = isRomOf("xiaomi")

inline val isOppoRom: Boolean
    get() = isRomOf("oppo")

inline val isVivoRom: Boolean
    get() = isRomOf("vivo")

inline val isOnePlusRom: Boolean
    get() = isRomOf("oneplus")

inline val isSmartisanRom: Boolean
    get() = isRomOf("smartisan", "deltainno")

inline val isMeiZuRom: Boolean
    get() = isRomOf("meizu")

inline val isSamsungRom: Boolean
    get() = isRomOf("samsung")

inline val isGoogleRom: Boolean
    get() = isRomOf("google")

inline val isSonyRom: Boolean
    get() = isRomOf("sony")

fun isRomOf(vararg names: String): Boolean =
    names.any { it.contains(Build.BOARD, true) || it.contains(Build.MANUFACTURER, true) }


//鸿蒙os
val isHarmonyOS: Boolean
    get() {
        try {
            val clazz = Class.forName("com.huawei.system.BuildEx")
            val classLoader = clazz.classLoader
            if (classLoader != null && classLoader.parent == null) {
                return clazz.getMethod("getOsBrand").invoke(clazz) == "harmony"
            }
        } catch (e: ClassNotFoundException) {
        } catch (e: NoSuchMethodException) {
        } catch (e: Exception) {
        }
        return false
    }