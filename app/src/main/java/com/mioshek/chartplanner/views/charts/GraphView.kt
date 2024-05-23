package com.mioshek.chartplanner.views.charts

import LineChart
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mioshek.chartplanner.R
import com.mioshek.chartplanner.assets.formats.DateFormatter
import com.mioshek.chartplanner.ui.AppViewModelProvider
import kotlinx.coroutines.delay
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
    var dragEnded by remember { mutableStateOf(true)}
    var slideLeft by remember { mutableStateOf(true)}
    var initialized by remember{ mutableStateOf(false)}
    if(!initialized){
        coroutineScope.launch {
            chartViewModel.calculateValuesForChart()
        }
        initialized = false
    }

    Box(
        Modifier.pointerInput(Unit){
            detectDragGestures (
                onDrag = {
                        change, dragAmount ->
                    change.consume()

                    val (x, _) = dragAmount
                    when {
                        x > 0 -> {
                            if (dragEnded && chartUiState.firstDay - 1 > 19723){
                                chartViewModel.changeDays(false)
                                slideLeft = true
                                dragEnded = false
                            }
                        }
                        x < 0 -> {
                            if(dragEnded){
                                chartViewModel.changeDays(true)
                                slideLeft = false
                            }
                            dragEnded = false
                        }
                    }
                },
                onDragEnd = {
                    if (chartUiState.numberOfDays !in CustomTimestamp.values().map { it.range }){
                        chartViewModel.changeTimestamp(CustomTimestamp.YEAR)
                    }
                    coroutineScope.launch {
                        delay(100)
                        dragEnded = true
                    }
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
                    var backgroundColor = MaterialTheme.colorScheme.tertiary.copy(0.4f)
                    if (selected == timestamp){
                        backgroundColor = MaterialTheme.colorScheme.tertiary
                    }
                    var name = ""
                    when(timestamp.name){
                        "WEEK" -> {name = stringResource(R.string.week)
                        }
                        "MONTH" -> {name = stringResource(R.string.month)
                        }
                        "YEAR" -> {name = stringResource(R.string.year)
                        }
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Box(
                        modifier = modifier
                            .weight(1f)
                            .background(backgroundColor, RoundedCornerShape(5.dp))
                            .padding(top = 5.dp, bottom = 5.dp)
                            .clickable {
                                selected = timestamp
                                chartViewModel.changeTimestamp(timestamp)
                            }
                    ){
                        Text(
                            text = name,
                            modifier = modifier
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onTertiary
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
                var showCircles by remember {
                    mutableStateOf(true)
                }
                LaunchedEffect(key1=null){
                    try {
                        showCircles = chartViewModel.getSettingValue(R.string.show_circles_on_graph).toBoolean()
                    }
                    catch (e: Exception){
                        Log.d("Setting Error", e.toString())
                    }
                }

                AnimatedVisibility(
                    visible = dragEnded,
                    enter = slideInHorizontally(animationSpec = keyframes {
                        this.durationMillis = 50
                    }
                    ) { fullWidth ->
                        if (slideLeft) -fullWidth / 3 else fullWidth / 3
                    },
                    exit = slideOutHorizontally(animationSpec = keyframes {
                        this.durationMillis = 50
                    }
                    ) { fullWidth ->
                        if (slideLeft) fullWidth / 3 else -fullWidth / 3
                    },
                ) {
                    LineChart(
                        yValues= chartUiState.yValues,
                        appearance = ChartSettings(
                            chartDescription = ChartDescription(
                                chartName = "${DateFormatter.sdf.format(chartUiState.firstDay.toLong() * 86400000L).substring(0,10)} - ${DateFormatter.sdf.format((chartUiState.firstDay.toLong() + chartUiState.numberOfDays)*86400000).substring(0,10)}",
                                chartNameSize = 20.dp,
                                chartNameColor =  MaterialTheme.colorScheme.onBackground,
                                xAxisName = stringResource(R.string.days),
                                yAxisName= stringResource(R.string.percentage),
                                axesNamesSize = 10.dp,
                                axesNamesColor = MaterialTheme.colorScheme.secondary
                            ),
                            lineColor = MaterialTheme.colorScheme.primary,
                            graphAxisColor = MaterialTheme.colorScheme.onBackground,
                            lineThickness = 1.dp,
                            hasColorAreaUnderChart = true,
                            colorAreaUnderChart = Pair(MaterialTheme.colorScheme.primary.copy(0.8f), Color.Transparent),
                            isCircleVisible = showCircles,
                            circleColor = MaterialTheme.colorScheme.tertiary,
                            backgroundColor = Color.Transparent,
                            axisFontSize = 40,
                            axisFontColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }
    }
}