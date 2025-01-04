package com.example.lojapdm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojapdm.data.repository.CarRepository
import com.example.lojapdm.domain.model.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarViewModel(private val repository: CarRepository = CarRepository()) : ViewModel() {

    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars

    init {
        fetchCars()
    }

    private fun fetchCars() {
        viewModelScope.launch {
            val fetchedCars = repository.getAllCars()
            Log.e("CarViewModel", fetchedCars.toString())
            _cars.value = fetchedCars
        }
    }
}