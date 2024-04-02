package com.rojasdev.apprecconprojectPro.fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rojasdev.apprecconprojectPro.ActivityLogin
import com.rojasdev.apprecconprojectPro.ActivityMainModule
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.alert.alertPhoneVerification
import com.rojasdev.apprecconprojectPro.alert.alertRefactorPassword
import com.rojasdev.apprecconprojectPro.controller.connected
import com.rojasdev.apprecconprojectPro.controller.customSnackBar
import com.rojasdev.apprecconprojectPro.controller.requireInput
import com.rojasdev.apprecconprojectPro.data.dataBase.AppDataBase
import com.rojasdev.apprecconprojectPro.data.entities.FincaEntity
import com.rojasdev.apprecconprojectPro.databinding.FragmentRegistrerUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentRegisterUser : Fragment() {
    private val fireStoreDB = FirebaseFirestore.getInstance()
    private var _binding: FragmentRegistrerUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrerUserBinding.inflate(inflater, container, false)

        requireActivity().title = "Registar Usuario"

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startActivity(Intent(requireContext(), ActivityLogin::class.java))
                }
        })

        initView()

        return binding.root
    }

    private fun initView() {
        val miList = listOf(
            binding.hitCorreo,
            binding.hitPassword,
            binding.hitUserName
        )

        binding.btnCreateAccount.setOnClickListener {
            if(requireInput.validate(miList,requireContext())){
                if(connected.isConnected(requireContext())){
                    verificationPassword()
                }else{
                    customSnackBar.showCustomSnackBar(requireView(),getString(R.string.noConnected))
                }
            }
        }
    }

    private fun insertFinca(){
        val mFinca = FincaEntity(
            null,
            binding.hitUserName.text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch{
            AppDataBase.getInstance((requireContext())).FincaDao().add(mFinca)
        }
    }

    private fun setup() {
        showProgress()
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(binding.hitCorreo.text.toString(),
                binding.hitPassword.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = it.result?.user
                    val userId = user?.uid ?: ""

                    val userData = hashMapOf(
                        "userName" to binding.hitUserName.text.toString(),
                    )

                    preferences(binding.hitCorreo.text.toString())

                    fireStoreDB.collection("/Finca").document(userId)
                        .set(userData)
                        .addOnSuccessListener {
                            insertFinca()
                            goneProgress()
                            val intent =
                                Intent(requireContext(), ActivityMainModule::class.java)
                            requireActivity().startActivity(intent)
                            requireActivity().finish()
                        }
                        .addOnFailureListener { e ->
                            goneProgress()
                            Log.w("PhoneVerification", "Error al guardar datos en FireStore", e)
                            customSnackBar.showCustomSnackBar(binding.hitCorreo,getString(R.string.error))
                        }

                }else{
                    goneProgress()
                    customSnackBar.showCustomSnackBar(requireView(),getString(R.string.error))
                }
            }
    }

    private fun verificationPassword(){
        val password = binding.hitPassword.text.toString()
        if (password.length <= 6){
            binding.hitPassword.error = getString(R.string.noWPassword)
            binding.hitPassword.requestFocus()
        }else{
            setup()
        }
    }

    private fun preferences (email:String){
        val preferences = requireContext().getSharedPreferences( "logIn", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("user","true")
        editor.putString("email",email)
        editor.apply()
    }

    fun showProgress(){
        binding.progressBar.visibility = View.VISIBLE
        binding.tvLoading.visibility = View.VISIBLE
        binding.cvWelcome.visibility = View.GONE
    }

    fun goneProgress(){
        binding.progressBar.visibility = View.GONE
        binding.tvLoading.visibility = View.GONE
        binding.cvWelcome.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}