package com.rojasdev.apprecconproject.legacy.fragments.work

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.FragmentCollectorsAndCollecionBinding
import com.rojasdev.apprecconproject.legacy.ActivityMainModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentWork(
    var scroll:(String)-> Unit,
    var preferences:()-> Unit
) : androidx.fragment.app.Fragment() {

    private lateinit var adapter: com.rojasdev.apprecconproject.legacy.adapters.adapterRvCollectors
    private var _binding: FragmentCollectorsAndCollecionBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorsAndCollecionBinding.inflate(inflater,container,false)

        binding.lyTotal.visibility = View.GONE

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                startActivity(Intent(requireContext(), ActivityMainModule::class.java))
            }
        })


        CoroutineScope(Dispatchers.IO).launch {
            dates()
        }

        com.rojasdev.apprecconproject.legacy.controller.scrolling.scrolling(binding.rvCollectors){
            scroll(it)
        }

        return binding.root
    }

    private suspend fun dates(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance((requireContext())).WorkDao().getFkIdCollectors()
            val collectors = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(requireContext()).RecolectoresDao().getAllWorkMen()

            launch(Dispatchers.Main) {
                if (collectors.isNotEmpty()){
                    initRv(idCollectors,collectors)
                }else{
                    preferencesUpdate()
                }
            }
        }
    }

    private fun initRv(idCollectors: List<Long>, collectors: List<com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity>) {
        adapter =
            com.rojasdev.apprecconproject.legacy.adapters.adapterRvCollectors(
                collectors,
                idCollectors,
                { item ->
                    initDetailCollector(item) // Next Activity
                },
                {
                    initAlertDelete(it) // Delete
                },
                {
                    initAlertAddCollection(it) // Add collection
                }
            )

        binding.rvCollectors.adapter = adapter
        binding.rvCollectors.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initDetailCollector(item: com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) {
        startActivity(
            Intent(
            requireContext(), com.rojasdev.apprecconproject.legacy.ActivityDetalleWork::class.java
        ).putExtra("userId", item.id).putExtra("userName", item.name))
    }

    private fun initAlertDelete(it: com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) {
        com.rojasdev.apprecconproject.legacy.alert.collection.alertDeleteCollector(
            it.name,
            true
        ) {
            com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(
                requireView(),
                getString(R.string.deleteCollector)
            )

            CoroutineScope(Dispatchers.IO).launch {
                com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(
                    requireContext()
                ).RecolectoresDao().deleteCollectorId(it.id!!)
                launch { dates() }
            }

        }.show(parentFragmentManager,"dialog")
    }

    private fun initAlertAddCollection(it: com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val prices = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(requireContext()).SettingDao().getPriceWorkState("active")
            launch {
                com.rojasdev.apprecconproject.legacy.alert.work.alerAddWork(
                    it,
                    prices
                ) {
                    insertCollection(it)
                }.show(parentFragmentManager,"dialog")
            }
        }

    }

    private fun insertCollection(work: com.rojasdev.apprecconproject.legacy.data.entities.WorkEntity) {
        com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(binding.fragmentCollectors,getString(R.string.addWorkFinish))
        CoroutineScope(Dispatchers.IO).launch {
            com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(requireContext()).WorkDao().insert(work)
            launch { dates() }
        }
    }

    private fun preferencesUpdate(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance((requireContext())).RecollectionDao().getFkIdCollectors()
            launch(Dispatchers.Main) {
                if(idCollectors.isEmpty()){
                    com.rojasdev.apprecconproject.legacy.alert.messagin.alertMessage(
                        getString(R.string.txtMessageOne),
                        getString(R.string.txtMessageTwo),
                        getString(R.string.txtRecolectionStart),
                        getString(R.string.btnFinish),
                        getString(R.string.requireCollectors)
                    ) {
                        if (it == "yes") {
                            preferences()
                            startActivity(Intent(requireContext(), ActivityMainModule::class.java))
                        } else {
                            preferences()
                            startActivity(Intent(requireContext(), ActivityMainModule::class.java))
                        }
                    }.show(parentFragmentManager,"dialog")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}