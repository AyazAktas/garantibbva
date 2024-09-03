package com.example.garantibbva.data.entity


import java.io.Serializable

data class Customer(
    var customerId: String?="",
    var costumerProfilePicture: Int?=0,
    var customerName: String?="",
    var customerTc: String?="",
    var customerBirthDate:String?="",
    var customerPhoneNumber:String?="",
    var customerPassword: String?="",
    var customersBalance: Double?=0.0,
    var customerNo: String?="",
    var accountNo: String?="",
    var accountLocation:String?="",
    var accountType:String?="",
    var accountPurpose: String?="",
    var ibanNumber:String?=""
) : Serializable{

}
// python firebase imp
// notification
