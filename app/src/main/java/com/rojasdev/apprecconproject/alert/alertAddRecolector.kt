package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.customSnackBar
import com.rojasdev.apprecconproject.controller.keyLIstener
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.controller.textListener
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.AlertRecolectonBinding

class alertAddRecolector(
    val onClickListener: (RecolectoresEntity) -> Unit,
    val finished: (Boolean) -> Unit
): DialogFragment() {

    private lateinit var binding: AlertRecolectonBinding
    private var insertCollector = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertRecolectonBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        buttons()

        textListener.lister(
            binding.yesAddRecolector,
            { addCollector() },
            { finish() }
        )

    val dialog = builder.create()
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    return dialog
    }

    private fun dates(view: View) {
        insertCollector = true
        val recolector = binding.yesAddRecolector.text.toString()
        val addUser = RecolectoresEntity(
            null,
            recolector,
            "active"
        )
        customSnackBar.showCustomSnackBar(view,"Recolector $recolector guardado")
        onClickListener(addUser)
    }

    private fun allUser() {
        if (insertCollector) {
            finished(true)
            dismiss()
        } else {
            dismiss()
        }
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
            allUser()
        }
    }

    private fun buttons (){
            binding.btnClose.setOnClickListener{
                finished(false)
                dismiss()
            }
    }
}