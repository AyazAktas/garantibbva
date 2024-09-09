package com.example.garantibbva.data.repository

import com.example.garantibbva.data.datasource.CorpDataSource
import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.entity.Customer

class CorpRepository(var corpDataSource: CorpDataSource) {
    suspend fun corpCustomerRegister(corpName:String,taxIdNumber:String,registrationNumber: String,address:String,corpPhoneNumber:String,corpEmail:String,contactPersonName: String,contactPersonPhone: String,contactPersonEmail: String,industry: String,contactPersonTc:String,accountType: String,dateOfIncorporation:String,annualRevenue: Double,numberOfEmployees: String,accountBalance: Double,accountPurpose: String,password: String)=corpDataSource.corpCustomerRegister(corpName,taxIdNumber, registrationNumber, address, corpPhoneNumber, corpEmail, contactPersonName, contactPersonPhone, contactPersonEmail, industry, contactPersonTc, accountType, dateOfIncorporation, annualRevenue, numberOfEmployees, accountBalance, accountPurpose,password)
    fun corpRegisterOtherInfos(corpId: String,
                               corpName: String,
                               taxIdNumber: String,
                               registrationNumber: String,
                               address: String,
                               corpPhoneNumber: String,
                               corpEmail: String,
                               industry: String,
                               dateOfIncorporation: String,
                               numberOfEmployees: String,
                               contactPersonName: String,
                               contactPersonPhone: String,
                               contactPersonEmail: String,
                               contactPersonTc: String,
                               password: String)=corpDataSource.corpRegisterOtherInfos(corpId, corpName, taxIdNumber, registrationNumber, address, corpPhoneNumber, corpEmail, industry, dateOfIncorporation, numberOfEmployees, contactPersonName, contactPersonPhone, contactPersonEmail, contactPersonTc, password)
    fun cancelRegistration(corpId: String)=corpDataSource.cancelRegistration(corpId)
    suspend fun login(enteredTcNo:String,enteredContactsCustomerNo:String,enteredPassword:String)=corpDataSource.login(enteredTcNo, enteredContactsCustomerNo, enteredPassword)
    suspend fun isCorpsPasswordCorrect(corp: Corp?, enteredPassword: String)=corpDataSource.isCorpsPasswordCorrect(corp, enteredPassword)
    fun closeAccountCorp(corpId:String)=corpDataSource.closeAccountCorp(corpId)
    suspend fun updateCorpPassword(corpId:String,corpPassword:String)=corpDataSource.updateCorpPassword(corpId, corpPassword)
    suspend fun passwordValidationCorp(enteredCorpCustomerNo:String,enteredCorpTC:String)=corpDataSource.passwordValidationCorp(enteredCorpCustomerNo, enteredCorpTC)
}