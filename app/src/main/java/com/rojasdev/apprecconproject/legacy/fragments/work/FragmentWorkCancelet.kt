package com.rojasdev.apprecconproject.legacy.fragments.work

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.FragmentCollectorsAndCollecionBinding
import com.rojasdev.apprecconproject.legacy.ActivityMainModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentWorkCancelet(
    var scroll:(String)-> Unit,
    var preferences:()-> Unit
) : androidx.fragment.app.Fragment() {

    private var _binding: FragmentCollectorsAndCollecionBinding? = null
    private lateinit var adapter: com.rojasdev.apprecconproject.legacy.adapters.adapterRvWorkTotal
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorsAndCollecionBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startActivity(Intent(requireContext(), ActivityMainModule::class.java))
                }
            })

        contextTheme()

        totalCollectionCollector()

        com.rojasdev.apprecconproject.legacy.controller.scrolling.scrolling(binding.rvCollectors) {
            scroll(it)
        }

        getTotalCollection()
        return binding.root
    }

    private fun totalCollectionCollector(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance((requireContext())).RecolectoresDao().getIDManWork()
            launch(Dispatchers.Main) {
                val collector = mutableListOf<com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector>()
                for(item in idCollectors){
                    val collectionTotal = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance((requireContext())).RecolectoresDao().getManAndWorkTotal(item.toInt())
                    if(collectionTotal[0].name_recolector != null){
                        collector.add(collectionTotal[0])
                    }
                }
                dates(collector)
            }
        }
    }

    private fun dates(total:List<com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector>) {
        adapter =
            com.rojasdev.apprecconproject.legacy.adapters.adapterRvWorkTotal(
                total,
                getColor()
            ) {
                initCancelCollection(it)
            }
        binding.rvCollectors.adapter = adapter
        binding.rvCollectors.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initCancelCollection(collectionTotal: com.rojasdev.apprecconproject.legacy.data.dataModel.workTotalCollector) {
        CoroutineScope(Dispatchers.IO).launch{
            val collection = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance((requireContext())).RecolectoresDao().getMenAndWork("active",collectionTotal.PK_ID_Recolector)

            launch(Dispatchers.Main) {
                com.rojasdev.apprecconproject.legacy.alert.work.alertCancelWork(
                    listOf(collectionTotal),
                    collection
                ) {
                    updateCollection(it)
                }.show(parentFragmentManager,"dialog")
            }
        }
    }

    private fun updateCollection(idUpdate: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val dataBase = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(requireContext())

            dataBase.RecolectoresDao().updateCollectorState(idUpdate)
            dataBase.WorkDao().updateWorkState(idUpdate)

            launch(Dispatchers.Main) {
                com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(requireView(),getString(R.string.collectionCanceled))
                getTotalCollection()
                totalCollectionCollector()
                preferencesUpdate()
            }
        }
    }

    private fun preferencesUpdate(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance((requireContext())).WorkDao().getFkIdCollectors()
            launch(Dispatchers.Main) {
                if(idCollectors.isEmpty()){
                    com.rojasdev.apprecconproject.legacy.alert.messagin.alertMessage(
                        getString(R.string.txtNewReport),
                        getString(R.string.txtCalendar),
                        getString(R.string.txtGoReport),
                        getString(R.string.txtReturnMenu),
                        getString(R.string.txtWorkFull)
                    ) {
                        if (it == "yes") {
                            preferences()
                            startActivity(
                                Intent(
                                    requireContext(),
                                    com.rojasdev.apprecconproject.legacy.ActivityInformes::class.java
                                )
                            )
                        } else {
                            preferences()
                            startActivity(Intent(requireContext(), ActivityMainModule::class.java))
                        }
                    }.show(parentFragmentManager,"dialog")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalCollection(){
        CoroutineScope(Dispatchers.IO).launch{
            val collectionTotal = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance((requireContext())).SettingDao().getTotalWorkActive()
            launch(Dispatchers.Main) {
                if(collectionTotal.isNotEmpty()){
                    binding.lyTotal.visibility = View.VISIBLE
                    binding.tvCollection.text = "Jornales trabajados\n ${collectionTotal[0].cantidad.toInt()}"

                    com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(collectionTotal[0].total.toInt()){
                        binding.tvTotal.text = "Total a pagar\n $it"
                    }
                }
            }
        }
    }
    private fun contextTheme() {
        binding.lyTotal.backgroundTintList = ColorStateList.valueOf(getColor())
    }

    private fun getColor():Int{
        var color: Int? = null
        com.rojasdev.apprecconproject.legacy.controller.controllerTheme.main(requireContext(),
            day = {
                color = ContextCompat.getColor(requireContext(), R.color.Orange)
            },
            night = {
                color = ContextCompat.getColor(requireContext(), R.color.OrangeDark)
            })
        return color!!
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}