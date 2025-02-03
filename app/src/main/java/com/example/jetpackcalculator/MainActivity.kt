package com.example.jetpackcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                listOf("7", "8", "9", "÷"),
                listOf("4", "5", "6", "×"),
                listOf("1", "2", "3", "-"),
                listOf(".", "0", "=", "+"),
                listOf("Clear", "Del") // Added "Del" button
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
                                            result = evaluateExpression(expression)
                                        } catch (e: Exception) {
                                            result = "Error"
                                        }
                                    }
                                    "Clear" -> {
                                        input = "0"
                                        result = ""
                                    }
                                    "Del" -> { // Delete last character
                                        input = if (input.length > 1) {
                                            input.dropLast(1)
                                        } else {
                                            "0"
                                        }
                                    }
                                    else -> {
                                        if (input == "0") input = label
                                        else input += label
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                        ) {
                            Text(label, fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}

// Simple expression evaluator
fun evaluateExpression(expression: String): String {
    return try {
        val result = expression.split('+', '-', '*', '/')
        val operators = expression.filter { it == '+' || it == '-' || it == '*' || it == '/' }
        var res = result[0].toDouble()

        for (i in operators.indices) {
            val num = result[i + 1].toDouble()
            when (operators[i]) {
                '+' -> res += num
                '-' -> res -= num
                '*' -> res *= num
                '/' -> res /= num
            }
        }
        res.toString()
    } catch (e: Exception) {
        "Error"
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}
