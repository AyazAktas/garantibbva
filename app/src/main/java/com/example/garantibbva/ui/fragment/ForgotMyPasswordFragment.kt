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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.app.NotificationCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.fragment.findNavController
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentForgotMyPasswordBinding
import com.example.garantibbva.ui.viewmodel.ForgotMyPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotMyPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotMyPasswordBinding
    private val passwordViewModel: ForgotMyPasswordViewModel by viewModels()

    private var generatedCode: String? = null
    private lateinit var editTextSMSCode: EditText
    private lateinit var buttonConfirmSMS: Button
    private lateinit var editTextNewPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonSubmitNewPassword: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_my_password, container, false)
        binding.forgotMyPasswordFragment = this

        editTextSMSCode = binding.editTextSMSCode
        buttonConfirmSMS = binding.buttonVerifySMS
        editTextNewPassword = binding.editTextNewPassword
        editTextConfirmPassword = binding.editTextConfirmPassword
        buttonSubmitNewPassword = binding.buttonSubmitNewPassword

        binding.toolbarImageStep1.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_forgotMyPasswordFragment_to_anasayfaFragment)
        }
        binding.toolbarImageStep2.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_forgotMyPasswordFragment_to_anasayfaFragment)
        }
        binding.toolbarImageStep3.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_forgotMyPasswordFragment_to_anasayfaFragment)
        }


        setupListeners()
        validateInputs()
        checkNotificationPermission()
        return binding.root
    }

    private fun setupListeners() {
        binding.editTextCustomerNumber.addTextChangedListener { validateInputs() }
        binding.editTextTC.addTextChangedListener { validateInputs() }
        binding.checkBoxInfo.setOnCheckedChangeListener { _, _ -> validateInputs() }
        binding.buttonStep1.setOnClickListener { onButtonClicked() }

        editTextSMSCode.addTextChangedListener { text ->
            val enteredCode = text.toString()
            if (enteredCode == generatedCode) {
                buttonConfirmSMS.isEnabled = true
            } else if (enteredCode.length == 6) {
                Toast.makeText(context, "Yanlış kod girildi!", Toast.LENGTH_SHORT).show()
                buttonConfirmSMS.isEnabled = false
            }
        }

        buttonConfirmSMS.setOnClickListener {
            showStep3()
        }

        editTextNewPassword.addTextChangedListener { validatePasswords() }
        editTextConfirmPassword.addTextChangedListener { validatePasswords() }

        buttonSubmitNewPassword.setOnClickListener {
            handlePasswordReset()
        }
    }

    private fun validateInputs() {
        val tcFilled = binding.editTextTC.text.isNotEmpty()
        val numberFilled = binding.editTextCustomerNumber.text.isNotEmpty()
        val isChecked = binding.checkBoxInfo.isChecked
        binding.buttonStep1.isEnabled = tcFilled && numberFilled && isChecked
    }

    private fun validatePasswords() {
        val newPassword = editTextNewPassword.text.toString()
        val confirmPassword = editTextConfirmPassword.text.toString()
        buttonSubmitNewPassword.isEnabled = newPassword == confirmPassword && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    private fun onButtonClicked() {
        val tc = binding.editTextTC.text.toString()
        val customerNumber = binding.editTextCustomerNumber.text.toString()

        viewLifecycleOwner.lifecycleScope.launch {
            val customer = passwordViewModel.passwordValidation(customerNumber, tc)
            if (customer != null) {
                saveCustomerId(customer.customerId.toString())  // customer.id yerine doğru ID alanını kullan
                Toast.makeText(context, "Sms Doğrulama aşamasına geçiliyor", Toast.LENGTH_SHORT).show()
                showStep2()
                generatedCode = generateVerificationCode()
                context?.let { sendNotification(it, generatedCode!!) }
            } else {
                Toast.makeText(context, "Geçersiz TC veya Müşteri Numarası", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showStep2() {
        binding.step1Layout.visibility = View.GONE
        binding.step2Layout.visibility = View.VISIBLE
        buttonConfirmSMS.isEnabled = false
    }

    private fun showStep3() {
        binding.step2Layout.visibility = View.GONE
        binding.step3Layout.visibility = View.VISIBLE
    }

    private fun generateVerificationCode(): String {
        val random = java.util.Random()
        val code = String.format("%06d", random.nextInt(1000000))
        return code
    }

    private fun sendNotification(context: Context, code: String) {
        val channelId = "password_reset_channel"
        val channelName = "Password Reset"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Channel for password reset notifications"
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, ForgotMyPasswordFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.garanti)
            .setContentTitle("Doğrulama Kodu")
            .setContentText("Doğrulama kodunuz: $code")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    private fun saveCustomerId(customerId: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("CUSTOMER_ID", customerId)
            apply()
        }
    }

    private fun getCustomerId(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("CUSTOMER_ID", null)
    }

    private fun handlePasswordReset() {
        val customerId = getCustomerId()
        val newPassword = editTextNewPassword.text.toString()

        if (customerId != null && validateFormPassword()) {
            viewLifecycleOwner.lifecycleScope.launch {
                passwordViewModel.updatePassword(customerId, newPassword)
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Parolanız başarıyla değiştirildi. Artık yeni parolanız ile giriş yapabilirsiniz.")
                    .setPositiveButton("Tamam") { dialog, id ->
                        findNavController().navigate(R.id.action_forgotMyPasswordFragment_to_anasayfaFragment)
                    }
                builder.create().show()
            }
        } else {
            Toast.makeText(context, "Parola güncellenemedi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateFormPassword(): Boolean {
        val newPassword = editTextNewPassword.text.toString()
        val confirmPassword = editTextConfirmPassword.text.toString()
        return newPassword == confirmPassword && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Bildirim izni verildi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Bildirim izni reddedildi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
