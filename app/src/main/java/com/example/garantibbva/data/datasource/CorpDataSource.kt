package com.example.garantibbva.data.datasource

import android.util.Log
import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.entity.Customer
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.random.Random

class CorpDataSource(val collectionReferenceCorp: CollectionReference) {

    private fun generateRandomCorpAccountNo(): String {
        val part1 = Random.nextInt(1000, 9999)
        val part2 = Random.nextLong(1000000, 9999999)
        return "$part1-$part2"
    }

    private suspend fun generateUniqueCorpAccountNo(): String {
        while (true) {
            val accountNo = generateRandomCorpAccountNo()
            val exists = withContext(Dispatchers.IO) {
                val querySnapshot = collectionReferenceCorp
                    .whereEqualTo("accountNo", accountNo)
                    .get()
                    .await()
                !querySnapshot.isEmpty
            }
            if (!exists) return accountNo
        }
    }

    private suspend fun generateUniqueContactPersonsCustomerNo(): String {
        while (true) {
            val customerNo = generateRandomContactPersonsCustomerNo()
            val exists = withContext(Dispatchers.IO) {
                val querySnapshot = collectionReferenceCorp
                    .whereEqualTo("customerNo", customerNo)
                    .get()
                    .await()
                !querySnapshot.isEmpty
            }
            if (!exists) return customerNo
        }
    }

    private fun generateRandomContactPersonsCustomerNo(): String {
        val part2 = Random.nextLong(1000000, 9999999)
        return "$part2"
    }

    private suspend fun generateUniqueCorpIbanNumber(accountNo: String): String {
        while (true) {
            val ibanNumber = generateRandomCorpIbanNumber(accountNo)
            val exists = withContext(Dispatchers.IO) {
                val querySnapshot = collectionReferenceCorp
                    .whereEqualTo("ibanNumber", ibanNumber)
                    .get()
                    .await()
                !querySnapshot.isEmpty
            }
            if (!exists) return ibanNumber
        }
    }

    private fun generateRandomCorpIbanNumber(accountNo: String): String {
        val accountNoWithoutDash = accountNo.replace("-", "")
        val countryCode = "TR"
        val checkDigits = Random.nextInt(10, 99).toString().padStart(2, '0')
        val bankCode = "00061"
        return "$countryCode$checkDigits$bankCode$accountNoWithoutDash"
    }



    suspend fun corpCustomerRegister(
        corpName:String,
        taxIdNumber:String,
        registrationNumber: String,
        address:String,
        corpPhoneNumber:String,
        corpEmail:String,
        contactPersonName: String,
        contactPersonPhone: String,
        contactPersonEmail: String,
        industry: String,
        contactPersonTc:String,
        accountType: String,
        dateOfIncorporation:String,
        annualRevenue: Double,
        numberOfEmployees: String,
        accountBalance: Double,
        accountPurpose: String,
        password:String
    ):Corp{
        val corpAccountNo= generateUniqueCorpAccountNo()
        val contactPersonsCustomerNo=generateUniqueContactPersonsCustomerNo()
        val iban=generateUniqueCorpIbanNumber(corpAccountNo)

        val newCorp=Corp(
            "",
            corpAccountNo,
            corpName,
            taxIdNumber,
            registrationNumber,
            address,
            corpPhoneNumber,
            corpEmail,
            contactPersonName,
            contactPersonPhone,
            contactPersonEmail,
            industry,
            contactPersonTc,
            contactPersonsCustomerNo,
            accountType,
            dateOfIncorporation,
            annualRevenue,
            numberOfEmployees,
            accountBalance,
            accountPurpose,
            iban,
            password
        )
        val documentRef=collectionReferenceCorp.add(newCorp).await()
        val corpId=documentRef.id
        newCorp.corpId=corpId

        documentRef.set(newCorp).await()
        return newCorp
    }


    fun corpRegisterOtherInfos(corpId: String,
                               corpName: String,
                               taxIdNumber: String,
                               registrationNumber: String,
                               address: String,
                               corpPhoneNumber: String,
                               corpEmail: String,
                               industry: String,
                               dateOfIncorporation: String,
                               numberOfEmployees: String,
                               contactPersonName: String,
                               contactPersonPhone: String,
                               contactPersonEmail: String,
                               contactPersonTc: String,
                               password: String){
        if (corpId.isEmpty()){
            Log.e("CorpUpdate", "Corp ID cannot be empty")
            return
        }

        val corpToUpdate=HashMap<String,Any>()
        corpToUpdate["corpName"]=corpName
        corpToUpdate["taxIdNumber"]=taxIdNumber
        corpToUpdate["registrationNumber"]=registrationNumber
        corpToUpdate["address"]=address
        corpToUpdate["corpPhoneNumber"]=corpPhoneNumber
        corpToUpdate["corpEmail"]=corpEmail
        corpToUpdate["contactPersonName"]=contactPersonName
        corpToUpdate["contactPersonPhone"]=contactPersonPhone
        corpToUpdate["contactPersonEmail"]=contactPersonEmail
        corpToUpdate["industry"]=industry
        corpToUpdate["contactPersonTc"]=contactPersonTc
        corpToUpdate["dateOfIncorporation"]=dateOfIncorporation
        corpToUpdate["numberOfEmployees"]=numberOfEmployees
        corpToUpdate["password"]=password

        val collectionReference=FirebaseFirestore.getInstance().collection("Corps")
        val documentRef = collectionReference.document(corpId)
        documentRef.update(corpToUpdate)
            .addOnSuccessListener {
                Log.d("CorpUpdate", "Corp successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.e("CorpUpdate", "Error updating document", e)
            }

    }

    fun cancelRegistration(corpId: String) {
        collectionReferenceCorp.document(corpId).delete()
    }


    suspend fun login(enteredTcNo:String,enteredContactsCustomerNo:String,enteredPassword:String):Corp?{
        return withContext(Dispatchers.IO){
            try {
                val querySnapshot=collectionReferenceCorp
                    .whereEqualTo("contactPersonTc",enteredTcNo)
                    .whereEqualTo("contactPersonsCustomerNo",enteredContactsCustomerNo)
                    .whereEqualTo("password",enteredPassword)
                    .get()
                    .await()
                if (querySnapshot.isEmpty){
                    Log.e("LoginError", "Hatalı Numara veya Parola")
                    return@withContext null
                }
                else{
                    return@withContext querySnapshot.documents[0].toObject(Corp::class.java)
                }
            }
            catch (e:Exception){
                Log.e("LoginERROE","Firebase sorgusunda hata: ${e.message}")
                return@withContext null
            }
        }
    }


}