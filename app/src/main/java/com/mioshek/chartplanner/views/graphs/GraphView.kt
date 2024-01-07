package com.mioshek.chartplanner.views.graphs

import LineChart
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mioshek.chartplanner.data.models.ChartDescription
import com.mioshek.chartplanner.data.models.ChartModel
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme

val yPoints = listOf(73f,25f,89f,38f,6f,45f,2f,64f,55f,7f,34f,69f,87f,56f,40f,18f,27f,56f,67f,2f,21f,16f,10f,10f,12f,4f,29f,50f,88f,96f,29f,91f,80f,55f,95f,22f,48f,0f,27f,89f,77f,69f,72f,80f,42f,66f,52f,63f,53f,1f,3f,88f,73f,92f,81f,67f,67f,40f,74f,68f,51f,51f,93f,30f,55f,26f,86f,51f,28f,1f,12f,89f,90f,41f,35f,58f,75f,100f,68f,44f,78f,27f,21f,5f,77f,92f,90f,15f,80f,92f,42f,90f,83f,6f,2f,89f,16f,28f,40f,56f,100f,50f,35f,89f,43f,64f,66f,13f,66f,58f,21f,71f,6f,45f,32f,94f,72f,39f,89f,21f,36f,14f,10f,96f,89f,6f,74f,66f,92f,52f,83f,94f,70f,40f,47f,69f,55f,76f,10f,41f,32f,9f,89f,33f,30f,14f,71f,30f,74f,49f,96f,29f,78f,76f,28f,22f,45f,89f,1f,50f,21f,95f,35f,96f,48f,75f,13f,49f,90f,30f,53f,73f,69f,26f,71f,5f,29f,48f,50f,10f,21f,80f,12f,18f,25f,89f,28f,35f,88f,87f,77f,16f,21f,7f,58f,99f,8f,54f,53f,21f,25f,80f,100f,6f,84f,93f,56f,54f,69f,89f,29f,20f,57f,80f,69f,57f,86f,99f,43f,39f,85f,48f,70f,70f,43f,13f,11f,44f,94f,47f,81f,61f,93f,41f,11f,92f,95f,90f,45f,84f,48f,43f,43f,53f,4f,1f,16f,11f,19f,5f,61f,40f,40f,12f,100f,9f,27f,26f,23f,38f,30f,84f,62f,90f,93f,5f,3f,71f,11f,68f,91f,91f,40f,94f,35f,69f,30f,78f,66f,20f,26f,38f,50f,28f,62f,16f,65f,92f,31f,33f,6f,68f,34f,39f,96f,81f,70f,22f,99f,60f,51f,1f,52f,78f,7f,80f,29f,90f,27f,25f,71f,7f,60f,65f,79f,45f,25f,40f,32f,37f,81f,82f,23f,87f,78f,71f,68f,42f,28f,97f,14f,95f,82f,30f,2f,54f,16f,23f,86f,24f,86f,21f,91f,7f,88f,22f,65f,79f,16f,100f,7f,6f,48f,8f,21f,13f,83f,34f,1f,93f,68f,88f,25f,12f,80f,74f)

@Composable
fun DrawGraph(modifier: Modifier = Modifier){

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            Column(

                modifier = modifier
                    .fillMaxHeight(0.6f)
                    .padding(top = 20.dp, bottom = 10.dp, start = 40.dp, end = 10.dp)

            ) {
                LineChart(
                    yValues= yPoints,
                    appearance = ChartModel(
                        chartDescription = ChartDescription(
                            chartName = "Successful",
                            chartNameSize = 20.dp,
                            chartNameColor =  MaterialTheme.colorScheme.onSurface,
                            xAxisName = "Days",
                            yAxisName= "Percentage",
                            axesNamesSize = 10.dp,
                            axesNamesColor = MaterialTheme.colorScheme.onSurface
                        ),
                        lineColor = MaterialTheme.colorScheme.onPrimary,
                        graphAxisColor = MaterialTheme.colorScheme.secondary,
                        lineThickness = 1.dp,
                        hasColorAreaUnderChart = true,
                        colorAreaUnderChart = Pair(MaterialTheme.colorScheme.primary, Color.Transparent),
                        isCircleVisible = true,
                        circleColor = MaterialTheme.colorScheme.tertiary,
                        backgroundColor = MaterialTheme.colorScheme.background,
                        axisFontSize = 40,
                        axisFontColor = MaterialTheme.colorScheme.primary
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