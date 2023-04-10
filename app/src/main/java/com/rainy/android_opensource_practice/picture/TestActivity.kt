package com.rainy.android_opensource_practice.picture

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.rainy.android_opensource_practice.BaseActivity
import com.rainy.android_opensource_practice.MainActivity
import com.rainy.android_opensource_practice.R
import com.rainy.android_opensource_practice.databinding.ActivityTestBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import kotlin.collections.ArrayList

/**
 * @author jiangshiyu
 * @date 2022/11/16
 */
class TestActivity : BaseActivity<BaseViewModel, ActivityTestBinding>() {

    companion object {
        const val TAG = "TestActivity"


        private const val mHighNotificationId = 9002
        private const val mStopAction = "com.widget.stop" // 暂停继续action
        private const val mDoneAction = "com.widget.done" // 完成action
        private var mFlag = 0
        private var mIsStop = false // 是否在播放 默认未开始

    }


    private val mHighChannelId = "重要渠道id"
    private val mHighChannelName = "重要通知"
    private var mReceiver: NotificationReceiver? = null

    private lateinit var mManager: NotificationManager
    private lateinit var mBuilder: NotificationCompat.Builder


    override fun initView(savedInstanceState: Bundle?) {

        mManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createReceiver()


        mViewBind?.btnAlbum?.setOnClickListener {

            PictureSelector.create(this)
                .openSystemGallery(SelectMimeType.ofAll())
                .setSelectionMode(SelectModeConfig.SINGLE)
                .forSystemResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: ArrayList<LocalMedia>?) {
                        Log.d(TAG, "onResult: ${result.toString()}")
                    }

                    override fun onCancel() {
                        Log.d(TAG, "onCancel: ")
                    }

                })
        }

        mViewBind?.btnNotification?.setOnClickListener {
            createNotificationForMessage()
        }
    }


    /**
     * 发起重要通知
     */
    private fun createNotificationForMessage() {

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(mHighChannelId,
                mHighChannelName,
                NotificationManager.IMPORTANCE_HIGH)
            channel.setShowBadge(true)
            mManager.createNotificationChannel(channel)
        }
        mBuilder = NotificationCompat.Builder(this, mHighChannelId)
            .setContentTitle("消息通知")
            .setContentText("你提交的反馈有了新的回复")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setAutoCancel(true)
            .addAction(R.mipmap.ic_launcher, "去看看", pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE) // 屏幕可见性，锁屏时，显示icon和标题，内容隐藏
        mManager.notify(mHighNotificationId, mBuilder.build())
    }


    /**
     * 创建广播接收器
     */
    private fun createReceiver() {
        val intentFilter = IntentFilter()
        // 添加接收事件监听
        intentFilter.addAction(mStopAction)
        intentFilter.addAction(mDoneAction)
        mReceiver = NotificationReceiver()
        // 注册广播
        registerReceiver(mReceiver, intentFilter)
    }

    private class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 拦截接收事件
            if (intent.action == mStopAction) {
                // 改变状态
                mIsStop = !mIsStop

            } else if (intent.action == mDoneAction) {
                Toast.makeText(context, "完成", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBundle(bundle: Bundle) {

    }

    override fun createObserver() {

    }

}