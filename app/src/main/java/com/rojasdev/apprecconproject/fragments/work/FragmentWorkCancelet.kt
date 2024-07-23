package com.rojasdev.apprecconproject.fragments.work

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityInformes
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.ActivityRecolectionDetail
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.adapterRvCollectionTotal
import com.rojasdev.apprecconproject.adapters.adapterRvCollectors
import com.rojasdev.apprecconproject.adapters.adapterRvWorkTotal
import com.rojasdev.apprecconproject.alert.collection.alertCancelCollection
import com.rojasdev.apprecconproject.alert.collection.alertCollection
import com.rojasdev.apprecconproject.alert.collection.alertDeleteCollector
import com.rojasdev.apprecconproject.alert.messagin.alertMessage
import com.rojasdev.apprecconproject.alert.work.alertCancelWork
import com.rojasdev.apprecconproject.controller.controllerTheme
import com.rojasdev.apprecconproject.controller.customSnackBar
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.controller.scrolling
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.dataModel.workTotalCollector
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.FragmentCollectorsAndCollecionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentWorkCancelet(
    var scroll:(String)-> Unit,
    var preferences:()-> Unit
) : Fragment() {

    private var _binding: FragmentCollectorsAndCollecionBinding? = null
    private lateinit var adapter: adapterRvWorkTotal
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

        scrolling.scrolling(binding.rvCollectors) {
            scroll(it)
        }

        getTotalCollection()
        return binding.root
    }

    private fun totalCollectionCollector(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = AppDataBase.getInstance((requireContext())).RecolectoresDao().getIDManWork()
            launch(Dispatchers.Main) {
                val collector = mutableListOf<workTotalCollector>()
                for(item in idCollectors){
                    val collectionTotal = AppDataBase.getInstance((requireContext())).RecolectoresDao().getManAndWorkTotal(item.toInt())
                    if(collectionTotal[0].name_recolector != null){
                        collector.add(collectionTotal[0])
                    }
                }
                dates(collector)
            }
        }
    }

    private fun dates(total:List<workTotalCollector>) {
        adapter = adapterRvWorkTotal(total,getColor()) {
            initCancelCollection(it)
        }
        binding.rvCollectors.adapter = adapter
        binding.rvCollectors.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initCancelCollection(collectionTotal: workTotalCollector) {
        CoroutineScope(Dispatchers.IO).launch{
            val collection = AppDataBase.getInstance((requireContext())).RecolectoresDao().getMenAndWork("active",collectionTotal.PK_ID_Recolector)

            launch(Dispatchers.Main) {
                alertCancelWork(listOf(collectionTotal), collection) {
                    updateCollection(it)
                }.show(parentFragmentManager,"dialog")
            }
        }
    }

    private fun updateCollection(idUpdate: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val dataBase = AppDataBase.getInstance(requireContext())

            dataBase.RecolectoresDao().updateCollectorState(idUpdate)
            dataBase.WorkDao().updateWorkState(idUpdate)

            launch(Dispatchers.Main) {
                customSnackBar.showCustomSnackBar(requireView(),getString(R.string.collectionCanceled))
                getTotalCollection()
                totalCollectionCollector()
                preferencesUpdate()
            }
        }
    }

    private fun preferencesUpdate(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = AppDataBase.getInstance((requireContext())).WorkDao().getFkIdCollectors()
            launch(Dispatchers.Main) {
                if(idCollectors.isEmpty()){
                    alertMessage(
                        getString(R.string.txtNewReport),
                        getString(R.string.txtCalendar),
                        getString(R.string.txtGoReport),
                        getString(R.string.txtReturnMenu),
                        getString(R.string.txtRecolectionFull)
                    ){
                        if(it == "yes"){
                            preferences()
                            startActivity(Intent(requireContext(), ActivityInformes::class.java))
                        }else{
                            preferences()
                            startActivity(Intent(requireContext(),ActivityMainModule::class.java))
                        }
                    }.show(parentFragmentManager,"dialog")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalCollection(){
        CoroutineScope(Dispatchers.IO).launch{
            val collectionTotal = AppDataBase.getInstance((requireContext())).SettingDao().getTotalWorkActive()
            launch(Dispatchers.Main) {
                if(collectionTotal.isNotEmpty()){
                    binding.lyTotal.visibility = View.VISIBLE
                    binding.tvCollection.text = "Jornales trabajados\n ${collectionTotal[0].cantidad.toInt()}"

                    price.priceSplit(collectionTotal[0].total.toInt()){
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
        controllerTheme.main(requireContext(),
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