package com.example.expensetracker.presentation.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.ui.theme.LightCyan
import com.example.expensetracker.ui.theme.PrimaryCyan
import com.example.expensetracker.ui.theme.TextDark
import com.example.expensetracker.ui.theme.TextGray
import com.example.expensetracker.utils.toPercentageString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingAnalysisScreen(
    analysisViewModel: AnalysisViewModel = hiltViewModel(),
    backToHome: () -> Unit
) {

    val state by analysisViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Spending Analysis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                navigationIcon = {
                    IconButton(
                        onClick = backToHome
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            TimeFilterSelector(
                onSelectedChange = analysisViewModel::onSelectedPeriodChanged
            )


            Spacer(modifier = Modifier.height(32.dp))

            WeeklyBarChart(state.dataCharts)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Top Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            state.topCategories.forEach { categoryUiState ->
                CategoryItem(
                    name = categoryUiState.name,
                    icon = categoryUiState.icon,
                    progress = categoryUiState.percent,
                    color = PrimaryCyan,
                    percentage = categoryUiState.percent.toPercentageString()
                )
            }
        }
    }
}


@Composable
fun TimeFilterSelector(onSelectedChange: (StatisticPeriod) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(24.dp))
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF5F5F5))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatisticPeriod.entries.forEachIndexed { index, text ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) PrimaryCyan else Color.Transparent)
                    .clickable {
                        selectedIndex = index
                        onSelectedChange(text)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text.displayName,
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun WeeklyBarChart(dataReports: Map<String, Float>) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {

        for ((key, value) in dataReports) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxHeight()
            ) {

                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .fillMaxHeight(value)
                        .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                        .background(PrimaryCyan)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = key,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGray,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    name: String,
    percentage: String,
    icon: ImageVector,
    progress: Float,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFAFAFA))
                .border(1.dp, Color.Transparent, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextGray,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))


        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = percentage,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )
        }

        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(40.dp),
                color = color,
                strokeWidth = 3.dp,
                trackColor = LightCyan,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    SpendingAnalysisScreen {}
}