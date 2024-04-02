package com.rojasdev.apprecconprojectPro.alert

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.rojasdev.apprecconprojectPro.ActivityMainModule
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.databinding.AlertPhoneVerificationBinding
import java.util.concurrent.TimeUnit

class   alertPhoneVerification(
    private var userName: String,
    private var phoneNumber: String,
    private var password: String
): DialogFragment() {

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storedVerificationId: String

    private lateinit var binding: AlertPhoneVerificationBinding

    private val fireStoreDB = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertPhoneVerificationBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        firebaseAuth = Firebase.auth

        animatedAlert.animatedInit(binding.cvVerificationSMS)
        registerUser(phoneNumber)

        binding.tvTitleVerification.text = "$phoneNumber"
        binding.tvInfoSMS.text = "${getString(R.string.tvContentInfo)} $phoneNumber"

        val verificationCode = binding.editTextNumber.text

        binding.btnVerification.setOnClickListener {
           if (verificationCode.isNotEmpty()){
               val credential = PhoneAuthProvider.getCredential(storedVerificationId, verificationCode.toString())
               signInWithPhoneAuthCredential(credential)
           } else {
               Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
           }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.requireDates))
        return dialog
    }

    private fun preferences (credential: String){
        val preferences = requireContext().getSharedPreferences( "credential", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("credential",credential)
        editor.apply()
    }
    private fun registerUser(phoneNumber: String) {

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                preferences(credential.toString())
                signInWithPhoneAuthCredential(credential) // Inicio de sesion
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)

                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        // Invalid request
                        Toast.makeText(requireContext(), "Número de teléfono no válido", Toast.LENGTH_SHORT).show()
                    }

                    is FirebaseTooManyRequestsException -> {
                        // The SMS quota for the project has been exceeded
                        Toast.makeText(requireContext(), "Número de teléfono ya registrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                Log.d(TAG, "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        firebaseAuth.setLanguageCode("es")
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    val userId = user?.uid ?: ""

                    val userData = hashMapOf(
                        "userName" to userName,
                    )

                    fireStoreDB.collection("/User").document(userId)
                        .set(userData)
                        .addOnSuccessListener {
                            Log.d("PhoneVerification", "Datos guardados en FireStore con ID: $userId")
                            Toast.makeText(requireContext(), "Datos Guardado en BD", Toast.LENGTH_SHORT).show()

                            val intent = Intent(requireContext(), ActivityMainModule::class.java)
                            requireActivity().startActivity(intent)
                            requireActivity().finish()
                        }
                        .addOnFailureListener {e ->
                            Log.w("PhoneVerification", "Error al guardar datos en FireStore", e)
                            Toast.makeText(requireContext(), "No se Save DB", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(requireContext(), "Error en el registro: ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }

}