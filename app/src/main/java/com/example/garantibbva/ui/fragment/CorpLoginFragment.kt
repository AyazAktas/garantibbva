package com.example.garantibbva.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.garantibbva.R
import com.example.garantibbva.databinding.FragmentCorpLoginBinding
import com.example.garantibbva.ui.viewmodel.CorpLoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CorpLoginFragment : Fragment() {
    private lateinit var binding: FragmentCorpLoginBinding
    private val loginViewModel:CorpLoginViewModel by viewModels()
    var enteredContactsCustomerNo:String=""
    var enteredContactsTcNo:String=""
    var enteredPassword:String=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_corp_login,container,false)
        binding.corpLoginFragment=this

        binding.buttonForgot.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_corpLoginFragment_to_forgotMyPasswordCorpFragment)
        }

        return binding.root
    }
    fun onLoginClicked(){
        viewLifecycleOwner.lifecycleScope.launch {
            val corp=loginViewModel.login(enteredContactsTcNo,enteredContactsCustomerNo,enteredPassword)
            if (corp!= null){
                Toast.makeText(context,"Giriş başarılı",Toast.LENGTH_SHORT).show()
                val action=CorpLoginFragmentDirections.actionCorpLoginFragmentToCorpPageFragment(corp)
                findNavController().navigate(action)
            }
            else{
                Snackbar.make(binding.root, "Hatalı TC, Numara veya Parola", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}