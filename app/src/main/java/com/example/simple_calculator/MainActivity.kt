package com.example.simple_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simple_calculator.ui.theme.SimpleCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleCalculatorTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("0") }
    var result by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf("") }
    var firstNumber by remember { mutableStateOf("") }

    val buttons = listOf(
        listOf("C", "%", "<-", "/"),
        listOf("7", "8", "9", "*"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("+/-", "0", ".", "="),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = result.ifEmpty { input },
            fontSize = 52.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        buttons.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { button ->
                    if (button.isNotEmpty()) {
                        CalculatorButton(button, onClick = {
                            when (button) {
                                "C" -> {
                                    input = "0"
                                    result = ""
                                    operator = ""
                                    firstNumber = ""
                                }
                                "<-" -> {
                                    input = if (input.length > 1) input.dropLast(1) else "0"
                                    result = if (result.length > 1) result.dropLast(1) else "0"
                                }
                                "+/-" -> {
                                    input = if (input.startsWith("-")) input.substring(1) else "-$input"
                                }
                                "%" -> {
                                    try {
                                        input = (input.toDouble() / 100).toString()
                                    } catch (e: Exception) {
                                        result = "Error"
                                    }
                                }
                                "+", "-", "*", "/" -> {
                                    if (input.isNotEmpty()) {
                                        firstNumber = input
                                        operator = button
                                        input = "0"
                                    }
                                }
                                "=" -> {
                                    if (firstNumber.isNotEmpty() && operator.isNotEmpty()) {
                                        try {
                                            val secondNumber = input
                                            val num1 = firstNumber.toDouble()
                                            val num2 = secondNumber.toDouble()
                                            result = when (operator) {
                                                "+" -> (num1 + num2).toString()
                                                "-" -> (num1 - num2).toString()
                                                "*" -> (num1 * num2).toString()
                                                "/" -> (num1 / num2).toString()
                                                else -> "Error"
                                            }
                                            input = result
                                            operator = ""
                                            firstNumber = ""
                                        } catch (e: Exception) {
                                            result = "Error"
                                        }
                                    }
                                }
                                "." -> {
                                    if (!input.contains(".")) {
                                        input += "."
                                    }
                                }
                                else -> {
                                    input = if (input == "0") button else input + button
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .size(90.dp)
            .padding(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
    ) {
        Text(text = label, fontSize = 24.sp, color = Color.White)
    }
}