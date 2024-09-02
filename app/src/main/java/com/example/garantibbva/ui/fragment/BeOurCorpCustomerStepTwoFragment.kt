package com.example.garantibbva.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentBeOurCorpCustomerStepTwoBinding
import com.example.garantibbva.ui.viewmodel.CorpRegistrationStepTwoViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BeOurCorpCustomerStepTwoFragment : Fragment() {
    private lateinit var binding: FragmentBeOurCorpCustomerStepTwoBinding
    private val registerViewModel: CorpRegistrationStepTwoViewModel by viewModels()
    private val calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_be_our_corp_customer_step_two, container, false)
        binding.beOurCorpCustomerStepTwoFragment = this
        val bundle: BeOurCorpCustomerStepTwoFragmentArgs by navArgs()
        val corpInfo = bundle.corp
        binding.corp = corpInfo
        binding.editTextAccountNo.setText(corpInfo.corpAccountNo)
        binding.editTextContactNo.setText(corpInfo.contactPersonsCustomerNo)

        binding.buttonSaveCorp.isEnabled = false

        binding.toolbarImage.setOnClickListener {
            Snackbar.make(it, "İşlemi iptal etmek istediğinizden emin misiniz?", Snackbar.LENGTH_SHORT)
                .setAction("Evet") {
                    registerViewModel.cancelRegistration(corpInfo.corpId.toString())
                    findNavController().navigate(R.id.action_beOurCorpCustomerStepTwoFragment_to_anasayfaFragment)
                }
                .show()
        }
        binding.checkboxAgree.setOnCheckedChangeListener { _, _ -> updateButtonState() }

        binding.editTextdateOfIncorporation.setOnClickListener {
            showDatePickerDialog()
        }

        return binding.root
    }

    private fun updateButtonState() {
        val areBothCheckBoxesChecked = binding.checkboxAgree.isChecked
        binding.buttonSaveCorp.isEnabled = areBothCheckBoxesChecked && validateForm()
    }

    private fun validateForm(): Boolean {
        val corpName = binding.editTextCorpName.text.toString().trim()
        val taxIdNumber = binding.editTextTaxNumber.text.toString().trim()
        val registrationNumber = binding.editTextRegisNumber.text.toString().trim()
        val address = binding.editTextAddress.text.toString().trim()
        val corpPhoneNumber = binding.editTextCorpPhoneNumber.text.toString().trim()
        val corpEmail = binding.editTextCorpEmailAddress.text.toString().trim()
        val contactPersonName = binding.editTextContactName.text.toString().trim()
        val contactPersonPhone = binding.editTextContactsPhoneNumber.text.toString().trim()
        val tc = binding.editTextContactsTc.text.toString().trim()
        val contactPersonEmail = binding.editTextContactsEmailAddress.text.toString().trim()
        val dateOfIncorporation = binding.editTextdateOfIncorporation.text.toString().trim()

        if (corpName.length < 4) {
            binding.editTextCorpName.error = "Şirket adı en az 4 karakter olmalıdır."
            return false
        }

        if (!tc.matches(Regex("^[1-9][0-9]{10}$")) || tc.length != 11) {
            binding.editTextContactsTc.error = "TC Kimlik Numarası 11 haneli olmalı ve başı 0 olamaz."
            return false
        }

        if (taxIdNumber.isEmpty() || !taxIdNumber.matches(Regex("^[0-9]{10}$"))) {
            binding.editTextTaxNumber.error = "Vergi Numarası 10 haneli olmalı."
            return false
        }

        if (registrationNumber.isEmpty()) {
            binding.editTextRegisNumber.error = "Tescil Numarası boş olmamalıdır."
            return false
        }

        if (address.length < 10) {
            binding.editTextAddress.error = "Adres en az 10 karakter olmalıdır."
            return false
        }

        val phonePattern = Regex("^0[0-9]{10}$") // Başlangıçta '0' ve ardından 10 rakam
        if (!phonePattern.matches(corpPhoneNumber)) {
            binding.editTextCorpPhoneNumber.error = "Telefon numarası 0 ile başlamalı ve ardından 10 haneli olmalıdır. (Örn. 0555555555)"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(corpEmail).matches()) {
            binding.editTextCorpEmailAddress.error = "Geçerli bir e-posta adresi girin."
            return false
        }

        if (contactPersonName.isEmpty()) {
            binding.editTextContactName.error = "İletişim kişisinin adı boş olmamalıdır."
            return false
        }

        if (!phonePattern.matches(contactPersonPhone)) {
            binding.editTextContactsPhoneNumber.error = "İletişim telefon numarası 0 ile başlamalı ve ardından 10 haneli olmalıdır."
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(contactPersonEmail).matches()) {
            binding.editTextContactsEmailAddress.error = "Geçerli bir iletişim e-posta adresi girin."
            return false
        }

        if (dateOfIncorporation.isEmpty()) {
            binding.editTextdateOfIncorporation.error = "Kuruluş tarihi seçilmelidir."
            return false
        }

        return true
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
        val myFormat = "dd/MM/yyyy" // Tarih formatı
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.editTextdateOfIncorporation.setText(sdf.format(calendar.time))
    }

    fun buttonSaveCorp(
        corpId: String,
        corpName: String,
        taxIdNumber: String,
        registrationNumber: String,
        address: String,
        corpPhoneNumber: String,
        corpEmail: String,
        contactPersonName: String,
        contactPersonPhone: String,
        contactPersonEmail: String,
        industry: String,
        contactPersonTc: String,
        dateOfIncorporation: String,
        numberOfEmployees: String,
        password: String
    ) {
        if (validateForm()) {
            registerViewModel.corpRegisterOtherInfos(
                corpId,
                corpName,
                taxIdNumber,
                registrationNumber,
                address,
                corpPhoneNumber,
                corpEmail,
                contactPersonName,
                contactPersonPhone,
                contactPersonEmail,
                industry,
                contactPersonTc,
                dateOfIncorporation,
                numberOfEmployees,
                password
            )
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Tebrikler, kaydınız başarıyla tamamlandı. Belirlediğiniz şifre ile şirket numaranız veya vergi numaranızı kullanarak giriş yapabilirsiniz. Parolanızı unutursanız giriş sayfasında 'Parolamı Unuttum' seçeneği ile yeni parola alabilirsiniz. Hoşgeldiniz.")
                .setPositiveButton("Tamam") { dialog, _ ->
                    findNavController().navigate(R.id.action_beOurCorpCustomerStepTwoFragment_to_anasayfaFragment)
                }
            builder.create().show()
        }
    }
}
