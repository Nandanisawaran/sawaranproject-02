package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    fun onButtonClick(buttonText: String) {
        when (buttonText) {
            "C" -> {
                input = ""
                result = ""
            }
            "=" -> {
                result = try {
                    evaluateExpression(input)
                } catch (_: Exception) {
                    "Error"
                }
            }
            else -> {
                input += buttonText
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = input, fontSize = 32.sp, modifier = Modifier.padding(bottom = 16.dp))
        Text(text = result, fontSize = 48.sp, modifier = Modifier.padding(bottom = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        CalculatorButtons(onButtonClick = ::onButtonClick)
    }
}

fun evaluateExpression(expression: String): String {
    return try {
        val numbers = expression.split(Regex("[+*/-]"))
        if (numbers.size != 2 || numbers.any { it.isEmpty() }) return "Error"

        val num1 = numbers[0].toDouble()
        val num2 = numbers[1].toDouble()
        val operator = expression.find { it in "+-*/" }

        val calculationResult = when (operator) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '*' -> num1 * num2
            '/' -> if (num2 != 0.0) num1 / num2 else return "Error"
            else -> return "Error"
        }
        calculationResult.toString()
    } catch (_: Exception) {
        "Error"
    }
}


@Composable
fun CalculatorButtons(onButtonClick: (String) -> Unit) {
    Column {
        val buttonLabels = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("C", "0", "=", "+")
        )

        buttonLabels.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { label ->
                    Button(
                        onClick = { onButtonClick(label) },
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    ) {
                        Text(text = label, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}
