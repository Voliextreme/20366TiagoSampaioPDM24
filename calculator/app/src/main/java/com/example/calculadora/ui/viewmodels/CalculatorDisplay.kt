package com.example.calculadora.ui.viewmodels

import CalculatorViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorDisplay(viewModel: CalculatorViewModel) {
    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = viewModel.display.value,
            color = Color.White,
            fontSize = 24.sp
        )
    }
}
