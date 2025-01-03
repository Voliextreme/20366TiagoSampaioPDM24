package com.example.lojapdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojapdm.data.model.Carro
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // Lista de carros carregada do Firestore
    private val _carros = MutableStateFlow<List<Carro>>(emptyList())
    val carros: StateFlow<List<Carro>> get() = _carros

    // Carro selecionado para detalhes
    private val _carroSelecionado = MutableStateFlow<Carro?>(null)
    val carroSelecionado: StateFlow<Carro?> get() = _carroSelecionado

    // Buscar todos os carros
    fun fetchCarros() {
        viewModelScope.launch {
            db.collection("carros")
                .get()
                .addOnSuccessListener { result ->
                    val listaCarros = result.map { document ->
                        Carro(
                            id = document.id,
                            marca = document.getString("marca") ?: "",
                            modelo = document.getString("modelo") ?: "",
                            preco = document.getDouble("preco") ?: 0.0,
                            descricao = document.getString("descricao") ?: ""
                        )
                    }
                    _carros.value = listaCarros
                }
                .addOnFailureListener {
                    _carros.value = emptyList()
                }
        }
    }

    // Buscar detalhes de um carro
    fun fetchCarroDetalhes(carroId: String) {
        viewModelScope.launch {
            db.collection("carros").document(carroId)
                .get()
                .addOnSuccessListener { document ->
                    _carroSelecionado.value = Carro(
                        id = document.id,
                        marca = document.getString("marca") ?: "",
                        modelo = document.getString("modelo") ?: "",
                        preco = document.getDouble("preco") ?: 0.0,
                        descricao = document.getString("descricao") ?: ""
                    )
                }
                .addOnFailureListener {
                    _carroSelecionado.value = null
                }
        }
    }

    fun clearCarroSelecionado() {
        _carroSelecionado.value = null
    }
}
