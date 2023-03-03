package com.rainy.androidcodeutil

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * @author jiangshiyu
 * @date 2023/3/3
 * 处理全屏输入模式下输入框被遮挡问题
 */
class InputHandler {


    private var mRootView: ViewGroup? = null
    private var rootViewParams: ConstraintLayout.LayoutParams? = null
    private var scrollViewParams: ViewGroup.MarginLayoutParams? = null
    private var ignoreViewParams: ViewGroup.MarginLayoutParams? = null

    private var lastRootLayoutHeight = 0
    private var rootViewMargin = 0
    private var scrollViewMargin = 0


    private fun handleInputView(
        rootView: ViewGroup,
        scrollView: ScrollView? = null,
        ignoreView: View? = null,
    ) {
        mRootView = rootView.getChildAt(0) as ViewGroup
        rootViewParams = mRootView?.layoutParams as ConstraintLayout.LayoutParams
        rootViewMargin = rootViewParams?.bottomMargin ?: 0

        mRootView?.viewTreeObserver?.addOnGlobalLayoutListener {
            resizeLayout(scrollView, ignoreView)
        }
        lastRootLayoutHeight = getRootLayoutHeight()

        scrollView?.let {
            scrollViewParams = it.layoutParams as ViewGroup.MarginLayoutParams
            scrollViewMargin = scrollViewParams?.bottomMargin ?: 0
        }

        ignoreView?.let {
            ignoreViewParams = it.layoutParams as ViewGroup.MarginLayoutParams
        }
    }


    private fun resizeLayout(scrollView: ScrollView?, ignoreView: View? = null) {
        val currentRootLayoutHeight = getRootLayoutHeight()
        if (currentRootLayoutHeight != lastRootLayoutHeight) {
            val rootLayoutHeight = mRootView?.rootView?.height ?: 0
            val keyBoardHeight = rootLayoutHeight - currentRootLayoutHeight
            if (keyBoardHeight > rootLayoutHeight / 4) {
                if (scrollView != null) {
                    val height = ignoreView?.height ?: 0
                    val topMargin = ignoreViewParams?.topMargin ?: 0
                    val bottomMargin = ignoreViewParams?.bottomMargin ?: 0
                    scrollViewParams?.bottomMargin =
                        keyBoardHeight - height - topMargin - bottomMargin
                    scrollView.requestLayout()
                } else {
                    rootViewParams?.bottomMargin = rootViewMargin + keyBoardHeight
                    mRootView?.requestLayout()
                }
            }
            lastRootLayoutHeight = currentRootLayoutHeight
        }
    }

    /**
     * 获取根布局高度
     */
    private fun getRootLayoutHeight(): Int {
        val r = Rect()
        mRootView?.getWindowVisibleDisplayFrame(r)
        return r.bottom
    }
}