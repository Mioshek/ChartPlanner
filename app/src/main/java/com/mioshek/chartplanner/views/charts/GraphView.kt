package com.mioshek.chartplanner.views.charts

import LineChart
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mioshek.chartplanner.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@Composable
fun DrawGraph(
    chartViewModel: ChartViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
){
    var currentHorizontalDrag by remember { mutableStateOf(Pair(0.dp, 0.dp)) }
    val coroutineScope = rememberCoroutineScope()
    val chartUiState by chartViewModel.chartUiState.collectAsState()
    var refresh by remember{ mutableStateOf(true)}
    if (refresh){
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .zIndex(1f)
        ){
            CircularProgressIndicator(
                modifier = modifier.size(50.dp),
                color = Color.White
            )
        }

        coroutineScope.launch {
            refresh = chartViewModel.calculateValuesForChart()
        }
    }

    Box(
        Modifier.pointerInput(Unit){
            detectDragGestures (
                onDrag = {
                        change, dragAmount ->
                    change.consume()

                    val (x, y) = dragAmount
                    when {
                        x > 0 -> {
                            var chartDrag = if (x < 10) x else 10F
                            if (chartDrag + currentHorizontalDrag.second.value > 10){
                                chartDrag = 0F
                            }
                            currentHorizontalDrag = Pair(
                                currentHorizontalDrag.first - chartDrag.toDp(),
                                currentHorizontalDrag.second + chartDrag.toDp()
                            )
                        }
                        x < 0 -> {
                            var chartDrag = if (-x < 20) -x else 20F
                            if (chartDrag + currentHorizontalDrag.first.value > 20){
                                chartDrag = 0F
                            }
                            currentHorizontalDrag = Pair(
                                currentHorizontalDrag.first + chartDrag.toDp(),
                                currentHorizontalDrag.second - chartDrag.toDp()
                            )
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
        var selected by remember{ mutableStateOf(CustomTimestamp.YEAR)}
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(50.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                for (timestamp in CustomTimestamp.values()){
                    var backgroundColor = MaterialTheme.colorScheme.surface
                    if (selected == timestamp){
                        backgroundColor = MaterialTheme.colorScheme.background
                    }
                    Box(
                        modifier = modifier
                            .weight(1f)
                            .background(backgroundColor, RoundedCornerShape(5.dp))
                            .padding(top = 5.dp, bottom = 5.dp)
                            .clickable {
                                selected = timestamp
                            }
                    ){
                        Text(
                            text = timestamp.name,
                            modifier = modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            Column(

                modifier = modifier
                    .fillMaxHeight(0.6f)
                    .padding(
                        top = 20.dp,
                        bottom = 10.dp,
                        start = 40.dp - currentHorizontalDrag.first,
                        end = 10.dp - currentHorizontalDrag.second
                    )

            ) {
                LineChart(
                    yValues= chartUiState.yValues,
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
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        axisFontSize = 40,
                        axisFontColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }
    }
}


//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun BottomNavigationBarPreview() {
//    ChartPlannerTheme {
//        DrawGraph()
//    }
//}