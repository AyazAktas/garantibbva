package com.example.garantibbva.data.datasource

import android.util.Log
import com.example.garantibbva.R
import com.example.garantibbva.data.entity.Customer
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.random.Random

class CustomerDataSource(val collectionReference: CollectionReference) {

    private fun generateRandomAccountNo(): String {
        val part1 = Random.nextInt(1000, 9999)
        val part2 = Random.nextLong(1000000, 9999999)
        return "$part1-$part2"
    }



    suspend fun login(enteredCustomerTcOrNo: String, enteredCustomerPassword: String): Customer? {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = collectionReference
                    .whereEqualTo("customerTc", enteredCustomerTcOrNo)
                    .whereEqualTo("customerPassword", enteredCustomerPassword)
                    .get()
                    .await()
                if (querySnapshot.isEmpty) {
                    val customerNoQuerySnapshot = collectionReference
                        .whereEqualTo("customerNo", enteredCustomerTcOrNo)
                        .whereEqualTo("customerPassword", enteredCustomerPassword)
                        .get()
                        .await()

                    if (customerNoQuerySnapshot.isEmpty) {
                        Log.e("LoginError", "Hatalı Numara veya Parola")
                        return@withContext null
                    } else {

                        return@withContext customerNoQuerySnapshot.documents[0].toObject(Customer::class.java)
                    }
                } else {

                    return@withContext querySnapshot.documents[0].toObject(Customer::class.java)
                }

            } catch (e: Exception) {
                Log.e("LoginError", "Firebase sorgusunda hata: ${e.message}")
                return@withContext null
            }
        }
    }


    fun customerRegister(costumerProfilePicture:Int,customerName:String,customerTc:String,customerPassword:String,customerNo:String,accountNo:String,accountLocation:String,accountType:String,accountPurpose:String){
        val newCustomer=Customer("",costumerProfilePicture,customerName,customerTc,customerPassword,0.0,customerNo,accountNo,accountLocation, accountType, accountPurpose)
        collectionReference.document().set(newCustomer)
    }


    fun customerInit(): List<Customer>  {
        return listOf()
    }

    suspend fun onForgetPasswordClicked() {
        Log.e("ParolaUnuttum", "Parola sıfırlama isteği")
    }
}
