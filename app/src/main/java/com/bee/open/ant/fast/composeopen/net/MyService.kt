package com.bee.open.ant.fast.composeopen.net

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("MyService", "Service Created")
    }

    // 当用户从最近任务列表中移除应用时会调用此方法
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("MyService", "App was removed from recent tasks")
        // 在这里执行应用被移除时的操作，比如清理资源或保存数据
        stopSelf()  // 停止服务
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null  // 这里不需要绑定服务
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyService", "Service Destroyed")
    }
}
