package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class PersonalCustomerRegisterViewModel@Inject constructor(var customerRepository: CustomerRepository) : ViewModel() {
    fun personalCustomerRegister(customerId:String, costumerProfilePicture: Int, customerName: String, customerTc: String, customerBirthDate:String, customerPhoneNumber:String, customerPassword: String, customersBalance: Double, accountLocation:String, accountType:String, accountPurpose: String){
        return customerRepository.personalCustomerRegister(customerId, costumerProfilePicture, customerName, customerTc, customerBirthDate, customerPhoneNumber, customerPassword, customersBalance, accountLocation, accountType,accountPurpose)
    }
}