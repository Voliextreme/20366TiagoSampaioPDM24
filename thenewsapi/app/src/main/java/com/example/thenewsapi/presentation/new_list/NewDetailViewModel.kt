package com.example.thenewsapi.presentation.new_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thenewsapi.data.repository.NewDetailRepositoryImpl
import com.example.thenewsapi.domain.model.NewDetail
import com.example.thenewsapi.domain.use_case.GetNewDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewDetailViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val repository = NewDetailRepositoryImpl(api)
    private val getNewDetailUseCase = GetNewDetailUseCase(repository)

    val newDetail = MutableStateFlow<NewDetail?>(null)

    fun fetchIndividualNew(newId: String){
        viewModelScope.launch {
            try{
                newDetail.value = getNewDetailUseCase(newId)
            }catch (e: Exception){
                newDetail.value = null
            }
        }
    }
}