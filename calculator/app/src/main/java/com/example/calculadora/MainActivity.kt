package com.example.calculadora

import CalculatorViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.calculadora.ui.theme.CalculadoraTheme
import com.example.calculadora.ui.viewmodels.CalculatorButton
import com.example.calculadora.ui.viewmodels.CalculatorDisplay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val calculatorViewModel: CalculatorViewModel by viewModels()
                    CalculatorLayout(
                        calculatorViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorLayout(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(Color.DarkGray, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
                .wrapContentSize()
        ) {
            // Display
            CalculatorDisplay(viewModel = viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons Grid
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                val buttonSpacing = 8.dp
                val buttonModifier = Modifier
                    .weight(1f)
                    .padding(buttonSpacing / 2)

                val buttonRows = listOf(
                    listOf("C", "", "", "/"),
                    listOf("7", "8", "9", "x"),
                    listOf("4", "5", "6", "-"),
                    listOf("1", "2", "3", "+"),
                    listOf("0", ".", "=", "")
                )

                for (row in buttonRows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { symbol ->
                            if (symbol.isNotEmpty()) {
                                CalculatorButton(symbol) {
                                    when (symbol) {
                                        "C" -> viewModel.onClearClick()
                                        "=" -> viewModel.onEqualClick()
                                        in "0".."9", "." -> viewModel.onNumberClick(symbol)
                                        else -> viewModel.onOperatorClick(symbol)
                                    }
                                }
                            } else {
                                Spacer(modifier = buttonModifier)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(buttonSpacing))
                }
            }
        }
    }
}
