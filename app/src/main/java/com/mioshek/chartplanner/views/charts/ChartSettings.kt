package com.mioshek.chartplanner.views.charts

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class ChartSettings(
    val chartDescription: ChartDescription,
    val lineColor: Color,
    val graphAxisColor: Color,
    val lineThickness: Dp,
    val hasColorAreaUnderChart: Boolean,
    val colorAreaUnderChart: Pair<Color,Color>,
    val isCircleVisible: Boolean,
    val circleColor: Color,
    val backgroundColor: Color,
    val axisFontSize: Int,
    val axisFontColor: Color
)

data class ChartDescription(
    val chartName: String,
    val chartNameSize: Dp,
    val chartNameColor: Color,
    val xAxisName: String,
    val yAxisName: String,
    val axesNamesSize: Dp,
    val axesNamesColor: Color
)