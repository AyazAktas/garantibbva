package com.example.garantibbva.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.garantibbva.data.repository.CorpRepository
import com.example.garantibbva.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CorpRegistrationStepTwoViewModel@Inject constructor(var corpRepository: CorpRepository) : ViewModel() {
    fun corpRegisterOtherInfos(corpId:String,corpName: String,taxIdNumber: String,registrationNumber: String,address: String,corpPhoneNumber: String,corpEmail: String,contactPersonName: String,contactPersonPhone: String,contactPersonEmail: String,industry: String,contactPersonTc: String,dateOfIncorporation: String,numberOfEmployees: String,password:String){
        corpRepository.corpRegisterOtherInfos(corpId, corpName, taxIdNumber, registrationNumber, address, corpPhoneNumber, corpEmail, contactPersonName, contactPersonPhone, contactPersonEmail, industry, contactPersonTc, dateOfIncorporation,numberOfEmployees,password)
    }
    fun cancelRegistration(corpId: String){
        corpRepository.cancelRegistration(corpId)
    }

}