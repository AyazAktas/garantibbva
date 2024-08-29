package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garantibbva.data.repository.CustomerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val customerRepository = CustomerRepository()

    fun login(enteredNo: String, enteredPassword: String, onLoginResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isSuccess = customerRepository.login(enteredNo, enteredPassword)
            onLoginResult(isSuccess)
        }
    }
}
