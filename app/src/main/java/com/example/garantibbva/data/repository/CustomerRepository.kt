package com.example.garantibbva.data.repository

import com.example.garantibbva.data.datasource.CustomerDataSource
import com.example.garantibbva.data.entity.Customer

class CustomerRepository(var customerDataSource:CustomerDataSource) {
    suspend fun login(enteredCustomerTcOrNo:String,enteredCustomerPassword:String)=customerDataSource.login(enteredCustomerTcOrNo, enteredCustomerPassword)
    suspend fun onForgetPasswordClicked()=customerDataSource.onForgetPasswordClicked()
    fun customerInit(): List<Customer> = customerDataSource.customerInit()
    suspend fun personalCustomerRegister(customerId:String, costumerProfilePicture: Int, customerName: String, customerTc: String, customerBirthDate:String, customerPhoneNumber:String, customerPassword: String, customersBalance: Double, accountLocation:String, accountType:String, accountPurpose: String)=customerDataSource.personalCustomerRegister(costumerProfilePicture, customerName, customerTc, customerBirthDate, customerPhoneNumber, customerPassword, customersBalance, accountLocation, accountType, accountPurpose)
    fun customerRegisterOtherInfos(customerId:String,customerName: String, customerTc: String, customerBirthDate: String, customerPhoneNumber: String, customerPassword: String, accountLocation: String)=customerDataSource.customerRegisterOtherInfos(customerId, customerName, customerTc, customerBirthDate, customerPhoneNumber, customerPassword, accountLocation)
    fun cancelRegistration(customerId:String)=customerDataSource.cancelRegistration(customerId)
    suspend fun isCustomersPasswordCorrect(customer: Customer?, enteredPassword: String)=customerDataSource.isCustomersPasswordCorrect(customer, enteredPassword)
    fun customerAccountClosing(customerId: String)=customerDataSource.customerAccountClosing(customerId)

}