package com.example.garantibbva.di

import android.app.Application
import com.example.garantibbva.data.datasource.CustomerDataSource
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltAndroidApp
class HiltApplication:Application() {
}