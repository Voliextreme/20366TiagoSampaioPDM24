package com.example.calculadora.ui.viewmodels

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorButton(
    symbol: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF444444),
            contentColor = Color.White
        ),
        modifier = Modifier.size(64.dp)
    ) {
        Text(text = symbol, fontSize = 20.sp)
    }
}
