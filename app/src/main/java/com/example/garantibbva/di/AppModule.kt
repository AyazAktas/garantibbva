package com.example.garantibbva.di

import com.example.garantibbva.data.datasource.CorpDataSource
import com.example.garantibbva.data.datasource.CustomerDataSource
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.repository.CorpRepository
import com.example.garantibbva.data.repository.CustomerRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.internal.PrepareOp
import javax.inject.Singleton


import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    @Named("CustomerCollection")
    fun provideCustomerCollectionReference(): CollectionReference {
        return Firebase.firestore.collection("Customers")
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
    fun provideCorpCollectionReference(): CollectionReference {
        return Firebase.firestore.collection("Corps")
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
}
