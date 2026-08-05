package com.rojasdev.apprecconproject.legacy.alert.collection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.legacy.controller.adsBanner
import com.rojasdev.apprecconproject.legacy.controller.animatedAlert
import com.rojasdev.apprecconproject.legacy.controller.controllerTheme
import com.rojasdev.apprecconproject.legacy.controller.customSnackBar
import com.rojasdev.apprecconproject.legacy.controller.keyLIstener
import com.rojasdev.apprecconproject.legacy.controller.requireInput
import com.rojasdev.apprecconproject.legacy.controller.textListener
import com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.AlertRecolectonBinding

class alertAddRecolector(
    var style : Boolean,
    val onClickListener: (RecolectoresEntity) -> Unit,
    val finished: (Boolean) -> Unit
): DialogFragment() {

    private lateinit var binding: AlertRecolectonBinding
    private var insertCollector = false

    @SuppressLint("ResourceAsColor")
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

        contextTheme()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun contextTheme() {
        var color: Int? = null
        controllerTheme.main(requireContext(),
            day = {
                color = ContextCompat.getColor(requireContext(), R.color.Orange)
            },
            night = {
                color = ContextCompat.getColor(requireContext(), R.color.OrangeDark)
            })

        if (style == true){
            binding.btAddRecolector.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.tvDescription.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.btnClose.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.btnClose.invalidate()

            binding.tilInputAdd.boxStrokeColor = color!!
            binding.tilInputAdd.hintTextColor = ColorStateList.valueOf(color!!)
        }else{
            controllerTheme.main(
                requireContext(),
                day = {
                    color = ContextCompat.getColor(requireContext(), R.color.Thunderbird)
                },
                night = {
                    color = ContextCompat.getColor(requireContext(), R.color.Dark_Tan)
                }
            )
            binding.btnClose.backgroundTintList = ColorStateList.valueOf(color!!)
            binding.btnClose.invalidate()
        }
    }

    private fun dates(view: View) {
        insertCollector = true
        val recolector = binding.yesAddRecolector.text.toString()
        val addUser =
            RecolectoresEntity(
                null,
                recolector,
                "active"
            )
        customSnackBar.showCustomSnackBar(view,"Trabajador $recolector guardado")
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
