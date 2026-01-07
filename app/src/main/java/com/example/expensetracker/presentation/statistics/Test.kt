

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData

// Modèles de données
data class Expense(
    val id: String,
    val amount: Double,
    val category: String,
    val date: java.time.LocalDate,
    val description: String
)

data class Category(
    val id: String,
    val name: String,
    val color: Color
)

@Composable
fun ExpenseDonutChart(
    expenses: List<Expense>,
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    // Grouper les dépenses par catégorie
    val categoryTotals = remember(expenses, categories) {
        categories.mapNotNull { category ->
            val total = expenses
                .filter { it.category == category.name }
                .sumOf { it.amount }

            if (total > 0) {
                Triple(category.name, total, category.color)
            } else null
        }
    }

    // Si aucune dépense, afficher un graphique par défaut
    if (categoryTotals.isEmpty()) {
        Box(modifier = modifier) {
            Text(
                text = "Aucune dépense",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        return
    }

    // Créer les slices pour le graphique
    val slices = categoryTotals.map { (name, amount, color) ->
        PieChartData.Slice(
            label = name,
            value = amount.toFloat(),
            color = color
        )
    }

    // Configuration du graphique
    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        animationDuration = 800,
        showSliceLabels = true, // Pas de labels sur les tranches
        strokeWidth = 30f, // Épaisseur du donut
        labelVisible = true,
        labelType = PieChartConfig.LabelType.PERCENTAGE,
        labelColor = Color.Black,
        labelColorType = PieChartConfig.LabelColorType.SPECIFIED_COLOR,
        backgroundColor = Color.Transparent,
        activeSliceAlpha = 1f,
        inActiveSliceAlpha = 0.6f,
        isClickOnSliceEnabled = true,
        isSumVisible = true,
        sumUnit = " FCFA"
    )

    // Données du graphique
    val pieChartData = PieChartData(
        slices = slices,
        plotType = PlotType.Donut
    )

    // Affichage du graphique
    DonutPieChart(
        modifier = modifier
            .width(200.dp)
            .height(200.dp),
        pieChartData = pieChartData,
        pieChartConfig = pieChartConfig
    ) { slice ->
        // Action au clic sur une tranche
        println("Cliqué sur: ${slice.label} - ${slice.value} FCFA")
    }
}

// Version avec légende
@Composable
fun ExpenseDonutChartWithLegend(
    expenses: List<Expense>,
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    val categoryTotals = remember(expenses, categories) {
        categories.mapNotNull { category ->
            val total = expenses
                .filter { it.category == category.name }
                .sumOf { it.amount }

            if (total > 0) {
                Triple(category.name, total, category.color)
            } else null
        }
    }

    if (categoryTotals.isEmpty()) {
        Box(modifier = modifier) {
            Text(
                text = "Aucune dépense",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        return
    }

    val slices = categoryTotals.map { (name, amount, color) ->
        PieChartData.Slice(
            label = name,
            value = amount.toFloat(),
            color = color
        )
    }

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        animationDuration = 800,
        showSliceLabels = false,
        strokeWidth = 40f,
        labelVisible = false,
        backgroundColor = Color.Transparent,
        activeSliceAlpha = 1f,
        inActiveSliceAlpha = 0.6f,
        isClickOnSliceEnabled = true,
        isSumVisible = false
    )

    val pieChartData = PieChartData(
        slices = slices,
        plotType = PlotType.Donut
    )

    Column(modifier = modifier) {
        // Graphique
        DonutPieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            pieChartData = pieChartData,
            pieChartConfig = pieChartConfig
        ) { slice ->
            println("Cliqué sur: ${slice.label}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Légende
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoryTotals.forEach { (name, amount, color) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(color)
                        )
                        Text(
                            text = name,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                    Text(
                        text = "${amount.toInt()} FCFA",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

// Version compacte (petit donut)
@Composable
fun CompactExpenseDonut(
    expenses: List<Expense>,
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    val categoryTotals = remember(expenses, categories) {
        categories.mapNotNull { category ->
            val total = expenses
                .filter { it.category == category.name }
                .sumOf { it.amount }

            if (total > 0) {
                Triple(category.name, total, category.color)
            } else null
        }
    }

    if (categoryTotals.isEmpty()) return

    val slices = categoryTotals.map { (name, amount, color) ->
        PieChartData.Slice(
            label = name,
            value = amount.toFloat(),
            color = color
        )
    }

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = false,
        showSliceLabels = false,
        strokeWidth = 15f,
        labelVisible = false,
        backgroundColor = Color.Transparent,
        isClickOnSliceEnabled = false
    )

    val pieChartData = PieChartData(
        slices = slices,
        plotType = PlotType.Donut
    )

    DonutPieChart(
        modifier = modifier
            .width(100.dp)
            .height(100.dp),
        pieChartData = pieChartData,
        pieChartConfig = pieChartConfig
    ) { }
}

// Exemple d'utilisation
@Preview(showBackground = true)
@Composable
fun ExpenseDonutExample() {
    val sampleCategories = listOf(
        Category("1", "Alimentation", Color(0xFF2196F3)),
        Category("2", "Transport", Color(0xFFF44336)),
        Category("3", "Loisirs", Color(0xFF4CAF50)),
        Category("4", "Santé", Color(0xFFFF9800))
    )

    val sampleExpenses = listOf(
        Expense("1", 25000.0, "Alimentation", java.time.LocalDate.now(), "Courses"),
        Expense("2", 15000.0, "Transport", java.time.LocalDate.now(), "Taxi"),
        Expense("3", 10000.0, "Loisirs", java.time.LocalDate.now(), "Cinéma"),
        Expense("4", 20000.0, "Alimentation", java.time.LocalDate.now(), "Restaurant"),
        Expense("5", 8000.0, "Santé", java.time.LocalDate.now(), "Pharmacie")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Version avec légende
        ExpenseDonutChartWithLegend(
            expenses = sampleExpenses,
            categories = sampleCategories
        )

        // Version simple
        ExpenseDonutChart(
            expenses = sampleExpenses,
            categories = sampleCategories,
            modifier = Modifier.fillMaxWidth()
        )

        // Version compacte
        CompactExpenseDonut(
            expenses = sampleExpenses,
            categories = sampleCategories
        )
    }
}