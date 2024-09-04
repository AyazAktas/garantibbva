package com.example.garantibbva.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.databinding.FragmentAccountDetailsCorpBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountDetailsCorpFragment : Fragment() {
    private lateinit var binding:FragmentAccountDetailsCorpBinding
    private var corpId: String? = null
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var corpListener: ListenerRegistration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_account_details_corp,container,false)
        binding.corpAccountDetailFragment=this
        val bundle:AccountDetailsCorpFragmentArgs by navArgs()
        val transactionCorp=bundle.corp
        binding.corp=transactionCorp

        binding.toolbarImage.setOnClickListener {
            val action=AccountDetailsCorpFragmentDirections.actionAccountDetailsCorpFragmentToCorpPageFragment(transactionCorp)
            findNavController().navigate(action)
        }

        val formattedIban=formatIban(transactionCorp.iban.toString())
        binding.textViewCorpIban.text=formattedIban

        val formattedAccountType=formatAccountType(transactionCorp.accountType.toString())
        binding.textViewCorpAccountType.text=formattedAccountType


        corpId=transactionCorp?.corpId
        corpId?.let {
            startFirestoreListener(it)
        }
        return binding.root
    }

    private fun formatIban(iban:String):String{
        return iban.chunked(4).joinToString(" ")
    }

    private fun formatAccountType(accountType: String): String {
        return accountType
            .replace("[", "")
            .replace("]", "")
            .trim()
    }



    private fun startFirestoreListener(corpId: String) {
        val documentRef = firestore.collection("Corps").document(corpId)

        corpListener = documentRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("AccountDetail", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val updatedCorp = snapshot.toObject(Corp::class.java)
                binding.corp = updatedCorp
            } else {
                Log.d("AccountDetail", "Current data: null")
            }
        }
    }

}
