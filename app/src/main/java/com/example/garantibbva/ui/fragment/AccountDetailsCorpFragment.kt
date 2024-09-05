package com.example.garantibbva.ui.fragment

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.graphics.Color
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

        corpId=transactionCorp?.corpId
        corpId?.let {
            startFirestoreListener(it)
        }

        val formattedIban=formatIban(transactionCorp.iban.toString())
        binding.textViewCorpIban.text=formattedIban

        val formattedAccountType=formatAccountType(transactionCorp.accountType.toString())
        binding.textViewCorpAccountType.text=formattedAccountType

        binding.buttonCorpAccountClosing.setOnClickListener {
            if (transactionCorp != null) {
                val action = AccountDetailsPersonalFragmentDirections.actionAccountDetailsPersonalFragmentToAccountClosingFragment(transactionCorp,null)
                findNavController().navigate(action)
            } else {
                Log.e("AccountClosing", "Customer data is null!")
            }
        }



        binding.buttonCorpAccountClosing.setOnClickListener {
            firestore.collection("Corps").document(corpId!!).get().addOnSuccessListener { document ->
                val currentBalance = document.getDouble("accountBalance")

                if (currentBalance != null && currentBalance != 0.0) {
                    val builder = AlertDialog.Builder(requireContext())
                    val messageTextView = TextView(requireContext())
                    messageTextView.text = "Bilgilendirme\nHesabınızda bakiye bulunduğu için işleminizi gerçekleştiremiyoruz. Lütfen hesabınızdaki tutarı başka bir hesaba aktararak, kapama işleminizi tekrar deneyin."
                    messageTextView.setTextAppearance(android.R.style.TextAppearance_Medium)
                    messageTextView.setPadding(32, 32, 32, 32)
                    messageTextView.textSize = 16f
                    val spannableString = SpannableString(messageTextView.text)
                    spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, 13, 0)
                    messageTextView.text = spannableString
                    builder.setView(messageTextView)
                        .setPositiveButton("Tamam") { dialog, id ->
                            dialog.dismiss()
                        }
                    val alertDialog = builder.create()
                    alertDialog.show()
                } else {
                    if (transactionCorp != null) {
                        val action = AccountDetailsCorpFragmentDirections.actionAccountDetailsCorpFragmentToAccountClosingFragment(transactionCorp,null)
                        findNavController().navigate(action)
                    } else {
                        Log.e("AccountClosing", "Customer data is null!")
                    }
                }
            }
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
