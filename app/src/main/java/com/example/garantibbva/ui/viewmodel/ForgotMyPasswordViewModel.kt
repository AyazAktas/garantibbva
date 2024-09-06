package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ForgotMyPasswordViewModel@Inject constructor(var customerRepository: CustomerRepository) : ViewModel()  {
    suspend fun passwordValidation(enteredCustomerNo:String,enteredTC:String): Customer?{
        return customerRepository.passwordValidation(enteredCustomerNo, enteredTC)
    }

    suspend fun updatePassword(customerId:String,customerPassword:String){
        return customerRepository.updatePassword(customerId, customerPassword)
    }
}