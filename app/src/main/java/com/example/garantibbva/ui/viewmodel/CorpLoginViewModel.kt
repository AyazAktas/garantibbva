package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.repository.CorpRepository
import com.example.garantibbva.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CorpLoginViewModel @Inject constructor(var corpRepository: CorpRepository) : ViewModel() {
    suspend fun login(enteredTcNo:String,enteredContactsCustomerNo:String,enteredPassword:String):Corp?{
        return corpRepository.login(enteredTcNo, enteredContactsCustomerNo, enteredPassword)
    }
}