package com.example.garantibbva.data.repository

import com.example.garantibbva.data.datasource.CustomerDataSource
import com.example.garantibbva.data.entity.Customer

class CustomerRepository(var customerDataSource:CustomerDataSource) {
    suspend fun login(enteredCustomerTcOrNo:String,enteredCustomerPassword:String)=customerDataSource.login(enteredCustomerTcOrNo, enteredCustomerPassword)
    suspend fun onForgetPasswordClicked()=customerDataSource.onForgetPasswordClicked()
    suspend fun customerInit(): List<Customer> = customerDataSource.customerInit()
}