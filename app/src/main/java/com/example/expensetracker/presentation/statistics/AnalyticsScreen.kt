package com.example.expensetracker.presentation.statistics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine


@Preview(showBackground = true)
@Composable
private fun TestLineChart(){
    // 1. D'abord, crée tes points de données
    val points = listOf(
        Point(x = 0f, y = 10f),
        Point(x = 1f, y = 25f),
        Point(x = 2f, y = 15f),
        Point(x = 3f, y = 40f),
        Point(x = 4f, y = 30f)
    )

// 2. Crée une ligne avec ces points
    val line = Line(
        dataPoints = points,
        lineStyle = LineStyle(color = Color.Blue),
        intersectionPoint = IntersectionPoint(),
        selectionHighlightPoint = SelectionHighlightPoint(),
        shadowUnderLine = ShadowUnderLine(),
        selectionHighlightPopUp = SelectionHighlightPopUp()
    )

// 3. Crée le LinePlotData
    val linePlotData = LinePlotData(
        lines = listOf(line)
    )

// 4. Configure les axes (optionnel)
    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .steps(points.size - 1)
        .labelData { i -> i.toString() }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelData { i -> (i * 10).toString() }
        .build()

// 5. Crée le LineChartData final
    val lineChartData = LineChartData(
        linePlotData = linePlotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color.White
    )

// 6. Affiche le graphique
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )
}