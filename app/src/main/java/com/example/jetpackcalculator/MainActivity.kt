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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("0") }
    var result by remember { mutableStateOf("") }
    var lastResult by remember { mutableStateOf("") } // Store last computed result
    var isNewCalculation by remember { mutableStateOf(false) } // Track if new calculation started

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display Area
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = input,
                fontSize = 32.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = result,
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Buttons Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            val buttons = listOf(
                listOf("(", ")", "AC", "⌫"),
                listOf("7", "8", "9", "÷"),
                listOf("4", "5", "6", "×"),
                listOf("1", "2", "3", "-"),
                listOf(".", "0", "=", "+")
            )

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
                                            val expression = input
                                                .replace('×', '*')
                                                .replace('÷', '/')
                                            val computedResult = evaluateExpression(expression)
                                            result = computedResult
                                            lastResult = computedResult // Store result for next operation
                                            isNewCalculation = true // Mark as new calculation
                                        } catch (e: Exception) {
                                            result = "Error"
                                        }
                                    }
                                    "AC" -> {
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
                                    "(" -> {
                                        if (input == "0" || isNewCalculation) {
                                            input = "("
                                        } else {
                                            input += "("
                                        }
                                        isNewCalculation = false
                                    }
                                    ")" -> {
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
                                            if (input == "0") input = label
                                            else input += label
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f),
                            shape = CircleShape
                        ) {
                            Text(label, fontSize = 22.sp)
                        }
                    }
                }
            }
        }
    }
}

// Ensure valid expression before evaluation
fun isValidExpression(expression: String): Boolean {
    val openBrackets = expression.count { it == '(' }
    val closeBrackets = expression.count { it == ')' }
    return openBrackets == closeBrackets
}

// Allow closing bracket only if it balances an opening bracket
fun canAddClosingBracket(expression: String): Boolean {
    val openBrackets = expression.count { it == '(' }
    val closeBrackets = expression.count { it == ')' }
    return openBrackets > closeBrackets
}
// Simple expression evaluator
fun evaluateExpression(expression: String): String {
    return try {
        val sanitizedExpression = expression.replace('×', '*').replace('÷', '/')
        val result = ExpressionBuilder(sanitizedExpression).build().evaluate()
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}