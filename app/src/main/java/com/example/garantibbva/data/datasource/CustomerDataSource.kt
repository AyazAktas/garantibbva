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

    private val testCustomers = listOf(
        Customer(
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
        ),
        Customer(
            customerId = "2",
            costumerProfilePicture = R.drawable.ayaz,
            customerName = "Merve",
            customerTc = "23456789012",
            customerPassword = "5678",
            customersBalance = 1500.0,
            customerNo = "10002",
            accountNo = generateRandomAccountNo(),
            accountLocation = "VIAPORT - KURTKOY",
            accountType = "Checking",
            accountPurpose = "Business"
        ),
        Customer(
            customerId = "3",
            costumerProfilePicture = R.drawable.ayaz,
            customerName = "Emre",
            customerTc = "34567890123",
            customerPassword = "9101",
            customersBalance = 2000.0,
            customerNo = "10003",
            accountNo = generateRandomAccountNo(),
            accountLocation = "Cevahir Mall",
            accountType = "Savings",
            accountPurpose = "Investment"
        )
        // Diğer müşteri örnekleri buraya eklenebilir
    )

    suspend fun login(enteredCustomerTcOrNo: String, enteredCustomerPassword: String): Customer? {
        return withContext(Dispatchers.IO) {
            val customer = testCustomers.find {
                (it.customerTc == enteredCustomerTcOrNo || it.customerNo == enteredCustomerTcOrNo)
                        && it.customerPassword == enteredCustomerPassword
            }
            if (customer != null) {
                customer
            } else {
                Log.e("LoginError", "Hatalı Numara veya Parola")
                null
            }
        }
    }

    suspend fun customerInit(): List<Customer> = withContext(Dispatchers.IO) {
        return@withContext testCustomers
    }

    suspend fun onForgetPasswordClicked() {
        Log.e("ParolaUnuttum", "Parola sıfırlama isteği")
    }
}
