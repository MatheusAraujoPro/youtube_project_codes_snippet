package com.example.jetpack_compose_app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class MyAppWidget  : GlanceAppWidgetReceiver() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override val glanceAppWidget: GlanceAppWidget = MyWidget()
}