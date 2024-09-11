package com.example.garantibbva.ui.fragment

import android.os .Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentIbanTransferConfirmBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class IbanTransferConfirmFragment : Fragment() {
    private lateinit var binding: FragmentIbanTransferConfirmBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_iban_transfer_confirm,container,false)
        binding.ibanTransferConfirmFragment=this
        val bundle:IbanTransferConfirmFragmentArgs by navArgs()

        val transaction = bundle.transaction
        val transactionCustomer = bundle.customer
        val transactionCorp = bundle.corp

        binding.transaction = transaction
        binding.customer = transactionCustomer
        binding.corp = transactionCorp

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val isWeekend = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY

        binding.toolbarImage.setOnClickListener {
            val action=IbanTransferConfirmFragmentDirections.actionIbanTransferConfirmFragmentToIbanTransferFragment2(transactionCustomer,transactionCorp)
            findNavController().navigate(action)
        }

        transactionCustomer?.let {
            binding.textViewAccountNoLocation.text=transactionCustomer.accountNo+"   "+transactionCustomer.accountLocation
            binding.textView93.text="Bireysel Ödeme"
            if (transactionCustomer.accountType?.contains("Maaş Müşterisi")==true && !isWeekend){
                binding.textViewTransactionCost.text="0,O TL"
            }
            else{
                binding.textViewMaas.text=""
                binding.textViewTransactionCost.text="4,43 TL"
            }
        }
        //iban formatla

        transactionCorp?.let {
            binding.textViewAccountNoLocation.text=transactionCorp.corpAccountNo+" "+transactionCorp.address
            binding.textView93.text="Kurum Ödemesi"
            binding.textViewMaas.text=""
            binding.textViewTransactionCost.text="4,43 TL"
        }


        return binding.root
    }
}