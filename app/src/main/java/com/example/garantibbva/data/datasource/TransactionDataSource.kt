package com.example.garantibbva.data.datasource

import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.entity.Transaction
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TransactionDataSource(private val firestore: FirebaseFirestore) {

    private val transactionCollection = firestore.collection("transactions")

    suspend fun addTransaction(transaction: Transaction) {
        transactionCollection.add(transaction).await()
    }

    suspend fun getTransactions(): List<Transaction> {
        val snapshot = transactionCollection.get().await()
        return snapshot.toObjects(Transaction::class.java)
    }

    suspend fun getTransactionById(transactionId: String): Transaction? {
        val snapshot = transactionCollection.whereEqualTo("transactionId", transactionId).get().await()
        return snapshot.documents.firstOrNull()?.toObject(Transaction::class.java)
    }

    suspend fun transferMoney(
        sender: Any,
        receiver: Any,
        amount: Double,
        description: String
    ): Boolean {
        val transaction = performTransfer(sender, receiver, amount, description)

        return if (transaction != null) {
            transactionCollection.add(transaction).await()
            true
        } else {
            false
        }
    }

    private fun performTransfer(
        sender: Any,
        receiver: Any,
        amount: Double,
        description: String
    ): Transaction? {
        val senderIban: String?
        val receiverIban: String?
        val senderName: String?
        val receiverName: String?

        when (sender) {
            is Customer -> {
                senderIban = sender.ibanNumber
                senderName = sender.customerName
                if (sender.customersBalance != null && sender.customersBalance!! >= amount) {
                    sender.customersBalance = sender.customersBalance!! - amount
                } else {
                    return null
                }
            }
            is Corp -> {
                senderIban = sender.iban
                senderName = sender.corpName
                if (sender.accountBalance != null && sender.accountBalance!! >= amount) {
                    sender.accountBalance = sender.accountBalance!! - amount
                } else {
                    return null
                }
            }
            else -> return null
        }

        when (receiver) {
            is Customer -> {
                receiverIban = receiver.ibanNumber
                receiverName = receiver.customerName
                receiver.customersBalance = receiver.customersBalance!! + amount
            }
            is Corp -> {
                receiverIban = receiver.iban
                receiverName = receiver.corpName
                receiver.accountBalance = receiver.accountBalance!! + amount
            }
            else -> return null
        }

        return Transaction(
            transactionId = generateTransactionId(),
            senderIban = senderIban,
            receiverIban = receiverIban,
            amount = amount,
            date = getCurrentDate(),
            description = description,
            senderName = senderName,
            receiverName = receiverName
        )
    }

    private fun generateTransactionId(): String {
        return java.util.UUID.randomUUID().toString()
    }

    private fun getCurrentDate(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }


}
