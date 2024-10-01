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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.databinding.FragmentAccountDetailsPersonalBinding
import com.example.garantibbva.ui.viewmodel.CostumerPageViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountDetailsPersonalFragment : Fragment() {
    private lateinit var binding: FragmentAccountDetailsPersonalBinding
    private var customerId: String? = null
    private var customerIban:String?=null
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var customerListener: ListenerRegistration
    private val customerPageViewModel: CostumerPageViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_details_personal, container, false)
        binding.accountDetailsPersonalFragment = this
        val bundle: AccountDetailsPersonalFragmentArgs by navArgs()
        val transactionCustomer = bundle.customer
        binding.customer = transactionCustomer


        val formattedIban = formatIban(transactionCustomer.ibanNumber.toString())
        binding.textViewIbanNO.text = formattedIban

        val formattedAccountType = formatAccountType(transactionCustomer.accountType.toString())
        binding.textViewAccountType.text = formattedAccountType

        binding.toolbarImage.setOnClickListener {
            val action =
                AccountDetailsPersonalFragmentDirections.actionAccountDetailsPersonalFragmentToCustomerPageFragment(
                    transactionCustomer
                )
            findNavController().navigate(action)
        }

        customerId = transactionCustomer?.customerId
        customerIban=transactionCustomer?.ibanNumber

        customerId?.let {
            startFirestoreListener(it, customerIban.toString())
        }


        binding.buttonAccountClosing.setOnClickListener {
            firestore.collection("Customers").document(customerId!!).get().addOnSuccessListener { document ->
                val currentBalance = document.getDouble("customersBalance")

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
                    if (transactionCustomer != null) {
                        val action = AccountDetailsPersonalFragmentDirections.actionAccountDetailsPersonalFragmentToAccountClosingFragment(null,transactionCustomer)
                        findNavController().navigate(action)
                    } else {
                        Log.e("AccountClosing", "Customer data is null!")
                    }
                }
            }
        }


        return binding.root
    }

    private fun formatIban(iban: String): String {
        return iban.chunked(4).joinToString(" ")
    }
    private fun formatAccountType(accountType: String): String {
        return accountType
            .replace("[", "")
            .replace("]", "")
            .trim()
    }



    private fun startFirestoreListener(customerId: String,customerIban: String) {
        val documentRef = firestore.collection("Customers").document(customerId)

        customerListener = documentRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Hata durumunda log kaydı
                Log.w("AccountDetail", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val updatedCustomer = snapshot.toObject(Customer::class.java)
                binding.customer = updatedCustomer
                fetchLastTransaction(customerId, customerIban)
            } else {
                Log.d("CustomerPageFragment", "Current data: null")
            }
        }
    }


    private fun fetchLastTransaction(customerId: String,customerIban:String){
        lifecycleScope.launch {
            val transactions=customerPageViewModel.getCustomerTransactions(customerId,customerIban)
            val lastTransaction=transactions.maxByOrNull {it.date.toString()}
            lastTransaction?.let {
                binding.textViewLastDeparture.text=it.date
            }
        }
    }
}
