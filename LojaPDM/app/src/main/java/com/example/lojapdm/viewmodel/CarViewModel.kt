package com.example.lojapdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojapdm.domain.model.Carro
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CarViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // Lista de carros carregada do Firestore
    private val _carros = MutableStateFlow<List<Carro>>(emptyList())
    val carros: StateFlow<List<Carro>> get() = _carros

    // Carro selecionado para detalhes
    private val _carroSelecionado = MutableStateFlow<Carro?>(null)
    val carroSelecionado: StateFlow<Carro?> get() = _carroSelecionado

    private val _carroDetalhes = MutableStateFlow<Carro?>(null)
    val carroDetalhes: StateFlow<Carro?> get() = _carroDetalhes

    // Buscar todos os carros
    fun fetchCarros() {
        viewModelScope.launch {
            try {
                val result = db.collection("carros").get().await()
                val listaCarros = result.documents.map { document ->
                    Carro(
                        id = document.id,
                        marca = document.getString("marca") ?: "",
                        modelo = document.getString("modelo") ?: "",
                        preco = document.getDouble("preco") ?: 0.0,
                        descricao = document.getString("descricao") ?: ""
                    )
                }
                _carros.value = listaCarros
            } catch (e: Exception) {
                _carros.value = emptyList()
            }
        }
    }
    fun fetchCarroDetalhes(carroId: String) {
        viewModelScope.launch {
            db.collection("carros")
                .document(carroId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val carro = Carro(
                            id = document.id,
                            marca = document.getString("marca") ?: "",
                            modelo = document.getString("modelo") ?: "",
                            preco = document.getDouble("preco") ?: 0.0,
                            descricao = document.getString("descricao") ?: ""
                        )
                        _carroSelecionado.value = carro
                    } else {
                        // Handle the case where the car does not exist
                        _carroSelecionado.value = null
                    }
                }
                .addOnFailureListener {
                    // Handle failure to fetch car details
                    _carroSelecionado.value = null
                }
        }
    }

    // Adicionar um carro
    fun addCarro(marca: String, modelo: String, preco: Double, descricao: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val carro = Carro(marca = marca, modelo = modelo, preco = preco, descricao = descricao)

        viewModelScope.launch {
            try {
                val docRef = db.collection("carros").add(carro).await()
                val addedCarro = carro.copy(id = docRef.id) // Getting the document ID
                _carros.value = _carros.value + addedCarro // Update the list of cars in state
                onSuccess()
            } catch (e: Exception) {
                onFailure("Erro ao adicionar carro: ${e.message}")
            }
        }
    }

    // Limpar o carro selecionado
    fun clearCarroSelecionado() {
        _carroSelecionado.value = null
    }


}
