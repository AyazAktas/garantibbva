package com.example.garantibbva.data.datasource

import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.entity.Transaction
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TransactionDataSource(private val firestore: FirebaseFirestore) {

    suspend fun getReceiverNameByIban(iban: String): String {
        val customerQuery = firestore.collection("Customers")
            .whereEqualTo("ibanNumber", iban)
            .get()
            .await()

        if (customerQuery.documents.isNotEmpty()) {
            val customer = customerQuery.documents.first().toObject(Customer::class.java)
            return customer?.customerName ?: "Kişi Bulunamadı."
        }

        val corpQuery = firestore.collection("Corps")
            .whereEqualTo("iban", iban)
            .get()
            .await()

        if (corpQuery.documents.isNotEmpty()) {
            val corp = corpQuery.documents.first().toObject(Corp::class.java)
            return corp?.corpName ?: "Kişi Bulunamadı."
        }

        return "Unknown"
    }

    suspend fun saveTransaction(transaction: Transaction) {
        val documentReference = firestore.collection("Transactions").add(transaction).await()
        transaction.transactionId = documentReference.id
        documentReference.set(transaction).await()
    }
}
