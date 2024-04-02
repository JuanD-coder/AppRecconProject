package com.rojasdev.apprecconprojectPro.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.controller.customSnackBar
import com.rojasdev.apprecconprojectPro.data.entities.FincaEntity
import com.rojasdev.apprecconprojectPro.databinding.AlertPerfilBinding

class alertDatesFinca(
    var nameFinca: List<FincaEntity>,
    var setOnClickListener: (FincaEntity) -> Unit
): DialogFragment() {
    private val fireStoreDB = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var binding: AlertPerfilBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertPerfilBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvWelcome)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val preferences = requireContext().getSharedPreferences( "logIn", Context.MODE_PRIVATE)
        val register = preferences.getString("email","")

        binding.tvNameFinca.text = nameFinca[0].name
        binding.tvEmail.text = register
        setup()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun setup() {

        binding.tvNameFinca.setOnClickListener {
            showProgress()
            val uid = mAuth.currentUser?.uid
            alertUpdateFinca(nameFinca[0].name){ newNameFinca->
                if (newNameFinca.equals(null)){
                    goneProgress()
                }else{
                    fireStoreDB.collection("/User").document(uid.toString())
                        .update("userName",newNameFinca)
                        .addOnCompleteListener{
                            val newFinca = FincaEntity(
                                nameFinca[0].id,
                                newNameFinca!!
                            )
                            goneProgress()
                            setOnClickListener(newFinca)
                            dismiss()
                        }
                        .addOnFailureListener{e ->
                            goneProgress()
                            Toast.makeText(requireContext(),e.toString() , Toast.LENGTH_SHORT).show()
                        }
                }
            }.show(parentFragmentManager,"dialog")
        }

        binding.cvPassword.setOnClickListener {
            alertRefactorPassword{
                showProgress()
                mAuth.setLanguageCode("es")
                mAuth.sendPasswordResetEmail(it).addOnCompleteListener {task->
                    if (task.isSuccessful){
                        goneProgress()
                        customSnackBar.showCustomSnackBar(binding.textView13,"en un momento te llegara un correo electronico")
                    }else{
                        goneProgress()
                        customSnackBar.showCustomSnackBar(binding.textView13,"pailas")
                    }
                }
            }.show(parentFragmentManager,"dialog")
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    fun showProgress(){
        binding.progressBar.visibility = View.VISIBLE
        binding.tvLoading.visibility = View.VISIBLE
        binding.cvWelcome.visibility = View.GONE
        binding.btnClose.visibility = View.GONE
    }

    fun goneProgress(){
        binding.progressBar.visibility = View.GONE
        binding.tvLoading.visibility = View.GONE
        binding.cvWelcome.visibility = View.VISIBLE
        binding.btnClose.visibility = View.VISIBLE
    }
}
