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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mioshek.chartplanner.ui.AppViewModelProvider
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme
import com.mioshek.chartplanner.views.habits.ListHabitsViewModel

@Composable
fun DrawGraph(
    chartViewModel: ChartViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
){
    var currentHorizontalDrag by remember { mutableStateOf(Pair(0.dp, 0.dp)) }
    val yPoints by remember {mutableStateOf(chartViewModel.chartUiState.value.yValues)}
    chartViewModel.calculateValuesForChart()
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