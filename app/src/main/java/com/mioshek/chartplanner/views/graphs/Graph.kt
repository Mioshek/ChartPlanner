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
import com.mioshek.chartplanner.data.models.GraphAppearance
import com.mioshek.chartplanner.ui.theme.ChartPlannerTheme


@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    yValues: List<Float>,
    padding: Dp = 16.dp,
    appearance: GraphAppearance
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
            drawAxes(xValues, yValues, appearance.graphAxisColor, appearance.axisFontColor, appearance.axisFontSize.toDp(), appearance.lineThickness)
            drawAreaUnderChart(xValues, yValues, appearance.colorAreaUnderChart)
            drawLineChart(xValues, yValues, appearance.lineColor, appearance.lineThickness.toPx())
            drawPoints(xValues, yValues, appearance.circleColor, 2.dp)
        }
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
    val entryWidth = axisLabelSize + 80.dp
    val partitionsX = size.width / entryWidth.value
    val xGap = size.width / partitionsX
    val xNumberGap = yValues.size / partitionsX
    var j = 0

    while (j < partitionsX){
        val x = j * xGap
        drawLine(
            start = Offset(x, size.height),
            end = Offset(x, size.height + 10),
            color = axisLineColor,
            strokeWidth = axisLineThickness.toPx()
        )

        drawContext.canvas.nativeCanvas.drawText(
            (j * xNumberGap).toInt().toString(),
            x,
            size.height + axisLabelSize.toPx() + 4f,
            Paint().apply {
            color = axisLabelColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = axisLabelSize.toPx()
            }
        )
        j +=1
    }

    // Draw y-axis labels with scallable
    val max: Int = yValues.max().toInt()
    val min: Int = yValues.min().toInt()
    val entryHeight = axisLabelSize + 50.dp
    val partitionsY = size.height / entryHeight.value
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
            -axisLabelSize.toPx() - 8f,
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

        drawCircle(
            color = pointColor,
            radius = pointRadius.toPx(),
            center = Offset(x+10, y)
        )
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
    val xPreviewPoints = listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,346,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,364,365,366)

    ChartPlannerTheme {
        Column(
            modifier = Modifier.padding(20.dp)
        ){
            LineChart(
                yValues= yPreviewPoints,
                appearance = GraphAppearance(
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.secondary,
                    1.dp,
                    true,
                    Pair(MaterialTheme.colorScheme.primary, Color.Transparent),
                    true,
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.background,
                    15,
                    MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
