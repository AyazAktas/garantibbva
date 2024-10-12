package com.example.garantibbva.data.datasource

import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.entity.Transaction
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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


    fun cancelTransfer(transactionId: String) {
        firestore.collection("Transactions").document(transactionId).delete()
    }


    suspend fun saveTransaction(transaction: Transaction) {
        val documentReference = firestore.collection("Transactions").add(transaction).await()
        transaction.transactionId = documentReference.id
        documentReference.set(transaction).await()
    }

    fun makeTransfer(iban: String, amount: Double?, senderId: String, totalAmount: Double) {
        // Alıcının bakiyesini güncelleyin
        firestore.collection("Customers")
            .whereEqualTo("ibanNumber", iban)
            .get()
            .addOnSuccessListener { customerQuery ->
                if (customerQuery.documents.isNotEmpty()) {
                    val customer = customerQuery.documents.first().toObject(Customer::class.java)
                    if (customer != null) {
                        customer.customersBalance = customer.customersBalance!! + amount!!
                        // Bakiyeyi güncelleyin
                        customerQuery.documents.first().reference
                            .update("customersBalance", customer.customersBalance)
                    }
                } else {
                    firestore.collection("Corps")
                        .whereEqualTo("iban", iban)
                        .get()
                        .addOnSuccessListener { corpQuery ->
                            if (corpQuery.documents.isNotEmpty()) {
                                val corp = corpQuery.documents.first().toObject(Corp::class.java)
                                if (corp != null) {
                                    corp.accountBalance = corp.accountBalance!! + amount!!
                                    // Bakiyeyi güncelleyin
                                    corpQuery.documents.first().reference
                                        .update("accountBalance", corp.accountBalance)
                                }
                            }
                        }
                }
            }

        firestore.collection("Customers")
            .whereEqualTo("customerId", senderId)
            .get()
            .addOnSuccessListener { customerQuery ->
                if (customerQuery.documents.isNotEmpty()) {
                    val customer = customerQuery.documents.first().toObject(Customer::class.java)
                    if (customer != null) {
                        customer.customersBalance = customer.customersBalance?.minus(totalAmount)
                        customerQuery.documents.first().reference
                            .update("customersBalance", customer.customersBalance)
                    }
                } else {
                    firestore.collection("Corps")
                        .whereEqualTo("corpId", senderId)
                        .get()
                        .addOnSuccessListener { corpQuery ->
                            if (corpQuery.documents.isNotEmpty()) {
                                val corp = corpQuery.documents.first().toObject(Corp::class.java)
                                if (corp != null) {
                                    corp.accountBalance = corp.accountBalance!! - totalAmount
                                    // Bakiyeyi güncelleyin
                                    corpQuery.documents.first().reference
                                        .update("accountBalance", corp.accountBalance)
                                }
                            }
                        }
                }
            }
    }

    suspend fun getCustomerTransactions(customerId: String, customerIban: String): List<Transaction> {
        val transactions = mutableListOf<Transaction>()

        // Gönderen olarak müşteri işlemlerini al
        val senderTransactionsQuery = firestore.collection("Transactions")
            .whereEqualTo("senderId", customerId)
            .get()
            .await()

        if (!senderTransactionsQuery.isEmpty) {
            for (doc in senderTransactionsQuery.documents) {
                val transaction = doc.toObject(Transaction::class.java)
                transaction?.let { transactions.add(it) }
            }
        }

        // Alıcı olarak müşteri işlemlerini al
        val receiverTransactionsQuery = firestore.collection("Transactions")
            .whereEqualTo("receiverIban", customerIban)
            .get()
            .await()

        if (!receiverTransactionsQuery.isEmpty) {
            for (doc in receiverTransactionsQuery.documents) {
                val transaction = doc.toObject(Transaction::class.java)
                transaction?.let { transactions.add(it) }
            }
        }

        return transactions
    }

}
