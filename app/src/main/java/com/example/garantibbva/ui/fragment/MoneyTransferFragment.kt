package com.example.garantibbva.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateValueAsState
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.databinding.FragmentMoneyTransferBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoneyTransferFragment : Fragment() {
    private lateinit var binding:FragmentMoneyTransferBinding
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private var corpListener: ListenerRegistration? = null
    private var customerListener: ListenerRegistration? = null
    private var customerId: String? = null
    private var corpId: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_money_transfer,container,false)
        binding.fragmentMoneyTransfer=this
        val args:MoneyTransferFragmentArgs by navArgs()
        val transactionCustomer = args.customer
        val transactionCorp = args.corp

        binding.customer = transactionCustomer
        binding.corp = transactionCorp

        corpId=transactionCorp?.corpId
        corpId?.let{
            firestoreListenerCorp(it)
        }

        customerId=transactionCustomer?.customerId
        customerId?.let{
            firestoreListenerCustomer(it)
        }

        transactionCustomer?.let {
            binding.cardViewIban.setOnClickListener {
                val action=MoneyTransferFragmentDirections.actionMoneyTransferFragmentToIbanTransferFragment(transactionCustomer,null)
                findNavController().navigate(action)
            }
            binding.toolbarImage.setOnClickListener {
                val action=MoneyTransferFragmentDirections.actionMoneyTransferFragmentToCustomerPageFragment(transactionCustomer)
                findNavController().navigate(action)
            }
        }
        transactionCorp?.let {
            binding.cardViewIban.setOnClickListener {
                val action=MoneyTransferFragmentDirections.actionMoneyTransferFragmentToIbanTransferFragment(null,transactionCorp)
                findNavController().navigate(action)
            }
            binding.toolbarImage.setOnClickListener {
                val action=MoneyTransferFragmentDirections.actionMoneyTransferFragmentToCorpPageFragment(transactionCorp)
                findNavController().navigate(action)
            }
        }


        return binding.root
    }
    private fun firestoreListenerCorp(corpId:String){
        val documentRef=firestore.collection("Corps").document(corpId)
        corpListener=documentRef.addSnapshotListener{snapshot,e->
            if (e!=null){
                Log.w("CorpPageFragment","Listen Failed!",e)
                return@addSnapshotListener
            }
            if (snapshot!=null&&snapshot.exists()){
                val updatedCorp=snapshot.toObject(Corp::class.java)
                binding.corp=updatedCorp
            }
            else{
                Log.d("CorpPageFragment","Current Data: NULL!!")
            }
        }
    }

    private fun firestoreListenerCustomer(customerId:String){
        val documentRef=firestore.collection("Customers").document(customerId)
        customerListener=documentRef.addSnapshotListener{snapshot,e->
            if (e!=null){
                Log.w("-","Listen Failed!",e)
                return@addSnapshotListener
            }
            if (snapshot!=null&&snapshot.exists()){
                val updatedCustomer=snapshot.toObject(Customer::class.java)
                binding.customer=updatedCustomer
            }
            else{
                Log.d("-","Current Data: NULL!!")
            }
        }
    }
}