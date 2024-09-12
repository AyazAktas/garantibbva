package com.example.garantibbva.ui.fragment

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os .Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.MainActivity
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentIbanTransferConfirmBinding
import com.example.garantibbva.ui.viewmodel.IbanTransferViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class IbanTransferConfirmFragment : Fragment() {
    private lateinit var binding: FragmentIbanTransferConfirmBinding
    private val ibanTransferViewModel: IbanTransferViewModel by viewModels()
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
            AlertDialog.Builder(requireContext())
                .setTitle("Transfer İptali")
                .setMessage("Şuan onaylamak üzere olduğunuz işlem iptal edilecektir ve eğer işleme devam etmek istiyorsanız bilgileri tekrar girmek zorunda olacaksınız. Onaylıyor musunuz?")
                .setPositiveButton("Evet") { dialog, id -> lifecycleScope.launch {ibanTransferViewModel.cancelTransfer(transaction.transactionId.toString())
                    val action=IbanTransferConfirmFragmentDirections.actionIbanTransferConfirmFragmentToIbanTransferFragment2(transactionCustomer,transactionCorp)
                    findNavController().navigate(action) }}
                .setNegativeButton("Hayır"){dialog, _ -> dialog.dismiss()}
                .show()
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

        transactionCorp?.let {
            binding.textViewAccountNoLocation.text=transactionCorp.corpAccountNo+" "+transactionCorp.address
            binding.textView93.text="Kurum Ödemesi"
            binding.textViewMaas.text=""
            binding.textViewTransactionCost.text="4,43 TL"
        }

        binding.buttonCancel.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Transfer İptali")
                .setMessage("İşlemi gerçekten iptal etmek istiyor musunuz?")
                .setPositiveButton("Evet") { dialog, id -> lifecycleScope.launch {ibanTransferViewModel.cancelTransfer(transaction.transactionId.toString())
                    val action=IbanTransferConfirmFragmentDirections.actionIbanTransferConfirmFragmentToMoneyTransferFragment(transactionCorp,transactionCustomer)
                    findNavController().navigate(action)}}
                .setNegativeButton("Hayır"){dialog, _ -> dialog.dismiss()}
                .show()
        }

        binding.buttonConfirm.setOnClickListener {
            lifecycleScope.launch {
                AlertDialog.Builder(requireContext())
                    .setTitle("Transfer Onayı")
                    .setMessage("İşlemi Onaylıyor musunuz ?")
                    .setPositiveButton("Evet") { dialog, id ->
                        lifecycleScope.launch {
                            val transactionCost: Double = if (transactionCustomer?.accountType?.contains("Maaş Müşterisi") == true && !isWeekend) {
                                0.0
                            } else {
                                4.43
                            }
                            val totalAmount = transaction.amount!! + transactionCost
                            ibanTransferViewModel.makeTransfer(
                                transaction.receiverIban.toString(),
                                transaction.amount,
                                transaction.senderId.toString(),
                                totalAmount
                            )
                            showTransferNotification()
                            val action = IbanTransferConfirmFragmentDirections
                                .actionIbanTransferConfirmFragmentToMoneyTransferFragment(transactionCorp, transactionCustomer)
                            findNavController().navigate(action)
                        }
                    }
                    .setNegativeButton("Hayır") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }


        return binding.root
    }

    private fun showTransferNotification() {
        val channelId = "transfer_channel"
        val channelName = "Transfer Notification"
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Para transfer bildirimi"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(requireContext(), MoneyTransferFragment::class.java) // Bildirim tıklamasında yönlendirilecek aktivite
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.garanti)
            .setContentTitle("Transfer Tamamlandı")
            .setContentText("Para transferiniz gerçekleştirilmiştir.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        checkNotificationPermission()

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            try {
                with(NotificationManagerCompat.from(requireContext())) {
                    notify(1, builder.build())
                }
            } catch (e: SecurityException) {
                Toast.makeText(requireContext(), "Bildirim izni reddedildi!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Bildirim göndermek için izin vermelisiniz.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Bildirim izni verildi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Bildirim izni reddedildi", Toast.LENGTH_SHORT).show()
            }
        }
    }

}