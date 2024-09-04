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
import com.example.garantibbva.databinding.FragmentCorpPageBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CorpPageFragment : Fragment() {
    private lateinit var binding:FragmentCorpPageBinding
    private var corpId: String? = null
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var corpListener: ListenerRegistration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_corp_page,container,false)
        binding.corpPageFragment=this
        val bundle:CorpPageFragmentArgs by navArgs()
        val transactionCorp=bundle.corp
        binding.corp=transactionCorp

        binding.textViewCorrpAccountDetail.setOnClickListener {
            val action=CorpPageFragmentDirections.actionCorpPageFragmentToAccountDetailsCorpFragment(transactionCorp)
            findNavController().navigate(action)
        }


        corpId=transactionCorp?.corpId
        corpId?.let{
            startFirestoreListener(it)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        corpListener.remove()
    }

    private fun startFirestoreListener(corpId:String){
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


}