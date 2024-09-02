package com.example.garantibbva.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentBeOurCustomerCorpBinding
import com.example.garantibbva.ui.viewmodel.CorpCustomerRegisterView
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BeOurCustomerCorpFragment : Fragment() {
    private lateinit var binding: FragmentBeOurCustomerCorpBinding
    private val registerViewModel: CorpCustomerRegisterView by viewModels()
    private val selectedAccountTypes = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_be_our_customer_corp, container, false)
        binding.beOurCorpCustomerFragment = this
        binding.checkBoxPrivacy.setOnCheckedChangeListener { _, _ -> updateButtonState() }
        binding.checkBoxInfoLetter.setOnCheckedChangeListener { _, _ -> updateButtonState() }
        binding.chipGroupAccountType.setOnCheckedStateChangeListener { _, checkedChipIds -> updateSelectedChips(checkedChipIds) }
        return binding.root
    }

    private fun updateButtonState() {
        val isAnyChipChecked = binding.chipGroupAccountType.checkedChipIds.isNotEmpty()
        val areBothCheckBoxesChecked = binding.checkBoxPrivacy.isChecked && binding.checkBoxInfoLetter.isChecked
        binding.buttonStartRegister.isEnabled = isAnyChipChecked && areBothCheckBoxesChecked
    }

    private fun updateSelectedChips(checkedChipIds: List<Int>) {
        selectedAccountTypes.clear()
        checkedChipIds.forEach { chipId ->
            val chip = binding.chipGroupAccountType.findViewById<Chip>(chipId)
            chip?.let {
                selectedAccountTypes.add(it.text.toString())
            }
        }
    }

    fun returnBeOurCustomerPage(it: View) {
        Navigation.findNavController(it).navigate(R.id.action_beOurCustomerCorpFragment_to_anasayfaFragment)
    }

    fun register() {
        if (selectedAccountTypes.isEmpty()) {
            Snackbar.make(binding.root, "Lütfen bir hesap türü seçin", Snackbar.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val selectedAccountType = selectedAccountTypes.toString()
            try {
                val corp = registerViewModel.corpCustomerRegister(
                    corpName = "",
                    taxIdNumber = "",
                    registrationNumber = "",
                    address = "",
                    corpPhoneNumber = "",
                    corpEmail = "",
                    contactPersonName = "",
                    contactPersonPhone = "",
                    contactPersonEmail = "",
                    industry = "",
                    contactPersonTc = "",
                    accountType = selectedAccountType,
                    dateOfIncorporation = "",
                    annualRevenue = 0.0,
                    numberOfEmployees = "",
                    accountBalance = 0.0,
                    accountPurpose = "Kurumsal",
                    password = ""
                )
                Snackbar.make(binding.root, "Kayıt işlemi başarıyla başladı.", Snackbar.LENGTH_SHORT).show()
                val action = BeOurCustomerCorpFragmentDirections.actionBeOurCustomerCorpFragmentToBeOurCorpCustomerStepTwoFragment(corp = corp)
                findNavController().navigate(action)
            } catch (e: Exception) {
                Log.e("hata", "Kayıt sırasında bir hata oluştu: ${e.message}")
            }
        }
    }
}
