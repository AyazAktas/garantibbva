package com.example.garantibbva.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.data.entity.Corp
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.entity.Transaction
import com.example.garantibbva.databinding.FragmentIbanTransferBinding
import com.example.garantibbva.ui.viewmodel.IbanTransferViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class IbanTransferFragment : Fragment() {
    private lateinit var binding: FragmentIbanTransferBinding
    private val ibanTransferViewModel: IbanTransferViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_iban_transfer, container, false)
        binding.fragmentIbanTransfer = this

        val args: IbanTransferFragmentArgs by navArgs()
        val transactionCustomer = args.customer
        val transactionCorp = args.corp

        binding.customer = transactionCustomer
        binding.corp = transactionCorp

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        binding.editTextDate.setText(dateFormat.format(calendar.time))

        binding.editTextDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), { _, dayOfMonth, month, year ->
                calendar.set(dayOfMonth, month, year)
                binding.editTextDate.setText(dateFormat.format(calendar.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }

        transactionCustomer?.let {
            binding.textViewAccountBalance.text = "Kullanılabilir Bakiye = ${transactionCustomer.customersBalance} TL"
            binding.textViewAccInfo.text = "${transactionCustomer.accountNo} - ${transactionCustomer.accountLocation}"

            binding.toolbarImage.setOnClickListener {
                val action = IbanTransferFragmentDirections.actionIbanTransferFragmentToMoneyTransferFragment(null, transactionCustomer)
                findNavController().navigate(action)
            }
        }

        transactionCorp?.let {
            binding.textViewAccountBalance.text = "Kullanılabilir Bakiye = ${transactionCorp.accountBalance} TL"
            binding.textViewAccInfo.text = "${transactionCorp.corpAccountNo} - ${transactionCorp.address}"

            binding.toolbarImage.setOnClickListener {
                val action = IbanTransferFragmentDirections.actionIbanTransferFragmentToMoneyTransferFragment(transactionCorp, null)
                findNavController().navigate(action)
            }
        }

        binding.buttonContinue.setOnClickListener {
            val senderIban = transactionCustomer?.ibanNumber ?: transactionCorp?.iban
            val receiverIban = binding.editTextText.text.toString()
            val amount = binding.editTextNumberAmount.text.toString().toDoubleOrNull() ?: 0.0
            val description = binding.editTextDesc.text.toString()
            val date = binding.editTextDate.text.toString()

            lifecycleScope.launch {
                if (!isValidIban(receiverIban)) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Geçersiz IBAN")
                        .setMessage("IBAN 'TR' ile başlamalı ve doğru formatta olmalıdır.")
                        .setPositiveButton("Tamam") { dialog, _ -> dialog.dismiss() }
                        .show()
                    return@launch
                }

                val receiverName = findReceiverName(receiverIban)
                val senderName = transactionCustomer?.customerName ?: transactionCorp?.corpName
                val senderId = transactionCustomer?.customerId ?: transactionCorp?.corpId
                val senderBalance = transactionCustomer?.customersBalance ?: transactionCorp?.accountBalance

                if (receiverIban == senderIban) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Hata")
                        .setMessage("Kendi IBAN'ınıza para gönderemezsiniz.")
                        .setPositiveButton("Tamam") { dialog, _ -> dialog.dismiss() }
                        .show()
                    return@launch
                }

                if (amount <= 0) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Hata")
                        .setMessage("Gönderim tutarı sıfır veya negatif olamaz.")
                        .setPositiveButton("Tamam") { dialog, _ -> dialog.dismiss() }
                        .show()
                    return@launch
                }

                if (receiverName == "Geçerli bir IBAN giriniz") {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Geçersiz IBAN")
                        .setMessage("Girdiğiniz IBAN kayıtlarda bulunamadı. Lütfen doğru bir IBAN giriniz.")
                        .setPositiveButton("Tamam") { dialog, _ -> dialog.dismiss() }
                        .show()
                    return@launch
                }

                if (amount > (senderBalance ?: 0.0)) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Yetersiz Bakiye")
                        .setMessage("Bakiyeniz bu işlemi gerçekleştirebilmek için yeterli değil. Lütfen gönderilmek istenen tutarı güncelleyiniz veya işlemi iptal ediniz.")
                        .setPositiveButton("Tamam") { dialog, _ -> dialog.dismiss() }
                        .show()
                } else {
                    val transaction = Transaction(
                        senderIban = senderIban ?: "",
                        receiverIban = receiverIban,
                        amount = amount,
                        date = date,
                        description = description,
                        senderName = senderName,
                        receiverName = receiverName,
                        senderId = senderId
                    )
                    ibanTransferViewModel.saveTransaction(transaction)
                    transactionCorp?.let {
                        val action=IbanTransferFragmentDirections.actionIbanTransferFragmentToIbanTransferConfirmFragment(transaction,null,transactionCorp)
                        findNavController().navigate(action)
                    }
                    transactionCustomer?.let {
                        val action=IbanTransferFragmentDirections.actionIbanTransferFragmentToIbanTransferConfirmFragment(transaction,transactionCustomer,null)
                        findNavController().navigate(action)
                    }

                }
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    private suspend fun findReceiverName(iban: String): String {
        return ibanTransferViewModel.getReceiverNameByIban(iban)
            .takeIf { it.isNotEmpty() && it != "Unknown" }
            ?: "Geçerli bir IBAN giriniz"
    }

    private fun isValidIban(iban: String): Boolean {
        return iban.startsWith("TR") && iban.length == 26
    }

}
