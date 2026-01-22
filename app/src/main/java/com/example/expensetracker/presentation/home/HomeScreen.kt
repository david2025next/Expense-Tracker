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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.R
import com.example.expensetracker.domain.model.Period
import com.example.expensetracker.presentation.register.ProfileImagePicker
import com.example.expensetracker.utils.toCurrency

@Composable
fun HomeRoute(homeViewModel: HomeViewModel = hiltViewModel(), goToForm: () -> Unit) {

    val balanceSummaryAndDailyTotals by homeViewModel.balanceSummaryAndDailyTotals.collectAsStateWithLifecycle()
    val transactions by homeViewModel.transactions.collectAsStateWithLifecycle()
    val userInfo by homeViewModel.userInfo.collectAsStateWithLifecycle()
    HomeScreen(
        balanceSummaryAndDailyTotals = balanceSummaryAndDailyTotals,
        transactions = transactions,
        onNavigationClick = goToForm,
        userInfo = userInfo,
        onPeriodSelected = homeViewModel::selectedPeriodChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    balanceSummaryAndDailyTotals: BalanceSummaryAndDailyTotalsUiState,
    transactions: List<TransactionItemUState>,
    userInfo: UserInfo,
    onPeriodSelected: (PeriodRange) -> Unit,
    onNavigationClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Column(
                        modifier = Modifier.padding(start = 5.dp)
                    ) {
                        Text(
                            text = "Salut ${userInfo.username}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Faites le suivi de vos revenus et depenses",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                navigationIcon = {
                    ProfileImagePicker(
                        size = 50.dp,
                        iconSize = 0.dp,
                        imageUri = userInfo.imageProfile,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigationClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    "Add"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.padding(16.dp)) {
                TransactionOverviewCard(
                    totalBalance = balanceSummaryAndDailyTotals.balance,
                    totalIncome = balanceSummaryAndDailyTotals.income,
                    totalExpense = balanceSummaryAndDailyTotals.expense
                )
            }

            DailySpendIndicator(
                balanceSummaryAndDailyTotals.totalSpentTodayIncome,
                balanceSummaryAndDailyTotals.totalSpentTodayExpense
            )

            Spacer(modifier = Modifier.height(16.dp))

            PeriodFilterSelector(
                periodsFilter = PeriodRange.entries.toTypedArray(),
                onPeriodSelected = onPeriodSelected
            )

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                }
            }
        }
    }
}

@Composable
private fun Header(image: ImageBitmap, name: String) {
}

@Composable
private fun TransactionOverviewCard(
    totalBalance: Long,
    totalIncome: Long,
    totalExpense: Long,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.totalbalance),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${totalBalance.toCurrency()} FCFA",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OverviewStatItem(
                    label = stringResource(R.string.revenuDisplayName),
                    amount = totalIncome,
                    icon = Icons.Default.ArrowUpward,
                    color = Color(0xFF2E7D32)
                )
                OverviewStatItem(
                    label = stringResource(R.string.expenseDisplayName),
                    amount = totalExpense,
                    icon = Icons.Default.ArrowDownward,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun OverviewStatItem(
    label: String,
    amount: Long,
    icon: ImageVector,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CustomIcon(
            icon = icon,
            size = 32.dp,
            backgroundColor = color.copy(alpha = 0.15f),
            tint = color,
            iconSize = 18.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${amount.toCurrency()} F",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun DailySpendIndicator(totalSpentTodayIncome: Long, totalSpentTodayExpense: Long) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.today),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TransactionTodaySummary(
                Icons.Default.ArrowUpward,
                stringResource(R.string.enterTransaction),
                totalSpentTodayIncome,
                Color(0xFF2E7D32)
            )
            TransactionTodaySummary(
                Icons.Default.ArrowDownward,
                stringResource(R.string.outTransaction),
                totalSpentTodayExpense,
                MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun TransactionTodaySummary(
    icon: ImageVector,
    label: String,
    amount: Long,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${amount.toCurrency()} F",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = color
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
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(periodsFilter) { periodFilter ->
            val isSelected = period == periodFilter.displayName
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
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomIcon(
            icon = transactionItemUState.icon,
            size = 48.dp,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            iconSize = 24.dp
        )

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = transactionItemUState.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
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
        }

        Text(
            text = "${if (transactionItemUState.isExpense) "-" else "+"}${transactionItemUState.amount.toCurrency()}F",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (transactionItemUState.isExpense) MaterialTheme.colorScheme.error else Color(
                0xFF2E7D32
            )
        )
    }
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

