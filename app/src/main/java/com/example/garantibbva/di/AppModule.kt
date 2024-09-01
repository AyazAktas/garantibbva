package com.example.garantibbva.di

import com.example.garantibbva.data.datasource.CustomerDataSource
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.repository.CustomerRepository
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
    fun provideCustomerDataSource():CustomerDataSource{
        return CustomerDataSource()
    }

    @Provides
    @Singleton
    fun provideCustomerRepository(customerDataSource:CustomerDataSource):CustomerRepository{
        return CustomerRepository(customerDataSource)
    }

}