package com.rojasdev.apprecconprojectPro.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconprojectPro.ActivityMainModule
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.controller.connected
import com.rojasdev.apprecconprojectPro.controller.customSnackBar
import com.rojasdev.apprecconprojectPro.controller.keyLIstener
import com.rojasdev.apprecconprojectPro.controller.requireInput
import com.rojasdev.apprecconprojectPro.controller.textListener
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.databinding.AlertRecolectonBinding


class alertAddLotes(
    val finca: Int,
    val onClickListener: (LoteEntity) -> Unit,
    val finishAlert: () -> Unit
): DialogFragment() {

    private lateinit var binding: AlertRecolectonBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertRecolectonBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        textListener.lister(
            binding.yesAddRecolector,
            { addLote() },
            { finish() }
        )

        if (!connected.isConnected(requireContext())){
            alertMessage(
                "para podder realizar modificaciones en los datos principales deve de tener internet",
                "comprueba la coneccion e intenta nuevamente",
                "cerrar",
                "listo",
                "ups, no tienes coneccion"
            ){
                if (it == "yes"){
                    startActivity(Intent(requireContext(), ActivityMainModule::class.java))
                }else{
                    startActivity(Intent(requireContext(), ActivityMainModule::class.java))
                }
            }.show(parentFragmentManager,"dialog")
        }

        binding.btnClose.setOnClickListener{
            dismiss()
        }

        binding.tilInputAddType.visibility = View.VISIBLE

        initView()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun dates(view: View) {
        val lote = binding.yesAddRecolector.text.toString()
        val type = binding.type.text.toString()
        val addLote = LoteEntity(
            null,
            lote,
            type,
            false,
            finca
            )
        customSnackBar.showCustomSnackBar(view,"Lote $lote guardado")
        onClickListener(addLote)
    }

    private fun addLote(){
        binding.btAddRecolector.text = getString(R.string.btnAddLote)
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
                binding.type.setText("")
            }
        }
    }
    private fun finish(){
        binding.btAddRecolector.text = getString(R.string.finish)
        binding.btAddRecolector.setOnClickListener {
            dismiss()
            finishAlert()
        }
    }

    private fun initView(){
        binding.view.visibility = View.GONE
        binding.switchDates.visibility = View.GONE
        binding.tvDescription.text = getString(R.string.addLote)
        binding.tilInputAdd.setStartIconDrawable(R.drawable.ic_lote)
    }
}