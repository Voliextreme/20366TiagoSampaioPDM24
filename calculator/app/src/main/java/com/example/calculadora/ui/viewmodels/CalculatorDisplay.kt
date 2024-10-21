package com.example.calculadora.ui.viewmodels

import CalculatorViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun CalculatorDisplay(viewModel: CalculatorViewModel) {
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
}
