
import android.content.res.Configuration
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme
import com.mioshek.chartplanner.views.charts.ChartDescription
import com.mioshek.chartplanner.views.charts.ChartSettings


@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    yValues: List<Float>,
    padding: Dp = 16.dp,
    appearance: ChartSettings
) {
    val xValues = (1..yValues.size).toList()
    Box(
        modifier = modifier
            .background(appearance.backgroundColor)
            .padding(padding)
    ) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
        ) {
            if (yValues.isNotEmpty()){
                drawAreaUnderChart(xValues, yValues, appearance.colorAreaUnderChart)
                drawLineChart(xValues, yValues, appearance.lineColor, appearance.lineThickness.toPx())
                if(appearance.isCircleVisible){
                    drawPoints(xValues, yValues, appearance.circleColor, 2.dp)
                }
            }
            drawAxes(xValues, yValues, appearance.graphAxisColor, appearance.axisFontColor, appearance.axisFontSize.toDp(), appearance.lineThickness)
            drawChartDescription(appearance.chartDescription)
        }
    }
}

private fun DrawScope.drawChartDescription(
    description: ChartDescription
){
    val graphNameStartingPoint = Pair(size.width /2, 0 - description.chartNameSize.value * 2)
    drawContext.canvas.nativeCanvas.drawText(
        description.chartName,
        graphNameStartingPoint.first,
        graphNameStartingPoint.second,
        Paint().apply {
            color = description.chartNameColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = description.chartNameSize.toPx()
        }
    )

    val graphAxisXStartingPoint = Pair(size.width / 2, size.height + 100)
    drawContext.canvas.nativeCanvas.drawText(
        description.xAxisName,
        graphAxisXStartingPoint.first,
        graphAxisXStartingPoint.second,
        Paint().apply {
            color = description.axesNamesColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = description.axesNamesSize.toPx()
        }
    )

    val yAxisNameChars = description.yAxisName.toCharArray().reversedArray()
    val charsSize = yAxisNameChars.size
    var y = size.height / 2 - charsSize / 2 * (description.axesNamesSize.value + 20)

    for (char in yAxisNameChars) {
        drawContext.canvas.nativeCanvas.save()

        // Rotate the canvas for each character
        drawContext.canvas.nativeCanvas.rotate(-90f, 0f, 0f)

        // Draw the rotated character
        drawContext.canvas.nativeCanvas.drawText(
            char.toString(),
            -y - 60f,
            -80f,
            Paint().apply {
                color = description.axesNamesColor.toArgb()
                textAlign = Paint.Align.CENTER
                textSize = description.axesNamesSize.toPx()
            }
        )

        drawContext.canvas.nativeCanvas.restore()

        y += description.axesNamesSize.value + 10f
    }

}

private fun DrawScope.drawAxes(
    xValues: List<Int>,
    yValues: List<Float>,
    axisLineColor: Color,
    axisLabelColor: Color,
    axisLabelSize: Dp,
    axisLineThickness: Dp
) {
    // Draw x-axis
    drawLine(
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        color = axisLineColor,
        strokeWidth = axisLineThickness.toPx()
    )

    // Draw y-axis
    drawLine(
        start = Offset(0f, 0f),
        end = Offset(0f, size.height),
        color = axisLineColor,
        strokeWidth = axisLineThickness.toPx()
    )

    // Draw x-axis labels
    if (yValues.size > 0){
        val entryWidth = axisLabelSize + 80.dp
        var partitionsX = size.width / entryWidth.value
        if (partitionsX > xValues.size) {partitionsX = xValues.size.toFloat()
        }
        val xGap = size.width / partitionsX
        val xNumberGap = yValues.size / partitionsX
        var j = 0
        var x = 0f
        while (j < partitionsX){
            drawLine(
                start = Offset(x, size.height),
                end = Offset(x, size.height + 10),
                color = axisLineColor,
                strokeWidth = axisLineThickness.toPx()
            )

            drawContext.canvas.nativeCanvas.drawText(
                "${(j * xNumberGap).toInt()}",
                x,
                size.height + axisLabelSize.toPx() + 4f,
                Paint().apply {
                    color = axisLabelColor.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = axisLabelSize.toPx()
                }
            )
            x += xGap
            j +=1
        }

        // Draw y-axis labels
        val max: Int = yValues.max().toInt()
        val min: Int = yValues.min().toInt()
        val entryHeight = axisLabelSize + 80.dp
        var partitionsY = size.height / entryHeight.value
        if (partitionsY > yValues.size) {partitionsY = yValues.size.toFloat()}
        val yGap = size.height / partitionsY
        val heightGap = (max - min).toFloat() / partitionsY

        var y = 0f
        var heightLabel = max.toFloat()
        var i = 0
        while (i < partitionsY + 1) {
            if (i > partitionsY){
                heightLabel = min.toFloat()
                y = size.height
            }
            // DRAW HERE
            drawLine(
                start = Offset(0f, y),
                end = Offset(10f, y),
                color = axisLineColor,
                strokeWidth = axisLineThickness.toPx()
            )

            drawContext.canvas.nativeCanvas.drawText(
                heightLabel.toInt().toString() /*+ "%"*/,
                -axisLabelSize.toPx() + 5f,
                y + axisLabelSize.toPx() / 2,
                Paint().apply {
                    color = axisLabelColor.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = axisLabelSize.toPx()
                }
            )
            heightLabel -= heightGap
            y += yGap
            i += 1
        }
    }
}

private fun DrawScope.drawLineChart(
    xValues: List<Int>,
    yValues: List<Float>,
    graphColor: Color,
    graphThickness: Float
) {
    if (xValues.size < 2 || yValues.size < 2) {
        return
    }

    val maxValueX = xValues.maxOrNull() ?: 1f
    val minValueX = xValues.minOrNull() ?: 0f
    val maxValueY = yValues.maxOrNull() ?: 1f
    val minValueY = yValues.minOrNull() ?: 0f

    val xRange = maxValueX.toFloat() - minValueX.toFloat()
    val yRange = maxValueY - minValueY

    val xScale = size.width / xRange
    val yScale = size.height / yRange

    val path = Path()

    for (i in xValues.indices) {
        val x = (xValues[i].toFloat() - minValueX.toFloat()) * xScale
        val y = size.height - (yValues[i] - minValueY) * yScale

        if (i == 0) {
            path.moveTo(x+10, y)
        } else {
            path.lineTo(x+10, y)
        }
    }

    drawPath(
        path = path,
        color = graphColor,
        style = Stroke(width = graphThickness)
    )
}

private fun DrawScope.drawPoints(
    xValues: List<Int>,
    yValues: List<Float>,
    pointColor: Color,
    pointRadius: Dp
) {
    val maxValueX = xValues.maxOrNull() ?: 1f
    val minValueX = xValues.minOrNull() ?: 0f
    val maxValueY = yValues.maxOrNull() ?: 1f
    val minValueY = yValues.minOrNull() ?: 0f

    val xRange = maxValueX.toFloat() - minValueX.toFloat()
    val yRange = maxValueY - minValueY

    val xScale = size.width / xRange
    val yScale = size.height / yRange

    for (i in xValues.indices) {
        val x = (xValues[i].toFloat() - minValueX.toFloat()) * xScale
        val y = size.height - (yValues[i] - minValueY) * yScale

        if(yValues[i] != 0f){
            drawCircle(
                color = pointColor,
                radius = pointRadius.toPx(),
                center = Offset(x+10, y)
            )
        }
    }
}

private fun DrawScope.drawAreaUnderChart(
    xValues: List<Int>,
    yValues: List<Float>,
    areaGradient: Pair<Color, Color>?
) {
    if (areaGradient == null) {
        return
    }

    val maxValueX = xValues.maxOrNull() ?: 1f
    val minValueX = xValues.minOrNull() ?: 0f
    val maxValueY = yValues.maxOrNull() ?: 1f
    val minValueY = yValues.minOrNull() ?: 0f

    val xRange = maxValueX.toFloat() - minValueX.toFloat()
    val yRange = maxValueY - minValueY

    val xScale = size.width / xRange
    val yScale = size.height / yRange

    val path = Path()

    for (i in xValues.indices) {
        val x = (xValues[i].toFloat() - minValueX.toFloat()) * xScale
        val y = size.height - (yValues[i] - minValueY) * yScale

        if (i == 0) {
            path.moveTo(x+10, y)
        } else {
            path.lineTo(x+10, y)
        }
    }

    path.lineTo(size.width, size.height)
    path.lineTo(0f, size.height)
    path.close()

    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = listOf(areaGradient.first, areaGradient.second),
            startY = 0f,
            endY = size.height
        )
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LineChartPreview() {
    val yPreviewPoints = listOf(79f,12f,22f,68f,68f,29f,53f,30f,39f,11f,50f,90f,3f,74f,63f,80f,4f,42f,84f,0f,95f,95f,1f,11f,90f,78f,72f,83f,97f,2f,20f,1f,19f,12f,77f,45f,87f,69f,40f,7f,73f,77f,96f,47f,23f,14f,10f,20f,46f,37f,1f,100f,70f,38f,6f,18f,16f,60f,17f,58f,15f,91f,99f,14f,40f,71f,80f,97f,79f,39f,19f,49f,20f,18f,81f,54f,11f,100f,95f,21f,37f,37f,13f,100f,36f,70f,10f,47f,90f,81f,99f,58f,59f,10f,70f,32f,48f,15f,73f,24f,68f,72f,12f,22f,31f,33f,40f,46f,95f,22f,33f,47f,57f,66f,26f,27f,92f,76f,81f,35f,47f,37f,90f,71f,79f,10f,74f,84f,21f,38f,82f,29f,38f,34f,48f,36f,39f,21f,83f,50f,44f,0f,5f,100f,44f,76f,30f,57f,82f,94f,27f,67f,4f,85f,60f,47f,10f,94f,38f,41f,84f,41f,26f,74f,77f,47f,62f,53f,83f,53f,16f,56f,36f,10f,38f,23f,1f,28f,43f,14f,76f,17f,69f,70f,96f,94f,89f,10f,32f,2f,46f,69f,7f,21f,52f,51f,88f,6f,80f,84f,69f,46f,39f,22f,77f,64f,42f,32f,50f,25f,89f,31f,94f,53f,18f,4f,58f,30f,23f,93f,98f,62f,87f,93f,5f,11f,5f,65f,8f,40f,96f,97f,16f,82f,4f,56f,68f,10f,44f,78f,50f,81f,39f,82f,19f,10f,61f,99f,84f,73f,61f,20f,24f,95f,82f,56f,63f,32f,32f,25f,71f,45f,30f,30f,37f,61f,40f,73f,45f,40f,60f,43f,39f,72f,46f,72f,41f,92f,9f,87f,25f,52f,27f,72f,52f,73f,46f,12f,26f,41f,76f,71f,92f,73f,98f,59f,41f,71f,48f,84f,94f,68f,34f,92f,46f,34f,60f,32f,45f,54f,46f,54f,50f,89f,50f,100f,32f,10f,17f,95f,1f,51f,97f,82f,60f,36f,62f,10f,10f,81f,49f,92f,71f,7f,78f,92f,54f,37f,34f,56f,89f,74f,33f,10f,25f,73f,89f,63f,43f,64f,41f,7f,60f,33f,64f,94f,87f,23f,44f,96f,20f,15f,51f,36f,99f,66f)

    ChartPlannerTheme {
        Column(
            modifier = Modifier.padding(20.dp)
        ){
            LineChart(
                yValues= yPreviewPoints,
                appearance = ChartSettings(
                    chartDescription = ChartDescription(
                        chartName = "GraphName",
                        chartNameSize = 40.dp,
                        chartNameColor =  MaterialTheme.colorScheme.onSurface,
                        xAxisName = "XAxisName",
                        yAxisName= "YAxisName",
                        axesNamesSize = 20.dp,
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
                    axisFontSize = 15,
                    axisFontColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
