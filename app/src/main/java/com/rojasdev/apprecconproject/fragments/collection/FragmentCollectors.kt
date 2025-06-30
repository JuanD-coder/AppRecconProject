package com.rojasdev.apprecconproject.fragments.collection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.ActivityRecolectionDetail
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.adapterRvCollectors
import com.rojasdev.apprecconproject.alert.collection.alertCollection
import com.rojasdev.apprecconproject.alert.collection.alertDeleteCollector
import com.rojasdev.apprecconproject.alert.messagin.alertMessage
import com.rojasdev.apprecconproject.controller.customSnackBar
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.FragmentCollectorsAndCollecionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentCollectors(
    var scroll: (String) -> Unit,
    var preferences: () -> Unit
) : Fragment() {

    private lateinit var adapter: adapterRvCollectors
    private var _binding: FragmentCollectorsAndCollecionBinding? = null
    private val binding get() = _binding!!
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorsAndCollecionBinding.inflate(inflater, container, false)

        binding.lyTotal.visibility = View.GONE

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startActivity(Intent(requireContext(), ActivityMainModule::class.java))
                }
            })

        binding.searchViewCollectors.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isBlank()) {
                        CoroutineScope(Dispatchers.IO).launch { dates() }
                        binding.llEmptyView.visibility = View.GONE

                    } else {
                        onQueryTextChangeDb(it)
                    }
                }
                return true
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            dates()
        }

        adapter = adapterRvCollectors(
            emptyList(),
            emptyList(),
            { item -> initDetailCollector(item) },
            { initAlertDelete(it) },
            { initAlertAddCollection(it) }
        )
        binding.rvCollectors.adapter = adapter
        binding.rvCollectors.layoutManager = LinearLayoutManager(requireContext())

        com.rojasdev.apprecconproject.controller.scrolling.scrolling(binding.rvCollectors) {
            scroll(it)
        }

        return binding.root
    }

    private fun onQueryTextChangeDb(query: String) {
        searchJob?.cancel()
        searchJob = CoroutineScope(Dispatchers.IO).launch {
            delay(300)
            val db = AppDataBase.getInstance(requireContext())
            val filteredCollectors = db
                .RecolectoresDao()
                .searchCollectorsByName(searchQuery = query)

            val idCollectors = db
                .RecollectionDao()
                .getFkIdCollectors()

            withContext(Dispatchers.Main) {
                initRv(idCollectors, filteredCollectors)
                if (filteredCollectors.isEmpty()) {
                    binding.llEmptyView.visibility = View.VISIBLE
                } else {
                    binding.llEmptyView.visibility = View.GONE
                }
            }
        }
    }


    private suspend fun dates() {
        val db = AppDataBase.getInstance(requireContext())
        val idCollectors = db.RecollectionDao().getFkIdCollectors()
        val collectors = db.RecolectoresDao().getAllRecolector()

        withContext(Dispatchers.Main) {
            if (collectors.isNotEmpty()) {
                initRv(idCollectors, collectors)
                binding.llEmptyView.visibility = View.GONE
            } else {
                preferencesUpdate()
            }
        }
    }

    private fun initRv(idCollectors: List<Long>, collectors: List<RecolectoresEntity>) {
        adapter.updateData(collectors, idCollectors)
    }

    private fun initDetailCollector(item: RecolectoresEntity) {
        startActivity(
            Intent(
                requireContext(), ActivityRecolectionDetail::class.java
            ).putExtra("userId", item.id).putExtra("userName", item.name)
        )
    }

    private fun initAlertDelete(it: RecolectoresEntity) {
        alertDeleteCollector(it.name, false) {
            customSnackBar.showCustomSnackBar(requireView(), getString(R.string.deleteCollector))

            CoroutineScope(Dispatchers.IO).launch {
                AppDataBase.getInstance(requireContext()).RecolectoresDao()
                    .deleteCollectorId(it.id!!)
                launch { dates() }
            }

        }.show(parentFragmentManager, "dialog")
    }

    private fun initAlertAddCollection(it: RecolectoresEntity) {
        alertCollection(it) {
            insertCollection(it)
        }.show(parentFragmentManager, "dialog")
    }

    private fun insertCollection(recollection: RecollectionEntity) {
        customSnackBar.showCustomSnackBar(
            binding.fragmentCollectors,
            getString(R.string.addCollectionFinish)
        )

        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(requireContext()).RecollectionDao()
                .addRecollection(recollection)
            dates()
        }
    }

    private fun preferencesUpdate() {
        CoroutineScope(Dispatchers.IO).launch {
            val idCollectors =
                AppDataBase.getInstance((requireContext())).RecollectionDao().getFkIdCollectors()
            launch(Dispatchers.Main) {
                if (idCollectors.isEmpty()) {
                    binding.llEmptyView.visibility = View.VISIBLE
                    alertMessage(
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
                } else {
                    binding.llEmptyView.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}