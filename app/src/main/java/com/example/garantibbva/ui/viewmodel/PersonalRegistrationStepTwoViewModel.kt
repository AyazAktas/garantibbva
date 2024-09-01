package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class PersonalRegistrationStepTwoViewModel@Inject constructor(var customerRepository: CustomerRepository) : ViewModel() {
    fun customerRegisterOtherInfos(customerId:String,customerName: String, customerTc: String, customerBirthDate: String, customerPhoneNumber: String, customerPassword: String, accountLocation: String){
        customerRepository.customerRegisterOtherInfos(customerId, customerName, customerTc, customerBirthDate, customerPhoneNumber, customerPassword, accountLocation)
    }
}