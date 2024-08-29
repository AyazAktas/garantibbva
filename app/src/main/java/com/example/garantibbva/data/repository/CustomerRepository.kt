package com.example.garantibbva.data.repository

import com.example.garantibbva.data.datasource.CustomerDataSource
import com.example.garantibbva.data.entity.Customer

class CustomerRepository {
    var costumerDataSource=CustomerDataSource()
    suspend fun login(enteredCustomerTcOrNo:String,enteredCustomerPassword:String)=costumerDataSource.login(enteredCustomerTcOrNo, enteredCustomerPassword)
    suspend fun onForgetPasswordClicked()=costumerDataSource.onForgetPasswordClicked()
    suspend fun customerInit(): List<Customer> = costumerDataSource.customerInit()
}