package com.example.jetpack_compose_app

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@RequiresApi(Build.VERSION_CODES.Q)
class MyWidget : GlanceAppWidget() {
    private val viewModel: WidgetViewModel = WidgetViewModel()
    val isRefreshCount = booleanPreferencesKey("resfreshCount")

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val context = LocalContext.current
            val isRefreshState = currentState(key = isRefreshCount)

            LaunchedEffect(isRefreshState) {
                viewModel.getScreenTime(context)
            }
            GlanceTheme {
                WidgetLayout(appList = viewModel.listOfStatsList)
            }
        }
    }
}

@Composable
private fun WidgetLayout(appList: MutableList<CustomUseStats>) {
    Column(
        modifier = GlanceModifier
            .background(color = Color(0XFFFFFAFA))
            .fillMaxSize()
            .cornerRadius(8.dp)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
    ) {
        val filteredAppList = appList.filter {
            it.name.contains("android").not() &&
            it.name.contains("xiaomi").not() &&
            it.name.contains("miui").not()
        }
        val topThreeOrderedList = filteredAppList.sortedByDescending { it.timeOfUsed }.take(3)
        val listColors = listOf(
            Color(0xFFFFFFFF), Color(0xFF7F7F7F), Color(0xFFA0A0A0)
        )
        val totalTime = topThreeOrderedList.sumOf { it.timeOfUsed }

        Header(totalTime = "$totalTime min")
        Spacer(modifier = GlanceModifier.height(16.dp))
        topThreeOrderedList.mapIndexed { index, customUseStats ->
            TimeBadge(
                appName = formatName(customUseStats.name),
                appTimeUsed = topThreeOrderedList[index].formatedTimeOfUsed,
                color = listColors[index],
                isFirst = index == 0
            )
        }
    }
}

@Composable
private fun TimeBadge(
    appName: String,
    appTimeUsed: String,
    color: Color,
    isFirst: Boolean = false
) {
    Row(
        modifier = GlanceModifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = GlanceModifier
                .background(color)
                .padding(4.dp)
                .cornerRadius(50.dp)
        ) {
            Text(
                text = "$appTimeUsed min",
                style = TextStyle(
                    color = ColorProvider(
                        color = if (isFirst) Color(0xFF000000) else Color(0xFFFFFFFF)
                    ),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
        Spacer(modifier = GlanceModifier.width(16.dp))
        Text(
            text = appName,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.End
            ),
            modifier = GlanceModifier.fillMaxWidth()
        )
    }
}

@Composable
fun Header(totalTime: String) {
    Column(
        modifier = GlanceModifier
            .height(64.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tempo de tela",
                style = TextStyle(
                    color = ColorProvider(
                        color = Color(0xFF000000)
                    ),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start
                ),
                modifier = GlanceModifier
                    .padding(start = 8.dp)
            )
            Spacer(GlanceModifier.width(20.dp))
            Image(
                provider = ImageProvider(
                    resId = R.drawable.refresh_icon
                ),
                contentDescription = null,
                modifier = GlanceModifier
                    .clickable(
                        onClick = actionRunCallback<RefreshAction>()
                    )
                    .size(24.dp)
            )
        }

        Text(
            text = totalTime,
            style = TextStyle(
                color = ColorProvider(
                    color = Color(0xFF000000)
                ),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Start
            ),
            modifier = GlanceModifier
                .padding(start = 8.dp)
                .fillMaxWidth()
        )
    }
}

private fun formatName(appName: String): String {
    val appNameSplit = appName.split(".")
    return appNameSplit[appNameSplit.lastIndex]
}

@RequiresApi(Build.VERSION_CODES.Q)
object RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val isRefresh = prefs[MyWidget().isRefreshCount]
            prefs[MyWidget().isRefreshCount] = isRefresh != true
        }
        MyWidget().update(context, glanceId)
    }
}

