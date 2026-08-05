package com.rojasdev.apprecconproject.legacy

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityDetalleWork : androidx.appcompat.app.AppCompatActivity() {

    lateinit var binding: ActivityRecolectionDetailBinding
    private lateinit var adapter: com.rojasdev.apprecconproject.legacy.adapters.adapterRvWork
    private var idCollector: Int? = null
    private var userName: String? = null
    private var prices: List<com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Recibir parametros
        idCollector = intent.getIntExtra("userId", 0)
        userName = intent.getStringExtra("userName")

        title = userName

        com.rojasdev.apprecconproject.legacy.controller.controllerTheme.main(
            this,
            day = {
                binding.viewHeaderBackground.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, com.rojasdev.apprecconproject.R.color.Orange))
            },
            night = {
                binding.viewHeaderBackground.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, com.rojasdev.apprecconproject.R.color.OrangeDark))
            }
        )

        getRecollection(idCollector!!)
    }

    private fun getRecollection(idCollector: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val work = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityDetalleWork).WorkDao().getWorkIdMen(idCollector)
            val totalRecolection = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityDetalleWork).WorkDao().getTotalDayWork(idCollector)
            val totalMoneyWork = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityDetalleWork).WorkDao().getTotalMoney(idCollector)
            prices = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityDetalleWork).SettingDao().getPriceWorkState("active")

            launch(Dispatchers.Main) {
                adapter =
                    com.rojasdev.apprecconproject.legacy.adapters.adapterRvWork(
                        work
                    ) {
                        // Update Collection
                        alertUpdateRecollection(it, idCollector)
                    }

                com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(totalMoneyWork) {
                    binding.tvTotal.text = "${getString(com.rojasdev.apprecconproject.R.string.totalPrinceCancel)}\n $it"
                }

                binding.tvTitle.text = "${getString(com.rojasdev.apprecconproject.R.string.WorkNumerb)}\n ${totalRecolection}"
                binding.rvRecolections.adapter = adapter
                binding.rvRecolections.layoutManager = LinearLayoutManager(this@ActivityDetalleWork)
            }
        }
    }

    private fun alertUpdateRecollection(it: com.rojasdev.apprecconproject.legacy.data.dataModel.workSettings, idCollector: Int){
        val workUpdate =
            com.rojasdev.apprecconproject.legacy.data.entities.WorkEntity(
                it.PK_ID_Trabajo,
                it.cantidad,
                it.actividad,
                it.Fecha,
                it.Estado,
                it.Fk_recolector,
                it.Fk_Configuracion
            )
        com.rojasdev.apprecconproject.legacy.alert.work.alertUpdateWork(
            userName!!,
            workUpdate,
            prices!!
        ) {
            updateCollection(it, idCollector)
        }.show(supportFragmentManager,"dialog")
    }

    private fun updateCollection(it: com.rojasdev.apprecconproject.legacy.data.entities.WorkEntity, idCollector: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityDetalleWork).WorkDao().updateWork(
                idCollector,
                it.date,
                it.collector,
                it.amount,
                it.total,
                it.setting
            )
            launch(Dispatchers.Main){
                getRecollection(idCollector)
                com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(binding.rvRecolections,getString(
                    com.rojasdev.apprecconproject.R.string.updateFinish))
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.rojasdev.apprecconproject.R.menu.edit,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            com.rojasdev.apprecconproject.R.id.editName -> showAlertEditName()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertEditName() {
        com.rojasdev.apprecconproject.legacy.alert.collection.alertUpdateNameCollector(
            idCollector!!,
            userName.toString(),
            true
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(
                    this@ActivityDetalleWork
                ).RecolectoresDao().updateCollectorName(it.id!!, it.name)
                launch(Dispatchers.Main) {
                    com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(
                        binding.rvRecolections,
                        getString(com.rojasdev.apprecconproject.R.string.editNameReady)
                    )
                }
            }
        }.show(supportFragmentManager,"dialog")
    }
}