package com.rojasdev.apprecconproject

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adapterRvWork
import com.rojasdev.apprecconproject.alert.collection.alertUpdateNameCollector
import com.rojasdev.apprecconproject.alert.work.alertUpdateWork
import com.rojasdev.apprecconproject.controller.controllerTheme
import com.rojasdev.apprecconproject.controller.customSnackBar
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.dataModel.workSettings
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.data.entities.WorkEntity
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityDetalleWork : AppCompatActivity() {

    lateinit var binding: ActivityRecolectionDetailBinding
    private lateinit var adapter: adapterRvWork
    private var idCollector: Int? = null
    private var userName: String? = null
    private var prices: List<SettingEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Recibir parametros
        idCollector = intent.getIntExtra("userId", 0)
        userName = intent.getStringExtra("userName")

        title = userName

        controllerTheme.main(
            this,
            day = {
                binding.viewHeaderBackground.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Orange))
            },
            night = {
                binding.viewHeaderBackground.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.OrangeDark))
            }
        )

        getRecollection(idCollector!!)
    }

    private fun getRecollection(idCollector: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val work = AppDataBase.getInstance(this@ActivityDetalleWork).WorkDao().getWorkIdMen(idCollector)
            val totalRecolection = AppDataBase.getInstance(this@ActivityDetalleWork).WorkDao().getTotalDayWork(idCollector)
            val totalMoneyWork = AppDataBase.getInstance(this@ActivityDetalleWork).WorkDao().getTotalMoney(idCollector)
            prices = AppDataBase.getInstance(this@ActivityDetalleWork).SettingDao().getPriceWorkState("active")

            launch(Dispatchers.Main) {
                adapter = adapterRvWork(work) {
                    // Update Collection
                    alertUpdateRecollection(it, idCollector)
                }

                price.priceSplit(totalMoneyWork) {
                    binding.tvTotal.text = "${getString(R.string.totalPrinceCancel)}\n $it"
                }

                binding.tvTitle.text = "${getString(R.string.WorkNumerb)}\n ${totalRecolection}"
                binding.rvRecolections.adapter = adapter
                binding.rvRecolections.layoutManager = LinearLayoutManager(this@ActivityDetalleWork)
            }
        }
    }

    private fun alertUpdateRecollection(it:workSettings, idCollector: Int){
        val workUpdate = WorkEntity(
            it.PK_ID_Trabajo,
            it.cantidad,
            it.actividad,
            it.Fecha,
            it.Estado,
            it.Fk_recolector,
            it.Fk_Configuracion
        )
        alertUpdateWork(
            userName!!,
            workUpdate,
            prices!!
        ){
            updateCollection(it, idCollector)
        }.show(supportFragmentManager,"dialog")
    }

    private fun updateCollection(it: WorkEntity, idCollector: Int) {
        Toast.makeText(this, it.total, Toast.LENGTH_SHORT).show()
 /*

        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityDetalleWork).WorkDao().updateWork(
                it.ID!!,
                it.date,
                it.collector,
                it.amount,
                it.total,
                it.setting
            )
            launch(Dispatchers.Main){
                getRecollection(idCollector)
                customSnackBar.showCustomSnackBar(binding.rvRecolections,getString(R.string.updateFinish))
            }
        }

  */
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.editName -> showAlertEditName()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertEditName() {
        alertUpdateNameCollector(
            idCollector!!,
            userName.toString()
        ){
            CoroutineScope(Dispatchers.IO).launch {
                AppDataBase.getInstance(this@ActivityDetalleWork).RecolectoresDao().updateCollectorName(it.id!!,it.name)
                launch(Dispatchers.Main) {
                    customSnackBar.showCustomSnackBar(binding.rvRecolections,getString(R.string.editNameReady))
                }
            }
        }.show(supportFragmentManager,"dialog")
    }
}