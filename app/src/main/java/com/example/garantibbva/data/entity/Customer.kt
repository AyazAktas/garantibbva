package com.example.garantibbva.data.entity

import java.io.Serializable

data class Customer(
    var customerId: String,
    var costumerProfilePicture: Int,
    var customerName: String,
    var customerTc: String,
    var customerPassword: String,
    var customersBalance: Double,
    var customerNo: String,
    var accountNo: String,
    var accountLocation: String,
    var accountType: String,
    var accountPurpose: String
) : Serializable{

}
