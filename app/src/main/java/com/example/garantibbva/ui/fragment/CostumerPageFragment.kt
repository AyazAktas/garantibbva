package com.example.garantibbva.ui.fragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.data.datasource.TransactionDataSource
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.entity.Transaction
import com.example.garantibbva.databinding.FragmentCustomerPageBinding
import com.example.garantibbva.ui.viewmodel.CostumerPageViewModel
import com.example.garantibbva.ui.viewmodel.IbanTransferViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerPageFragment : Fragment() {
    private lateinit var binding: FragmentCustomerPageBinding
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var customerListener: ListenerRegistration
    private var customerId: String? = null
    private val customerPageViewModel: CostumerPageViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_page, container, false)
        binding.customerPageFragment = this

        val bundle: CustomerPageFragmentArgs by navArgs()
        val transactionCustomer = bundle.customer
        binding.customer = transactionCustomer
        binding.textViewAccountDetailsAction.setOnClickListener {
            val action=CustomerPageFragmentDirections.actionCustomerPageFragmentToAccountDetailsPersonalFragment(transactionCustomer)
            findNavController().navigate(action)
        }
        customerId = transactionCustomer?.customerId


        customerId?.let {
            startFirestoreListener(it)
        }

        val formattedBalance = String.format("%.2f TL", transactionCustomer.customersBalance)
        binding.textViewBalance.text = formattedBalance

        binding.imageViewTransfer.setOnClickListener {
            val action=CustomerPageFragmentDirections.actionCustomerPageFragmentToMoneyTransferFragment(null,transactionCustomer)
            findNavController().navigate(action)
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
                Log.w("CustomerPageFragment", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val updatedCustomer = snapshot.toObject(Customer::class.java)
                updatedCustomer?.let {
                    val formattedBalanceString = it.customersBalance.toString().replace(",", ".")
                    val formattedBalance = formattedBalanceString.toDoubleOrNull()

                    if (formattedBalance != null) {
                        it.customersBalance = formattedBalance
                        binding.customer = it
                        fetchLastTransaction(it.customerId!!, it.ibanNumber!!)
                    } else {
                        Log.e("CustomerPageFragment", "Invalid balance format: ${it.customersBalance}")
                    }
                }
            } else {
                Log.d("CustomerPageFragment", "Current data: null")
            }
        }
    }



    private fun fetchLastTransaction(customerId: String, customerIban: String) {
        lifecycleScope.launch {
            val transactions = customerPageViewModel.getCustomerTransactions(customerId, customerIban)
            val lastTransaction = transactions.maxByOrNull { it.date.toString() }
            lastTransaction?.let {
                binding.textViewTransactionType.text = it.transactionType
                if(customerIban==it.receiverIban){
                    binding.textViewAmount.text = " + ${it.amount} TL"
                }
                if (customerIban==it.senderIban){
                    binding.textViewAmount.text = " - ${it.amount} TL"
                }
                binding.textViewTransactionDetail.text = it.transactionId+" / "+it.receiverName+" "+it.description
                binding.textViewLastTransactionDate.text = it.date + " | " + it.transactionHour
            }
        }
    }
}
