package com.example.jetpack_compose_app

import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.Process
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.util.Calendar
import java.util.Date


class WidgetViewModel : ViewModel() {
    private val _listOfStatsList = mutableStateListOf<CustomUseStats>()
    val listOfStatsList: MutableList<CustomUseStats> get() = _listOfStatsList

    private fun getUsageStatsList(context: Context): MutableList<UsageStats>? {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - (24 * 60 * 60 * 1000) // 24 hours ago
        return usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )
    }

    fun checkForPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode =
            appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)
        return mode == MODE_ALLOWED
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getScreenTime(context: Context) {
        val customUseStatsList = mutableListOf<CustomUseStats>()
        val usagesList = getUsageStatsList(context)
        usagesList?.map {
            customUseStatsList.add(
                CustomUseStats(
                    name = it.packageName,
                    formatedTimeOfUsed = convertDateToFormattedTime(Date(it.totalTimeVisible)),
                    timeOfUsed = convertDate(Date(it.totalTimeVisible))
                )
            )
        }
        if (_listOfStatsList.isNotEmpty())
            _listOfStatsList.clear()

        _listOfStatsList.addAll(customUseStatsList)
    }


    private fun convertDateToFormattedTime(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date

        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)

        if (seconds < 10)
            return "$minutes:0$seconds"
        else
            return "$minutes:$seconds"
    }
}

private fun convertDate(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    val minutes = calendar.get(Calendar.MINUTE)

    return minutes
}

data class CustomUseStats(
    val name: String = "",
    val formatedTimeOfUsed: String = "",
    val timeOfUsed: Int = 0
)