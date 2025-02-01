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
import androidx.compose.ui.text.input.TextFieldValue

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
    var input by remember { mutableStateOf(TextFieldValue("0")) }
    var result by remember { mutableStateOf("") }

    // Define actions for the calculator
    fun onDigitClick(digit: String) {
        input = if (input.text == "0") {
            TextFieldValue(digit)
        } else {
            TextFieldValue(input.text + digit)
        }
    }

    fun onOperatorClick(operator: String) {
        input = TextFieldValue(input.text + " $operator ")
    }

    fun onEqualClick() {
        try {
            result = evaluateExpression(input.text)
        } catch (e: Exception) {
            result = "Error"
        }
    }

    fun onClearClick() {
        input = TextFieldValue("0")
        result = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display
        Text(
            text = input.text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Buttons
        Column {
            // Create rows of buttons
            Row {
                CalculatorButton("7", onClick = { onDigitClick("7") })
                CalculatorButton("8", onClick = { onDigitClick("8") })
                CalculatorButton("9", onClick = { onDigitClick("9") })
                CalculatorButton("/", onClick = { onOperatorClick("/") })
            }
            Row {
                CalculatorButton("4", onClick = { onDigitClick("4") })
                CalculatorButton("5", onClick = { onDigitClick("5") })
                CalculatorButton("6", onClick = { onDigitClick("6") })
                CalculatorButton("*", onClick = { onOperatorClick("*") })
            }
            Row {
                CalculatorButton("1", onClick = { onDigitClick("1") })
                CalculatorButton("2", onClick = { onDigitClick("2") })
                CalculatorButton("3", onClick = { onDigitClick("3") })
                CalculatorButton("-", onClick = { onOperatorClick("-") })
            }
            Row {
                CalculatorButton("0", onClick = { onDigitClick("0") })
                CalculatorButton(".", onClick = { onDigitClick(".") })
                CalculatorButton("=", onClick = { onEqualClick() })
                CalculatorButton("+", onClick = { onOperatorClick("+") })
            }
            Row {
                CalculatorButton("C", onClick = { onClearClick() })
            }
        }
    }
}

@Composable
fun CalculatorButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .size(70.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

// Evaluate the expression (basic example using eval function)
fun evaluateExpression(expression: String): String {
    // You can use a library like exp4j to evaluate the expression
    // Here for simplicity, we just return the expression
    return expression
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}
