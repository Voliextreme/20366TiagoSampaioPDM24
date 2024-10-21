package com.example.lazyexamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val itemList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
            ExemploLista(itemList)
            }
        }
    }


@Composable
fun ExemploLista(itemList: List<String>) {
    LazyColumn {
        items(itemList){ item ->
            Text(text = item)
        }
    }
}
