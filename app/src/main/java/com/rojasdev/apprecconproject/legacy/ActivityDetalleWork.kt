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

        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.controllerTheme.main(
            this,
            day = {
                binding.viewHeaderBackground.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, _root_ide_package_.com.rojasdev.apprecconproject.R.color.Orange))
            },
            night = {
                binding.viewHeaderBackground.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, _root_ide_package_.com.rojasdev.apprecconproject.R.color.OrangeDark))
            }
        )

        getRecollection(idCollector!!)
    }

    private fun getRecollection(idCollector: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val work = _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityDetalleWork).WorkDao().getWorkIdMen(idCollector)
            val totalRecolection = _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityDetalleWork).WorkDao().getTotalDayWork(idCollector)
            val totalMoneyWork = _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityDetalleWork).WorkDao().getTotalMoney(idCollector)
            prices = _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityDetalleWork).SettingDao().getPriceWorkState("active")

            launch(Dispatchers.Main) {
                adapter =
                    _root_ide_package_.com.rojasdev.apprecconproject.legacy.adapters.adapterRvWork(
                        work
                    ) {
                        // Update Collection
                        alertUpdateRecollection(it, idCollector)
                    }

                _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(totalMoneyWork) {
                    binding.tvTotal.text = "${getString(_root_ide_package_.com.rojasdev.apprecconproject.R.string.totalPrinceCancel)}\n $it"
                }

                binding.tvTitle.text = "${getString(_root_ide_package_.com.rojasdev.apprecconproject.R.string.WorkNumerb)}\n ${totalRecolection}"
                binding.rvRecolections.adapter = adapter
                binding.rvRecolections.layoutManager = LinearLayoutManager(this@ActivityDetalleWork)
            }
        }
    }

    private fun alertUpdateRecollection(it: com.rojasdev.apprecconproject.legacy.data.dataModel.workSettings, idCollector: Int){
        val workUpdate =
            _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.entities.WorkEntity(
                it.PK_ID_Trabajo,
                it.cantidad,
                it.actividad,
                it.Fecha,
                it.Estado,
                it.Fk_recolector,
                it.Fk_Configuracion
            )
        _root_ide_package_.com.rojasdev.apprecconproject.legacy.alert.work.alertUpdateWork(
            userName!!,
            workUpdate,
            prices!!
        ) {
            updateCollection(it, idCollector)
        }.show(supportFragmentManager,"dialog")
    }

    private fun updateCollection(it: com.rojasdev.apprecconproject.legacy.data.entities.WorkEntity, idCollector: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityDetalleWork).WorkDao().updateWork(
                idCollector,
                it.date,
                it.collector,
                it.amount,
                it.total,
                it.setting
            )
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
            true
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                _root_ide_package_.com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(
                    this@ActivityDetalleWork
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