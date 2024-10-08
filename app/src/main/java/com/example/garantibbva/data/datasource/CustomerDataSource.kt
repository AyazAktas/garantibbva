package com.example.garantibbva.data.datasource

import android.util.Log
import com.example.garantibbva.data.entity.Customer
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.random.Random

class CustomerDataSource(private val collectionReference: CollectionReference) {

    private fun generateRandomAccountNo(): String {
        val part1 = Random.nextInt(1000, 9999)
        val part2 = Random.nextLong(1000000, 9999999)
        return "$part1-$part2"
    }

    private suspend fun generateUniqueAccountNo(): String {
        while (true) {
            val accountNo = generateRandomAccountNo()
            val exists = withContext(Dispatchers.IO) {
                val querySnapshot = collectionReference
                    .whereEqualTo("accountNo", accountNo)
                    .get()
                    .await()
                !querySnapshot.isEmpty
            }
            if (!exists) return accountNo
        }
    }

    private suspend fun generateUniqueCustomerNo(): String {
        while (true) {
            val customerNo = generateRandomCustomerNo()
            val exists = withContext(Dispatchers.IO) {
                val querySnapshot = collectionReference
                    .whereEqualTo("customerNo", customerNo)
                    .get()
                    .await()
                !querySnapshot.isEmpty
            }
            if (!exists) return customerNo
        }
    }

    private fun generateRandomCustomerNo(): String {
        val part2 = Random.nextLong(1000000, 9999999)
        return "$part2"
    }

    private suspend fun generateUniqueIbanNumber(accountNo: String): String {
        while (true) {
            val ibanNumber = generateRandomIbanNumber(accountNo)
            val exists = withContext(Dispatchers.IO) {
                val querySnapshot = collectionReference
                    .whereEqualTo("ibanNumber", ibanNumber)
                    .get()
                    .await()
                !querySnapshot.isEmpty
            }
            if (!exists) return ibanNumber
        }
    }

    private fun generateRandomIbanNumber(accountNo: String): String {
        val accountNoWithoutDash = accountNo.replace("-", "")
        val countryCode = "TR"
        val checkDigits = Random.nextInt(10, 99).toString().padStart(2, '0')
        val bankCode = "00061"
        val accountDigit = Random.nextInt(100000, 1000000)
        return "$countryCode$checkDigits$bankCode$accountDigit$accountNoWithoutDash"
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

    suspend fun passwordValidation(enteredCustomerNo:String,enteredTC:String):Customer?{
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = collectionReference
                    .whereEqualTo("customerTc", enteredTC)
                    .whereEqualTo("customerNo", enteredCustomerNo)
                    .get()
                    .await()
                if (querySnapshot.isEmpty) {
                    Log.e("LoginError", "Hatalı Numara veya Parola")
                    return@withContext null
                } else {
                    return@withContext querySnapshot.documents[0].toObject(Customer::class.java)
                }
            }
            catch (e: Exception) {
                Log.e("LoginError", "Firebase sorgusunda hata: ${e.message}")
                return@withContext null
            }
        }
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
        val accountNo = generateUniqueAccountNo()
        val uniqueCustomerNo = generateUniqueCustomerNo()
        val uniqueIbanNumber = generateUniqueIbanNumber(accountNo)

        val newCustomer = Customer(
            "",
            costumerProfilePicture,
            customerName,
            customerTc,
            customerBirthDate,
            customerPhoneNumber,
            customerPassword,
            customersBalance,
            uniqueCustomerNo,
            accountNo,
            accountLocation,
            accountType,
            accountPurpose,
            uniqueIbanNumber
        )

        val documentRef = collectionReference.add(newCustomer).await()
        val customerId = documentRef.id
        newCustomer.customerId = customerId

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

        val collectionReference = FirebaseFirestore.getInstance().collection("Customers")
        val documentRef = collectionReference.document(customerId)

        documentRef.update(customerToUpdate)
            .addOnSuccessListener {
                Log.d("CustomerUpdate", "Customer successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.e("CustomerUpdate", "Error updating document", e)
            }
    }

    fun updatePassword(customerId:String,customerPassword:String){
        if (customerId.isEmpty()) {
            Log.e("CustomerUpdate", "Customer ID cannot be empty")
            return
        }
        val customerToUpdate=HashMap<String,Any>()
        customerToUpdate["customerPassword"]=customerPassword
        val collectionReference = FirebaseFirestore.getInstance().collection("Customers")
        val documentRef = collectionReference.document(customerId)
        documentRef.update(customerToUpdate)
            .addOnSuccessListener {
                Log.d("CustomerUpdate", "Customer successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.e("CustomerUpdate", "Error updating document", e)
            }
    }

    fun cancelRegistration(customerId: String) {
        collectionReference.document(customerId).delete()
    }

    fun customerAccountClosing(customerId: String) {
        collectionReference.document(customerId).delete()
    }

    fun customerInit(): List<Customer> {
        return listOf()
    }


    suspend fun isCustomersPasswordCorrect(customer: Customer?, enteredPassword: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val correctPassword = customer?.customerPassword
                return@withContext enteredPassword == correctPassword
            } catch (e: Exception) {
                Log.e("PasswordCheckError", "Şifre kontrolünde hata: ${e.message}")
                return@withContext false
            }
        }
    }


    fun onForgetPasswordClicked() {
        Log.e("ParolaUnuttum", "Parola sıfırlama isteği")
    }
}
