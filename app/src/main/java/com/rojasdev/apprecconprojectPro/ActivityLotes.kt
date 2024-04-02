package com.rojasdev.apprecconprojectPro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.rojasdev.apprecconprojectPro.adapters.adapterRvCollectors
import com.rojasdev.apprecconprojectPro.adapters.adapterRvLotes
import com.rojasdev.apprecconprojectPro.adapters.adapterRvLotesP
import com.rojasdev.apprecconprojectPro.adapters.adapterRvSettings
import com.rojasdev.apprecconprojectPro.alert.alertAddLotes
import com.rojasdev.apprecconprojectPro.alert.alertDeleteCollector
import com.rojasdev.apprecconprojectPro.alert.alertSettingsUpdate
import com.rojasdev.apprecconprojectPro.alert.alertUpdateNameLote
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.controller.connected
import com.rojasdev.apprecconprojectPro.controller.customSnackBar
import com.rojasdev.apprecconprojectPro.controller.dateFormat
import com.rojasdev.apprecconprojectPro.controller.price
import com.rojasdev.apprecconprojectPro.data.dataBase.AppDataBase
import com.rojasdev.apprecconprojectPro.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconprojectPro.data.dataModel.lotesAndCollection
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.data.entities.SettingEntity
import com.rojasdev.apprecconprojectPro.databinding.ActivityLotesBinding
import com.rojasdev.apprecconprojectPro.databinding.ActivitySettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityLotes : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private val fireStoreDB = FirebaseFirestore.getInstance()
    val database = FirebaseDatabase.getInstance()
    lateinit var binding : ActivityLotesBinding
    private lateinit var adapter: adapterRvLotes
    var lotesAndCollection = mutableListOf<lotesAndCollection>()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLotesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = "lotes"

        initResiclerView()

        binding.btProduccion.setOnClickListener {
            binding.clPreciosVigentes.visibility = View.GONE
            setupRecyclerViewArchived()
        }

        binding.btExit.setOnClickListener {
            binding.clPreciosVigentes.visibility = View.VISIBLE
            setupRecyclerView()
        }

        binding.fbNewLote.setOnClickListener {
            alertAddLotes(
                1,
                {
                    insertLote(it)
                },
                {
                    insertLoteFireBase()
                }
            ).show(supportFragmentManager,"dialog")
        }

    }

    private fun getLotes(){
        CoroutineScope(Dispatchers.IO).launch{
            val lotes = AppDataBase.getInstance(this@ActivityLotes).LoteDao().get()
            launch(Dispatchers.Main) {
                adapter = adapterRvLotes(
                    lotes,
                    {update->
                        if (connected.isConnected(this@ActivityLotes)){
                            initUpdate(update)
                        }else{
                           customSnackBar.showCustomSnackBar(binding.viewPagerIc2,getString(R.string.noConnected))
                        }
                    },
                    {delete->
                        if (connected.isConnected(this@ActivityLotes)){
                            initDelete(delete)
                        }else{
                            customSnackBar.showCustomSnackBar(binding.viewPagerIc2,getString(R.string.noConnected))
                        }
                    }
                )

                binding.rvLotes.adapter = adapter
                binding.rvLotes.layoutManager = LinearLayoutManager(this@ActivityLotes)
            }
        }
    }

    private fun initDelete(delete: LoteEntity) {
        alertDeleteCollector(
            delete.name
        ){
            delete(delete.id!!)
            initResiclerView()
        }.show(supportFragmentManager,"dialog")
    }

    private fun initUpdate(update: LoteEntity) {
        alertUpdateNameLote(
            update.id!!.toInt(),
            update.name
        ){
            updateLote(it)
        }.show(supportFragmentManager,"dialog")
    }

    private fun updateLote(it : LoteEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityLotes).LoteDao().updateLoteName(it.id!!,it.name)
            launch(Dispatchers.Main) {
                customSnackBar.showCustomSnackBar(binding.viewPagerIc2,getString(R.string.editNameReady))
            }
        }
    }

    private fun initResiclerView() {
        getProductionLotes()
        getLotes()
    }

    private fun delete(it:Int) {
        customSnackBar.showCustomSnackBar(binding.viewPagerIc2,getString(R.string.deleteCollector))
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityLotes).LoteDao().deleteLoteId(it)
        }
    }


    private fun setupRecyclerView() {
        binding.rvProduccion.setPadding(0,0,0,0)
        binding.rvProduccion.apply {
            if(lotesAndCollection.isEmpty()){
                noHistory()
            }else{
                visibilityButton(lotesAndCollection.size)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = adapterRvLotesP(lotesAndCollection)
            }
        }
    }

    private fun setupRecyclerViewArchived() {
        val height = resources.displayMetrics.widthPixels
        val setPadding = height.div(3.9)
        binding.rvProduccion.setPadding(0,0,0,setPadding.toInt())
                binding.rvProduccion.apply {
                    title = getString(R.string.todoLote)
                    binding.btProduccion.visibility = View.GONE
                    binding.btExit.visibility = View.VISIBLE
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = adapterRvLotesP(lotesAndCollection)

        }
    }

    private fun getProductionLotes(){
        CoroutineScope(Dispatchers.IO).launch{
            val lotes = AppDataBase.getInstance(this@ActivityLotes).LoteDao().get()
            launch(Dispatchers.Main) {
                for(item in lotes){
                    val query = AppDataBase.getInstance(this@ActivityLotes).LoteDao().getCollectionLote(item.id!!)
                    if(query[0].name_lote != null){
                        lotesAndCollection.add(query[0])
                    }
                }
                setupRecyclerView()
            }
        }
    }

    private fun noHistory() {
        title = "lotes"
        binding.rvProduccion.visibility = View.GONE
        binding.btExit.visibility = View.GONE
    }

    private fun visibilityButton(size: Int) {
        title = getString(R.string.ProduccionLotes)
        binding.btExit.visibility = View.GONE
        if(size > 4)
            binding.btProduccion.visibility = View.VISIBLE
        else
            binding.btProduccion.visibility = View.GONE
    }

    private fun insertLote(lote: LoteEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityLotes).LoteDao().add(lote)
            launch(Dispatchers.Main) {
                insertLoteFireBase()
            }
        }
    }

    private fun uodateStateLote(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityLotes).LoteDao().updateLoteState(id,true)
        }
    }

    private fun insertLoteFireBase() {
        showProgress()
        CoroutineScope(Dispatchers.IO).launch {
            val lotes = AppDataBase.getInstance(this@ActivityLotes).LoteDao().getNoFisebase(false)
            launch(Dispatchers.Main) {
                for (lote in lotes){
                    val userId = mAuth.currentUser!!.uid

                    val userData = hashMapOf(
                        "id" to lote.id,
                        "name" to lote.name,
                        "type" to lote.type,
                        "finca" to lote.finca
                    )

                    fireStoreDB.collection("/Finca/$userId/Lotes").document()
                        .set(userData)
                        .addOnSuccessListener {
                            initResiclerView()
                            goneProgress()
                            uodateStateLote(lote.id!!)
                        }
                }
            }
        }
    }

    fun showProgress(){
        binding.progressBar.visibility = View.VISIBLE
        binding.tvLoading.visibility = View.VISIBLE
    }

    fun goneProgress(){
        binding.progressBar.visibility = View.GONE
        binding.tvLoading.visibility = View.GONE
    }

}