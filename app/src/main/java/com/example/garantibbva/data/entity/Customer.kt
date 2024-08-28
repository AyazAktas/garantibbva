package com.example.garantibbva.data.entity

import java.io.Serializable

data class Customer(var customerId:String,
                    var customerName:String,
                    var customerTc:String,
                    var customerPassword:String,
                    var customersBalance:Int,
                    var customerNo:String,
                    var accountType:String,
                    var accountInfo:String):Serializable{

                    }