package com.mioshek.chartplanner.data.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class GraphAppearance(
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