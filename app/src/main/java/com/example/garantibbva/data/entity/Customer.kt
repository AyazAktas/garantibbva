package com.example.garantibbva.data.entity

import java.io.Serializable

data class Customer(var customer_id:String,
                    var customer_name:String,
                    var customer_tc:String,
                    var customer_password:String,
                    var customers_balance:Int,
                    var customer_no:String,
                    var account_type:String,
                    var account_info:String):Serializable{
}