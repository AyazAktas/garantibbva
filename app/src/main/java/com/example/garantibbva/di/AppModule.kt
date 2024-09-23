package com.example.garantibbva.di

import com.example.garantibbva.data.datasource.CorpDataSource
import com.example.garantibbva.data.datasource.CustomerDataSource
import com.example.garantibbva.data.datasource.TransactionDataSource
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.repository.CorpRepository
import com.example.garantibbva.data.repository.CustomerRepository
import com.example.garantibbva.data.repository.TransactionRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    @Named("CustomerCollection")
    fun provideCustomerCollectionReference(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection("Customers")
    }

    @Provides
    @Singleton
    fun provideCustomerDataSource(@Named("CustomerCollection") collectionReference: CollectionReference): CustomerDataSource {
        return CustomerDataSource(collectionReference)
    }

    @Provides
    @Singleton
    fun provideCustomerRepository(customerDataSource: CustomerDataSource): CustomerRepository {
        return CustomerRepository(customerDataSource)
    }

    @Provides
    @Singleton
    @Named("CorpCollection")
    fun provideCorpCollectionReference(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection("Corps")
    }

    @Provides
    @Singleton
    fun provideCorpDataSource(@Named("CorpCollection") collectionReference: CollectionReference): CorpDataSource {
        return CorpDataSource(collectionReference)
    }

    @Provides
    @Singleton
    fun provideCorpRepository(corpDataSource: CorpDataSource): CorpRepository {
        return CorpRepository(corpDataSource)
    }

    @Provides
    @Singleton
    @Named("TransactionCollection")
    fun provideTransactionCollectionReference(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection("Transactions")
    }

    @Provides
    @Singleton
    fun provideTransactionDataSource(firestore: FirebaseFirestore): TransactionDataSource {
        return TransactionDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(transactionDataSource: TransactionDataSource): TransactionRepository {
        return TransactionRepository(transactionDataSource)
    }
}
