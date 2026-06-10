package com.rojasdev.apprecconproject.legacy

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityRecolectionDetail : androidx.appcompat.app.AppCompatActivity() {

    lateinit var binding: ActivityRecolectionDetailBinding
    private lateinit var adapter: com.rojasdev.apprecconproject.legacy.adapters.adapterRvRecolection
    private lateinit var collectionUpdate: List<com.rojasdev.apprecconproject.legacy.data.dataModel.collectorCollection>
    private lateinit var collectionTotal: List<com.rojasdev.apprecconproject.legacy.data.dataModel.collecionTotalCollector>
    private var idCollector: Int? = null
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        // Recibir parametros
        idCollector = intent.getIntExtra("userId", 0)
        userName = intent.getStringExtra("userName")

        title = userName

        getRecollection(idCollector!!)

    }

    @SuppressLint("SetTextI18n")
    private fun getRecollection(idCollector: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val collection= _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityRecolectionDetail).RecolectoresDao().getCollectorAndCollection("active",idCollector)

            val totalRecolection = _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityRecolectionDetail).RecolectoresDao().getCollectorAndCollectionTotal(idCollector)
            launch(Dispatchers.Main) {
                collectionUpdate = listOf(collection[0])
                collectionTotal = listOf(totalRecolection[0])
                adapter =
                    _root_ide_package_.com.rojasdev.apprecconproject.legacy.adapters.adapterRvRecolection(
                        collection
                    ) {
                        // Update Collection
                        alertUpdateRecollection(it, idCollector)
                    }
                _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(totalRecolection[0].price_total.toInt()){
                    binding.tvTotal.text = "${getString(_root_ide_package_.com.rojasdev.apprecconproject.R.string.totalPrinceCancel)}\n $it"
                }

                binding.tvTitle.text = "${getString(_root_ide_package_.com.rojasdev.apprecconproject.R.string.recolection)}\n ${totalRecolection[0].kg_collection.toFloat()} Kg"
                binding.rvRecolections.adapter = adapter
                binding.rvRecolections.layoutManager = LinearLayoutManager(this@ActivityRecolectionDetail)
            }
        }
    }

    private fun alertUpdateRecollection(it: com.rojasdev.apprecconproject.legacy.data.dataModel.collectorCollection, idCollector: Int){
        _root_ide_package_.com.rojasdev.apprecconproject.legacy.alert.collection.alertCollectionUpdate(
            it.PK_ID_Recoleccion,
            idCollector,
            it.Alimentacion,
            it.Cantidad,
            it.name_recolector
        ) {
            updateCollection(it, idCollector)
        }.show(supportFragmentManager,"dialog")
    }

    private fun updateCollection(it: com.rojasdev.apprecconproject.legacy.data.entities.RecollectionEntity, idCollector: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityRecolectionDetail).RecollectionDao().updateCollection(it.ID!!,it.date,it.collector,it.total,it.setting)
            launch(Dispatchers.Main){
                getRecollection(idCollector)
                _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(binding.rvRecolections,getString(
                    _root_ide_package_.com.rojasdev.apprecconproject.R.string.updateFinish))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(_root_ide_package_.com.rojasdev.apprecconproject.R.menu.edit,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            _root_ide_package_.com.rojasdev.apprecconproject.R.id.editName -> showAlertEditName()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertEditName() {
        _root_ide_package_.com.rojasdev.apprecconproject.legacy.alert.collection.alertUpdateNameCollector(
            idCollector!!,
            userName.toString(),
            false
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(
                    this@ActivityRecolectionDetail
                ).RecolectoresDao().updateCollectorName(it.id!!, it.name)
                launch(Dispatchers.Main) {
                    _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(
                        binding.rvRecolections,
                        getString(_root_ide_package_.com.rojasdev.apprecconproject.R.string.editNameReady)
                    )
                }
            }
        }.show(supportFragmentManager,"dialog")
    }
}
