package com.example.garantibbva.ui.fragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.databinding.FragmentCustomerPageBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

@AndroidEntryPoint
class CustomerPageFragment : Fragment() {
    private lateinit var binding: FragmentCustomerPageBinding
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var customerListener: ListenerRegistration
    private var customerId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_page, container, false)
        binding.customerPageFragment = this

        val bundle: CustomerPageFragmentArgs by navArgs()
        val transactionCustomer = bundle.customer
        binding.customer = transactionCustomer

        customerId = transactionCustomer?.customerId

        customerId?.let {
            startFirestoreListener(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        customerListener.remove()
    }

    private fun startFirestoreListener(customerId: String) {
        val documentRef = firestore.collection("Customers").document(customerId)

        customerListener = documentRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Hata durumunda log kaydı
                Log.w("CustomerPageFragment", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val updatedCustomer = snapshot.toObject(Customer::class.java)
                binding.customer = updatedCustomer
            } else {
                Log.d("CustomerPageFragment", "Current data: null")
            }
        }
    }
}
