package com.example.garantibbva.di

import com.example.garantibbva.data.datasource.CustomerDataSource
import com.example.garantibbva.data.entity.Customer
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


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCustomerDataSource(collectionReference: CollectionReference) : CustomerDataSource{
        return CustomerDataSource(collectionReference)
    }

    @Provides
    @Singleton
    fun provideCustomerRepository(customerDataSource:CustomerDataSource):CustomerRepository{
        return CustomerRepository(customerDataSource)
    }

    @Provides
    @Singleton
    fun provideCollectionReference() : CollectionReference{
        return Firebase.firestore.collection("Customers")
    }

}