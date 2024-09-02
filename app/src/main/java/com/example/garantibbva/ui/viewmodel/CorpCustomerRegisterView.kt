package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.repository.CorpRepository
import com.example.garantibbva.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CorpCustomerRegisterView @Inject constructor(var corpRepository: CorpRepository) : ViewModel() {
    suspend fun corpCustomerRegister(corpName:String,taxIdNumber:String,registrationNumber: String,address:String,corpPhoneNumber:String,corpEmail:String,contactPersonName: String,contactPersonPhone: String,contactPersonEmail: String,industry: String,contactPersonTc:String,accountType: String,dateOfIncorporation:String,annualRevenue: Double,numberOfEmployees: Int,accountBalance: Double,accountPurpose: String):Corp{
        return corpRepository.corpCustomerRegister(corpName, taxIdNumber, registrationNumber, address, corpPhoneNumber, corpEmail, contactPersonName, contactPersonPhone, contactPersonEmail, industry, contactPersonTc, accountType, dateOfIncorporation, annualRevenue, numberOfEmployees, accountBalance, accountPurpose)
    }
}