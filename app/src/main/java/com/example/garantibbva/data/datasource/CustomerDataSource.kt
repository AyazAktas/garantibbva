package com.example.garantibbva.data.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.garantibbva.R
import com.example.garantibbva.data.entity.Customer
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
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

    var customerList= MutableLiveData<List<Customer>>()

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

    private fun generateRandomCustomerNo(): String {
        val part2 = Random.nextLong(1000000, 9999999)
        return "$part2"
    }

    suspend fun personalCustomerRegister(
        costumerProfilePicture: Int,
        customerName: String,
        customerTc: String,
        customerBirthDate: String,
        customerPhoneNumber: String,
        customerPassword: String,
        customersBalance: Double,
        accountLocation: String,
        accountType: String,
        accountPurpose: String
    ): Customer {
        val newCustomer = Customer(
            "", // Placeholder for customerId
            costumerProfilePicture,
            customerName,
            customerTc,
            customerBirthDate,
            customerPhoneNumber,
            customerPassword,
            customersBalance,
            generateRandomCustomerNo(),
            generateRandomAccountNo(),
            accountLocation,
            accountType,
            accountPurpose
        )

        val documentRef = collectionReference.add(newCustomer).await()
        val customerId = documentRef.id
        newCustomer.customerId = customerId

        // Update the document with the generated customerId
        documentRef.set(newCustomer).await()

        return newCustomer
    }

    fun customerRegisterOtherInfos(customerId: String, customerName: String, customerTc: String, customerBirthDate: String, customerPhoneNumber: String, customerPassword: String, accountLocation: String) {
        if (customerId.isEmpty()) {
            Log.e("CustomerUpdate", "Customer ID cannot be empty")
            return
        }

        val customerToUpdate = HashMap<String, Any>()
        customerToUpdate["customerName"] = customerName
        customerToUpdate["customerTc"] = customerTc
        customerToUpdate["customerBirthDate"] = customerBirthDate
        customerToUpdate["customerPhoneNumber"] = customerPhoneNumber
        customerToUpdate["customerPassword"] = customerPassword
        customerToUpdate["accountLocation"] = accountLocation

        // Koleksiyon referansı
        val collectionReference = FirebaseFirestore.getInstance().collection("Customers")

        // Belge referansı
        val documentRef = collectionReference.document(customerId)

        // Güncelleme işlemi
        documentRef.update(customerToUpdate)
            .addOnSuccessListener {
                Log.d("CustomerUpdate", "Customer successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.e("CustomerUpdate", "Error updating document", e)
            }
    }

    fun customerInit(): List<Customer> {
        return listOf()
    }

    suspend fun onForgetPasswordClicked() {
        Log.e("ParolaUnuttum", "Parola sıfırlama isteği")
    }
}
