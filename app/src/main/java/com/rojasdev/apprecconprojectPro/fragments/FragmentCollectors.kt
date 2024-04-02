package com.rojasdev.apprecconprojectPro.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rojasdev.apprecconprojectPro.databinding.FragmentCollectorsAndCollecionBinding
import com.rojasdev.apprecconprojectPro.ActivityMainModule
import com.rojasdev.apprecconprojectPro.ActivityRecolection
import com.rojasdev.apprecconprojectPro.ActivityRecolectionDetail
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.adapters.adapterRvCollectors
import com.rojasdev.apprecconprojectPro.alert.alertCollection
import com.rojasdev.apprecconprojectPro.alert.alertDeleteCollector
import com.rojasdev.apprecconprojectPro.alert.alertMessage
import com.rojasdev.apprecconprojectPro.controller.customSnackBar
import com.rojasdev.apprecconprojectPro.controller.scrolling
import com.rojasdev.apprecconprojectPro.data.dataBase.AppDataBase
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecollectionEntity
import com.rojasdev.apprecconprojectPro.services.serviceCollector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentCollectors(
    var scroll:(String)-> Unit,
    var preferences:()-> Unit
) : Fragment() {
    private val mAuth = FirebaseAuth.getInstance()
    private val fireStoreDB = FirebaseFirestore.getInstance()
    private lateinit var adapter: adapterRvCollectors
    private var _binding: FragmentCollectorsAndCollecionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorsAndCollecionBinding.inflate(inflater,container,false)

        binding.lyTotal.visibility = View.GONE

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                startActivity(Intent(requireContext(),ActivityMainModule::class.java))
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            dates()
        }

            scrolling.scrolling(binding.rvCollectors){
            scroll(it)
        }

        return binding.root
    }

    private suspend fun dates(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = AppDataBase.getInstance((requireContext())).RecollectionDao().getFkIdCollectors()
            val collectors = AppDataBase.getInstance(requireContext()).RecolectoresDao().getAllRecolector()
            val lotes = AppDataBase.getInstance(requireContext()).LoteDao().get()
            launch(Dispatchers.Main) {
                if (collectors.isNotEmpty()){
                    initRv(idCollectors,collectors,lotes)
                }else{
                    preferencesUpdate()
                }
            }
        }
    }

    private fun initRv(idCollectors: List<Long>, collectors: List<RecolectoresEntity>, lotes: List<LoteEntity>) {
        adapter = adapterRvCollectors(
            collectors,
            idCollectors,
            { item ->
                initDetailCollector(item) // Next Activity
            },
            {
                initAlertDelete(it) // Delete
            },
            {
                initAlertAddCollection(it,lotes) // Add collection
            }
        )

        binding.rvCollectors.adapter = adapter
        binding.rvCollectors.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initDetailCollector(item: RecolectoresEntity) {
        startActivity(Intent(
            requireContext(), ActivityRecolectionDetail::class.java
        ).putExtra("userId", item.id).putExtra("userName", item.name))
    }

    private fun initAlertDelete(it: RecolectoresEntity) {
        alertDeleteCollector(it.name){
            customSnackBar.showCustomSnackBar(requireView(),getString(R.string.deleteCollector))

            CoroutineScope(Dispatchers.IO).launch {
                AppDataBase.getInstance(requireContext()).RecolectoresDao().deleteCollectorId(it.id!!)
                launch {
                    serviceCollector.deleteCollector(fireStoreDB,it.id!!)!!.addOnSuccessListener {
                        goneProgress()
                        startActivity(Intent(requireContext(), ActivityRecolection::class.java))
                    }
                    dates() }
            }

        }.show(parentFragmentManager,"dialog")
    }

    private fun initAlertAddCollection(it: RecolectoresEntity, lotes: List<LoteEntity>) {
        alertCollection(
            it,
            lotes,
            {
                insertCollection(it)
            },
            {
                if (it){
                    insertColletionFirebase()
                }
            }
            ).show(parentFragmentManager,"dialog")
    }

    private fun insertCollection(recollection: RecollectionEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(requireContext()).RecollectionDao().addRecollection(recollection)
            launch(Dispatchers.Main) {
                customSnackBar.showCustomSnackBar(binding.fragmentCollectors,getString(R.string.addCollectionFinish))
            }
        }
    }

    private fun preferencesUpdate(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = AppDataBase.getInstance((requireContext())).RecollectionDao().getFkIdCollectors()
            launch(Dispatchers.Main) {
                if(idCollectors.isEmpty()){
                    alertMessage(
                        getString(R.string.txtMessageOne),
                        getString(R.string.txtMessageTwo),
                        getString(R.string.txtRecolectionStart),
                        getString(R.string.btnFinish),
                        getString(R.string.requireCollectors)
                    ){
                        if(it == "yes"){
                            preferences()
                            startActivity(Intent(requireContext(),ActivityMainModule::class.java))
                        }else{
                            preferences()
                            startActivity(Intent(requireContext(),ActivityMainModule::class.java))
                        }
                    }.show(parentFragmentManager,"dialog")
                }
            }
        }
    }

    private fun insertColletionFirebase() {
        showProgress()
        CoroutineScope(Dispatchers.IO).launch {
            val collection = AppDataBase.getInstance(requireContext()).RecollectionDao().getAllCollectionFirebase(false)
            launch(Dispatchers.Main) {
                for (itCollection in collection){
                    val userId = mAuth.currentUser!!.uid

                    val userData = hashMapOf(
                        "id" to  itCollection.ID,
                        "cantidad" to itCollection.total,
                        "fecha" to itCollection.date,
                        "state" to itCollection.state,
                        "recolector" to itCollection.collector,
                        "configuracion" to itCollection.setting,
                        "lote" to itCollection.lote,
                        "firebase" to true
                    )

                    fireStoreDB.collection("/Finca/$userId/Recoleccion").document()
                        .set(userData)
                        .addOnSuccessListener {
                            goneProgress()
                            startActivity(Intent(requireContext(), ActivityRecolection::class.java))
                            updateStateCollection(itCollection.ID!!)
                        }
                }
            }
        }
    }

    private fun updateStateCollection(id:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(requireContext()).RecollectionDao().updateCollectionStateFirebase(id,true)
            launch(Dispatchers.Main) {
                customSnackBar.showCustomSnackBar(binding.fragmentCollectors,"lista la copia de seguridad")
                dates()
            }
        }
    }

    fun showProgress(){
        binding.lyTotal.visibility = View.GONE
        binding.rvCollectors.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.tvLoading.visibility = View.VISIBLE
    }

    fun goneProgress(){
        binding.rvCollectors.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.tvLoading.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}