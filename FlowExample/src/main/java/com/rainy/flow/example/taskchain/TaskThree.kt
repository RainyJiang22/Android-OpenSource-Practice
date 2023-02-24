package com.rainy.flow.example.taskchain

import android.content.Context
import androidx.appcompat.app.AlertDialog

/**
 * @author jiangshiyu
 * @date 2023/2/24
 */
class TaskThree : ChainTask {
    override fun execute(): Boolean {
        return true
    }

    override fun launch(context: Context, callback: () -> Unit) {
        AlertDialog.Builder(context).setMessage("这是第三个弹框")
            .setPositiveButton("ok") { x, y ->
                callback()
            }.show()
    }
}