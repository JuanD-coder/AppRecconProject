package com.rojasdev.apprecconprojectPro.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.IntentSender.OnFinished
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.databinding.AlertRecolectonBinding
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.controller.connected
import com.rojasdev.apprecconprojectPro.controller.customSnackBar
import com.rojasdev.apprecconprojectPro.controller.keyLIstener
import com.rojasdev.apprecconprojectPro.controller.requireInput
import com.rojasdev.apprecconprojectPro.controller.textListener
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity

class alertAddRecolector(
    val onClickListener: (RecolectoresEntity) -> Unit,
    val finished: (Boolean) -> Unit
): DialogFragment() {
    var firebase: Boolean = true
    private lateinit var binding: AlertRecolectonBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertRecolectonBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

       binding.switchDates.setOnCheckedChangeListener { buttonView, isChecked ->
           firebase = if (isChecked){
               true
           }else{
               false
           }
       }

        textListener.lister(
            binding.yesAddRecolector,
            { addCollector() },
            { finish() }
        )

        binding.btnClose.setOnClickListener{
            dismiss()
        }

        if (connected.isConnected(requireContext())){
            binding.switchDates.isChecked = true
        }else{
            binding.switchDates.isChecked = false
            binding.switchDates.visibility = View.GONE
            binding.view.visibility = View.GONE
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun dates(view: View) {
        val recolector = binding.yesAddRecolector.text.toString()
        val addUser = RecolectoresEntity(
            null,
            recolector,
            "active",
            false
        )
        customSnackBar.showCustomSnackBar(view,"Recolector $recolector guardado")
        onClickListener(addUser)
    }

    private fun addCollector(){
        binding.btAddRecolector.text = getString(R.string.btnAddRecolector)
        val myListInput = listOf(
            binding.yesAddRecolector
        )

        keyLIstener.start(binding.yesAddRecolector){
            val required = requireInput.validate(myListInput,requireContext())
            if (required){
                dates(binding.yesAddRecolector)
                binding.yesAddRecolector.setText("")
            }
        }

        binding.btAddRecolector.setOnClickListener {
            val required = requireInput.validate(myListInput,requireContext())
            if (required){
                dates(it)
                binding.yesAddRecolector.setText("")
            }
        }
    }
    private fun finish(){
        binding.btAddRecolector.text = getString(R.string.finish)
        binding.btAddRecolector.setOnClickListener {
            dismiss()
            finished(firebase)
        }
    }

}