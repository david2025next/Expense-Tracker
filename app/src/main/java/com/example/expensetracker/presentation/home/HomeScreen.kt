package com.example.expensetracker.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.R
import com.example.expensetracker.presentation.register.ProfileImagePicker
import com.example.expensetracker.utils.toCurrency
import com.example.expensetracker.utils.toHumanDateToday

@Composable
fun HomeRoute(homeViewModel: HomeViewModel = hiltViewModel(), goToForm: () -> Unit) {

    val state by homeViewModel.state.collectAsStateWithLifecycle()
    val userInfo by homeViewModel.userInfo.collectAsStateWithLifecycle()
    HomeScreen(
        homeUiState = state,
        onNavigationClick = goToForm,
        userInfo = userInfo,
        onPeriodSelected = homeViewModel::selectedPeriodChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    homeUiState: HomeUiState,
    userInfo: UserInfo,
    onPeriodSelected: (PeriodRange) -> Unit,
    onNavigationClick: () -> Unit
) {


    Scaffold(
        topBar = { HomeTopBar(userInfo) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigationClick,
                modifier = Modifier.offset(y = 48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {}
    ) { padding ->


        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {

            item {
                PeriodFilterSelector(
                    periodsFilter = PeriodRange.entries.toTypedArray(),
                    onPeriodSelected = onPeriodSelected
                )
            }

            item {
                BalanceCard(
                    balance = homeUiState.balance,
                    percent = homeUiState.percent
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = homeUiState.period.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "-${homeUiState.totalsSpentForPeriod.toCurrency()}F",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            items(homeUiState.transactions) {
                TransactionItem(it)
            }
        }
    }

}

private fun Dp.toPx(): Float = this.value


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(userInfo: UserInfo) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Salut ${userInfo.username}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Suivi de vos finances",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        navigationIcon = {
            ProfileImagePicker(
                size = 40.dp,
                iconSize = 0.dp,
                imageUri = userInfo.imageProfile,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    )
}


@Composable
private fun BalanceCard(
    balance: Long,
    percent: Float
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.totalbalance),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "${balance.toCurrency()} FCFA",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { percent },
                modifier = Modifier.fillMaxWidth(),
                color = if (percent > 0.5f) MaterialTheme.colorScheme.error else ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
        }
    }
}


@Composable
fun HomeBottomBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.BarChart, null) },
            label = { Text("Stats") }
        )
    }
}

@Composable
fun PeriodFilterSelector(
    periodsFilter: Array<PeriodRange>,
    onPeriodSelected: (PeriodRange) -> Unit,
    modifier: Modifier = Modifier
) {

    var period by rememberSaveable { mutableStateOf(PeriodRange.TODAY.displayName) }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        periodsFilter.forEach { periodFilter ->
            val isSelected = periodFilter.displayName == period
            FilterChip(
                selected = isSelected,
                onClick = {
                    period = periodFilter.displayName
                    onPeriodSelected(periodFilter)
                },
                label = { Text(text = periodFilter.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = MaterialTheme.colorScheme.outline
                )
            )
        }

    }
}

@Composable
fun TransactionItem(
    transactionItemUState: TransactionItemUState,
) {

    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp)),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        headlineContent = {
            Text(
                text = transactionItemUState.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = {
            Row {
                Text(
                    text = transactionItemUState.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (transactionItemUState.displayDate != null) {
                    Text(
                        text = " • ${transactionItemUState.displayDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        leadingContent = {
            CustomIcon(
                icon = transactionItemUState.icon,
                size = 48.dp,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                iconSize = 24.dp
            )
        },
        trailingContent = {
            Text(
                text = "${if (transactionItemUState.isExpense) "-" else "+"}${transactionItemUState.amount.toCurrency()}F",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (transactionItemUState.isExpense) MaterialTheme.colorScheme.error else Color(
                    0xFF2E7D32
                )
            )
        }
    )
}

@Composable
private fun CustomIcon(
    icon: ImageVector,
    size: Dp,
    backgroundColor: Color,
    tint: Color,
    iconSize: Dp,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

class BottomAppBarCutoutShape(private val radiusPx: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width / 2 - radiusPx, 0f)
            cubicTo(
                size.width / 2 - radiusPx / 2f, radiusPx * 1.5f,
                size.width / 2 + radiusPx / 2f, radiusPx * 1.5f,
                size.width / 2 + radiusPx / 2f, 0f,
            )
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }

}

