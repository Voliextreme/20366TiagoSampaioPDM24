package com.example.calculadora

import CalculatorViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.example.calculadora.ui.theme.CalculadoraTheme
import com.example.calculadora.ui.viewmodels.CalculatorButton



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val calculatorViewModel: CalculatorViewModel by viewModels()
                    Layout(
                        calculatorViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Layout(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = viewModel.display.value, color = Color.Black)
            }

            // Calculator Buttons
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    CalculatorButton(" ") {  }
                    CalculatorButton(" ") {  }
                    CalculatorButton(" ") {  }
                    CalculatorButton("C") { viewModel.onClearClick() }
                }
                Row {
                    CalculatorButton("7") { viewModel.onNumberClick("7") }
                    CalculatorButton("8") { viewModel.onNumberClick("8") }
                    CalculatorButton("9") { viewModel.onNumberClick("9") }
                    CalculatorButton("/") { viewModel.onOperatorClick("/") }
                }
                Row {
                    CalculatorButton("4") { viewModel.onNumberClick("4") }
                    CalculatorButton("5") { viewModel.onNumberClick("5") }
                    CalculatorButton("6") { viewModel.onNumberClick("6") }
                    CalculatorButton("x") { viewModel.onOperatorClick("x") }
                }
                Row {
                    CalculatorButton("1") { viewModel.onNumberClick("1") }
                    CalculatorButton("2") { viewModel.onNumberClick("2") }
                    CalculatorButton("3") { viewModel.onNumberClick("3") }
                    CalculatorButton("-") { viewModel.onOperatorClick("-") }
                }
                Row {
                    CalculatorButton("0") { viewModel.onNumberClick("0") }
                    CalculatorButton(".") { viewModel.onNumberClick(".") }
                    CalculatorButton("=") { viewModel.onEqualClick() }
                    CalculatorButton("+") { viewModel.onOperatorClick("+") }
                }
            }
        }
    }
}
