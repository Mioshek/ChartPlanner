package com.mioshek.chartplanner.views.charts

import LineChart
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme
import kotlin.math.roundToInt

val yPoints = listOf(0f,13f,1f,7f,8f,14f,19f,10f,15f,13f,13f,19f,14f,4f,0f,8f,2f,11f,1f,18f,17f,17f,12f,19f,16f,18f,9f,17f,16f,31f,32f,30f,28f,23f,39f,29f,23f,22f,30f,36f,30f,38f,20f,29f,32f,26f,29f,36f,33f,35f,31f,40f,34f,29f,22f,38f,40f,40f,44f,58f,54f,47f,49f,56f,51f,40f,47f,51f,44f,54f,40f,60f,60f,41f,45f,49f,42f,55f,58f,55f,52f,55f,59f,53f,56f,59f,47f,60f,72f,74f,75f,64f,66f,77f,67f,72f,77f,74f,79f,77f,73f,62f,80f,68f,78f,62f,77f,66f,73f,65f,78f,67f,76f,76f,71f,65f,70f,78f,80f,89f,93f,99f,85f,81f,89f,95f,83f,81f,97f,99f,92f,90f,90f,93f,81f,98f,97f,84f,94f,82f,85f,93f,90f,80f,93f,84f,88f,81f,99f,81f,100f)

@Composable
fun DrawGraph(modifier: Modifier = Modifier){
    var currentHorizontalDrag by remember { mutableStateOf(Pair(0.dp, 0.dp)) }
    Box(
        Modifier.pointerInput(Unit){
            detectDragGestures (
                onDrag = {
                        change, dragAmount ->
                    change.consume()

                    val (x, y) = dragAmount
                    when {
                        x > 0 -> {
                            Log.d("Direction", "Right")
                            var chartDrag = if (x < 10) x else 10F
                            if (chartDrag + currentHorizontalDrag.second.value > 10){
                                chartDrag = 0F
                            }
                            currentHorizontalDrag = Pair(
                                currentHorizontalDrag.first - chartDrag.toDp(),
                                currentHorizontalDrag.second + chartDrag.toDp()
                            )
                            Log.d("Direction", "${currentHorizontalDrag.first}, ${currentHorizontalDrag.second}")
                        }
                        x < 0 -> {
                            Log.d("Direction", "Left")
                            var chartDrag = if (-x < 20) -x else 20F
                            if (chartDrag + currentHorizontalDrag.first.value > 20){
                                chartDrag = 0F
                            }
                            currentHorizontalDrag = Pair(
                                currentHorizontalDrag.first + chartDrag.toDp(),
                                currentHorizontalDrag.second - chartDrag.toDp()
                            )
                            Log.d("Direction", "${currentHorizontalDrag.first}, ${currentHorizontalDrag.second}")
                        }
                    }
                    when {
                        y > 0 -> { Log.d("Direction", "Down") }
                        y < 0 -> { Log.d("Direction", "Up") }
                    }
                },
                onDragEnd = {
                    currentHorizontalDrag = Pair(0.dp, 0.dp)
                }
            )
        }
    ){
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            Column(

                modifier = modifier
                    .fillMaxHeight(0.6f)
                    .padding(top = 20.dp, bottom = 10.dp, start = 40.dp - currentHorizontalDrag.first, end = 10.dp - currentHorizontalDrag.second)

            ) {
                LineChart(
                    yValues= yPoints,
                    appearance = ChartSettings(
                        chartDescription = ChartDescription(
                            chartName = "Successful",
                            chartNameSize = 20.dp,
                            chartNameColor =  MaterialTheme.colorScheme.onSurface,
                            xAxisName = "Days",
                            yAxisName= "Percentage",
                            axesNamesSize = 10.dp,
                            axesNamesColor = MaterialTheme.colorScheme.onSurface
                        ),
                        lineColor = MaterialTheme.colorScheme.secondary,
                        graphAxisColor = MaterialTheme.colorScheme.secondary,
                        lineThickness = 1.dp,
                        hasColorAreaUnderChart = true,
                        colorAreaUnderChart = Pair(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary),
                        isCircleVisible = true,
                        circleColor = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.background,
                        axisFontSize = 40,
                        axisFontColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomNavigationBarPreview() {
    ChartPlannerTheme {
        DrawGraph()
    }
}