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
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Receipt
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
import com.example.expensetracker.ui.theme.LightCyan
import com.example.expensetracker.ui.theme.PrimaryCyan
import com.example.expensetracker.ui.theme.TextDark
import com.example.expensetracker.ui.theme.TextGray


val BarChartUnselected = Color(0xFFB2EBF2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingAnalysisScreen() {
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
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = TextGray
                    )
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

            TimeFilterSelector()

            Spacer(modifier = Modifier.height(32.dp))

            WeeklyBarChart()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Top Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            CategoryItem(
                name = "Food",
                percentage = "35% of total",
                icon = Icons.Rounded.Fastfood,
                progress = 0.35f,
                color = PrimaryCyan
            )
            CategoryItem(
                name = "Rent",
                percentage = "15% of total",
                icon = Icons.Rounded.Home,
                progress = 0.15f,
                color = PrimaryCyan
            )
            CategoryItem(
                name = "Utility Bills",
                percentage = "30% of total",
                icon = Icons.Rounded.Receipt,
                progress = 0.30f,
                color = PrimaryCyan
            )
        }
    }
}


@Composable
fun TimeFilterSelector() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Week", "Month", "Year")

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
        options.forEachIndexed { index, text ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) PrimaryCyan else Color.Transparent)
                    .clickable { selectedIndex = index },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun WeeklyBarChart() {

    val data = listOf(0.4f, 0.3f, 0.6f, 0.5f)
    val days = listOf("Semaine 1", "Semaine 2", "Semaine 3", "Semaine 4")


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEachIndexed { index, value ->

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
                    text = days[index],
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
    SpendingAnalysisScreen()
}