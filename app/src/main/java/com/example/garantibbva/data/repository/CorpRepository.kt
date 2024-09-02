package com.example.garantibbva.data.repository

import com.example.garantibbva.data.datasource.CorpDataSource

class CorpRepository(var corpDataSource: CorpDataSource) {
    suspend fun corpCustomerRegister(corpName:String,taxIdNumber:String,registrationNumber: String,address:String,corpPhoneNumber:String,corpEmail:String,contactPersonName: String,contactPersonPhone: String,contactPersonEmail: String,industry: String,contactPersonTc:String,accountType: String,dateOfIncorporation:String,annualRevenue: Double,numberOfEmployees: Int,accountBalance: Double,accountPurpose: String)=corpDataSource.corpCustomerRegister(corpName,taxIdNumber, registrationNumber, address, corpPhoneNumber, corpEmail, contactPersonName, contactPersonPhone, contactPersonEmail, industry, contactPersonTc, accountType, dateOfIncorporation, annualRevenue, numberOfEmployees, accountBalance, accountPurpose)
}