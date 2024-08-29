package com.example.garantibbva.data.datasource

import android.util.Log
import com.example.garantibbva.R
import com.example.garantibbva.data.entity.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class CustomerDataSource {

    private fun generateRandomAccountNo(): String {
        val part1 = Random.nextInt(1000, 9999)
        val part2 = Random.nextLong(1000000, 9999999)
        return "$part1-$part2"
    }

    private val testCustomer = Customer(
        customerId = "1",
        costumerProfilePicture = R.drawable.ayaz,
        customerName = "Ayaz",
        customerTc = "12345678901",
        customerPassword = "1234",
        customersBalance = 1000.0,
        customerNo = "10001",
        accountNo = generateRandomAccountNo(),
        accountLocation = "VIAPORT - KURTKOY",
        accountType = "Savings",
        accountPurpose = "Personal"
    )

    suspend fun login(enteredCustomerTcOrNo: String, enteredCustomerPassword: String): Boolean {
        return withContext(Dispatchers.IO) {
            if ((enteredCustomerTcOrNo == testCustomer.customerTc || enteredCustomerTcOrNo == testCustomer.customerNo)
                && enteredCustomerPassword == testCustomer.customerPassword) {
                true
            } else {
                Log.e("LoginError", "Hatalı Numara veya Parola")
                false
            }
        }
    }

    suspend fun customerInit(): List<Customer> = withContext(Dispatchers.IO) {
        val customerList = ArrayList<Customer>()
        customerList.add(testCustomer)
        return@withContext customerList
    }
    suspend fun onForgetPasswordClicked() {
        Log.e("ParolaUnuttum","Parola sıfırlama isteği")
    }
}