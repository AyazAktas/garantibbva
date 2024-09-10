package com.example.garantibbva.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentBeOurPersonalCustomerStepTwoBinding
import com.example.garantibbva.ui.viewmodel.PersonalRegistrationStepTwoViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BeOurPersonalCustomerStepTwoFragment : Fragment() {
    private lateinit var binding: FragmentBeOurPersonalCustomerStepTwoBinding
    private val registerViewModel: PersonalRegistrationStepTwoViewModel by viewModels()
    private val calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_be_our_personal_customer_step_two, container, false)
        binding.beOurCustomerStepTwoFragment = this
        val bundle: BeOurPersonalCustomerStepTwoFragmentArgs by navArgs()
        val newCustomerInfo = bundle.customer
        binding.customer = newCustomerInfo
        binding.editTextCustomerNo.setText(newCustomerInfo.customerNo)

        binding.buttonSaveCustomer.isEnabled = false

        binding.toolbarImage.setOnClickListener {
            Snackbar.make(it, "İşlemi iptal etmek istediğinizden emin misiniz?", Snackbar.LENGTH_SHORT)
                .setAction("Evet") {
                    registerViewModel.cancelRegistration(newCustomerInfo.customerId.toString())
                    findNavController().navigate(R.id.action_beOurPersonalCustomerStepTwoFragment_to_beOurCustomerFragment)
                }
                .show()
        }
        binding.checkboxAgree.setOnCheckedChangeListener { _, _ -> updateButtonState() }
        binding.editTextBirthDate.setOnClickListener {
            showDatePickerDialog()
        }

        return binding.root
    }

    private fun updateButtonState() {
        val areBothCheckBoxesChecked = binding.checkboxAgree.isChecked
        binding.buttonSaveCustomer.isEnabled = areBothCheckBoxesChecked && validateForm()
    }

    private fun validateForm(): Boolean {
        val name = binding.editTextName.text.toString().trim()
        val tc = binding.editTextTC.text.toString().trim()
        val birthDate = binding.editTextBirthDate.text.toString().trim()
        val phoneNumber = binding.editTextPhoneNumber.text.toString().trim()
        val address = binding.editTextTextAddress.text.toString().trim()
        val password = binding.editTextNumberPassword.text.toString().trim()

        if (name.length < 4) {
            binding.editTextName.error = "Ad Soyad en az 4 karakter olmalıdır."
            return false
        }

        if (!tc.matches(Regex("^[1-9][0-9]{10}$")) || tc.length != 11) {
            binding.editTextTC.error = "TC Kimlik Numarası 11 haneli olmalı ve başı 0 olamaz."
            return false
        }

        if (birthDate.isEmpty()) {
            binding.editTextBirthDate.error = "Doğum tarihi seçilmelidir."
            return false
        }

        if (!isAgeAtLeast18(birthDate)) {
            binding.editTextBirthDate.error = "En az 18 yaşında olmalısınız."
            return false
        }

        val phonePattern = Regex("^0[0-9]{10}$") // Başlangıçta '0' ve ardından 10 rakam
        if (!phonePattern.matches(phoneNumber)) {
            binding.editTextPhoneNumber.error = "Telefon numarası 0 ile başlamalı ve ardından 10 haneli olmalıdır. (Örn. 0555555555)"
            return false
        }

        if (address.length < 10) {
            binding.editTextTextAddress.error = "Adres en az 10 karakter olmalıdır."
            return false
        }

        if (password.length != 6) {
            binding.editTextNumberPassword.error = "Şifre 6 haneli olmalıdır."
            return false
        }

        return true
    }

    private fun isAgeAtLeast18(birthDate: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val dob = sdf.parse(birthDate)
        val today = Calendar.getInstance()
        val birthDateCalendar = Calendar.getInstance().apply { time = dob }

        var age = today.get(Calendar.YEAR) - birthDateCalendar.get(Calendar.YEAR)
        if (birthDateCalendar.get(Calendar.MONTH) > today.get(Calendar.MONTH) ||
            (birthDateCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    birthDateCalendar.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))
        ) {
            age--
        }

        return age >= 18
    }

    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.editTextBirthDate.setText(sdf.format(calendar.time))
    }

    fun buttonSaveCustomer(customerId: String, customerName: String, customerTc: String, customerBirthDate: String, customerPhoneNumber: String, customerPassword: String, accountLocation: String) {
        if (validateForm()) {
            registerViewModel.customerRegisterOtherInfos(customerId, customerName, customerTc, customerBirthDate, customerPhoneNumber, customerPassword, accountLocation)
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Tebrikler, kaydınız başarıyla tamamlandı. Belirlediğiniz şifre ile müşteri numaranız veya TC Kimlik numaranızı kullanarak giriş yapabilirsiniz. Parolanızı unutursanız giriş sayfasında 'Parolamı Unuttum' seçeneği ile yeni parola alabilirsiniz. Hoşgeldiniz.")
                .setPositiveButton("Tamam") { dialog, id ->
                    findNavController().navigate(R.id.action_beOurPersonalCustomerStepTwoFragment_to_anasayfaFragment)
                }
            builder.create().show()
        }
    }

}
