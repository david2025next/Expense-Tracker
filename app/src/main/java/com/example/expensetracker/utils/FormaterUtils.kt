package com.example.expensetracker.utils

import kotlin.math.roundToInt


fun Float.toPercentageString() = "${(this*100).roundToInt()}% of totals"