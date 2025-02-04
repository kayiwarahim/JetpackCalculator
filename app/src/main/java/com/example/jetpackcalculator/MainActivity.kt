package com.example.jetpackcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.*
import java.math.BigDecimal
import java.math.RoundingMode

// Main activity where the Jetpack Compose UI is rendered
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp() // Loads the Calculator UI
        }
    }
}

// Composable function that represents the entire calculator UI
@Composable
fun CalculatorApp() {
    // Stores user input (expression)
    var input by remember { mutableStateOf("0") }

    // Stores calculated result
    var result by remember { mutableStateOf("") }

    // Stores last computed result to allow chaining operations
    var lastResult by remember { mutableStateOf("") }

    // Indicates if a new calculation is starting (helps in clearing previous results)
    var isNewCalculation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display Area: Shows user input and computed result
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = input, // Display user-entered expression
                fontSize = 32.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = result, // Display computed result
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Buttons Section: Defines calculator buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Define calculator button layout
            val buttons = listOf(
                listOf("(", ")", "AC", "⌫"),
                listOf("7", "8", "9", "÷"),
                listOf("4", "5", "6", "×"),
                listOf("1", "2", "3", "-"),
                listOf(".", "0", "=", "+")
            )

            // Iterate over button rows
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { label ->
                        Button(
                            onClick = {
                                when (label) {
                                    "=" -> {
                                        try {
                                            // Convert operators to standard format
                                            val expression = input
                                                .replace('×', '*')
                                                .replace('÷', '/')

                                            // Evaluate expression and store the result
                                            val computedResult = evaluateExpression(expression)
                                            result = computedResult
                                            lastResult = computedResult // Store for chaining
                                            isNewCalculation = true // Mark for fresh input
                                        } catch (e: Exception) {
                                            result = "Error" // Handle invalid expressions
                                        }
                                    }
                                    "AC" -> { // Clear everything
                                        input = "0"
                                        result = ""
                                        lastResult = ""
                                        isNewCalculation = false
                                    }
                                    "⌫" -> { // Delete last character
                                        input = if (input.length > 1) {
                                            input.dropLast(1)
                                        } else {
                                            "0"
                                        }
                                    }
                                    "(" -> { // Handle opening bracket
                                        if (input == "0" || isNewCalculation) {
                                            input = "("
                                        } else {
                                            input += "("
                                        }
                                        isNewCalculation = false
                                    }
                                    ")" -> { // Handle closing bracket
                                        if (canAddClosingBracket(input)) {
                                            input += ")"
                                        }
                                    }
                                    else -> {
                                        if (isNewCalculation) {
                                            if (label in listOf("+", "-", "×", "÷")) {
                                                input = "$lastResult$label" // Continue from result
                                            } else {
                                                input = label // Start fresh
                                            }
                                            isNewCalculation = false
                                        } else {
                                            if (input == "0") input = label // Replace default 0
                                            else input += label // Append input
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f),
                            shape = CircleShape // Make buttons circular
                        ) {
                            Text(label, fontSize = 22.sp)
                        }
                    }
                }
            }
        }
    }
}

// Function to ensure valid expression before evaluation
fun isValidExpression(expression: String): Boolean {
    val openBrackets = expression.count { it == '(' }
    val closeBrackets = expression.count { it == ')' }
    return openBrackets == closeBrackets // Ensures balanced brackets
}

// Allow adding closing bracket only if there's an unmatched opening bracket
fun canAddClosingBracket(expression: String): Boolean {
    val openBrackets = expression.count { it == '(' }
    val closeBrackets = expression.count { it == ')' }
    return openBrackets > closeBrackets
}

// Function to evaluate the mathematical expression
fun evaluateExpression(expression: String): String {
    return try {
        val sanitizedExpression = expression.replace('×', '*').replace('÷', '/')

        // Use Exp4j to evaluate as double
        val result = ExpressionBuilder(sanitizedExpression).build().evaluate()

        // Convert result to BigDecimal for precision
        BigDecimal(result).stripTrailingZeros().toPlainString()
    } catch (e: Exception) {
        "Error" // Handle evaluation errors
    }
}

// Preview function for Jetpack Compose UI
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}
