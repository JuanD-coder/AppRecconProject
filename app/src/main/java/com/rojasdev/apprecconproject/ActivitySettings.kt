package com.rojasdev.apprecconproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adapterRvPricesWork
import com.rojasdev.apprecconproject.adapters.adapterRvSettings
import com.rojasdev.apprecconproject.alert.settings.alertAddPriceWork
import com.rojasdev.apprecconproject.alert.settings.alertSettingsUpdate
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.customSnackBar
import com.rojasdev.apprecconproject.controller.dateFormat
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.ActivitySettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivitySettings : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    private var idNoAliment: Int? = null
    private var idYesAliment: Int? = null
    private var priceYesAliment: Int? = null
    private var priceNoAliment: Int? = null
    lateinit var adapterRvPricesWork: adapterRvPricesWork
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.previousPrice)

        adsBanner.initLoadAds(binding.banner)

        getNoAliment()
        getYesAliment()
        getPricesWork()

        CoroutineScope(Dispatchers.IO).launch {
            val count = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getPriceWorkCount()
            launch(Dispatchers.Main) {
                if (count > 6) {
                    binding.btnAddWork.visibility = View.GONE
                    getPricesWork()
                    preferencesWorkMen()

                } else {
                    binding.btnAddWork.visibility = View.VISIBLE
                    binding.btnAddWork.setOnClickListener {
                        alertAddPriceWork()
                    }
                }
            }
        }

        binding.btUpdateNoAliment.setOnClickListener {
            animatedAlert.animatedClick(binding.cvNoAliment)
            alertSettingsUpdate(
                getString(R.string.notFeeding),
                "no",
                idNoAliment!!,
                priceNoAliment!!,
                false
            ) {
                insertNewSetting(it) {
                    getNoAliment()
                    setupRecyclerView()
                }
            }.show(supportFragmentManager, "dialog")
        }

        binding.btUpdateYesAliment.setOnClickListener {
            animatedAlert.animatedClick(binding.cvYesAliment)
            alertSettingsUpdate(
                getString(R.string.yesFeeding),
                "yes",
                idYesAliment!!,
                priceYesAliment!!,
                false
            ) {
                insertNewSetting(it) {
                    getYesAliment()
                    setupRecyclerView()
                }
            }.show(supportFragmentManager, "dialog")
        }

        binding.btViewAlimentArchived.setOnClickListener {
            binding.clPreciosVigentes.visibility = View.GONE
            setupRecyclerViewArchived()
        }

        binding.btExit.setOnClickListener {
            setupRecyclerView()
            binding.clPreciosVigentes.visibility = View.VISIBLE
        }

        setupRecyclerView()
    }

    private fun alertAddPriceWork() {
        val dialog = alertAddPriceWork(
            { insertSettings(it) }, { getPricesWork() }
        )

        dialog.show(supportFragmentManager, "dialog")
    }

    private fun preferencesWorkMen() {
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("workMen","true")
        editor.apply()
    }

    private fun insertSettings(settings: SettingEntity){
        CoroutineScope(Dispatchers.IO).launch{
            AppDataBase.getInstance(this@ActivitySettings).SettingDao().insertConfig(settings)
            launch {
                setupRecyclerView()
                getPricesWork()
            }
        }
    }

    private fun getPricesWork() {
        CoroutineScope(Dispatchers.IO).launch {
            val query = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getPriceWork()
            launch(Dispatchers.Main) {
                if (query.isNotEmpty()) {
                    Toast.makeText(this@ActivitySettings, query[0].feeding, Toast.LENGTH_SHORT).show()
                    adapterRvPricesWork = adapterRvPricesWork(query) {
                        alertSettingsUpdate(it.feeding, it.feeding, it.Id!!, it.cost, true) { entity ->
                            insertNewSetting(entity) {
                                getPricesWork()
                                setupRecyclerView()
                            }
                        }.show(supportFragmentManager, "dialog")
                    }
                    binding.rvPricesWork.adapter = adapterRvPricesWork
                    binding.rvPricesWork.layoutManager = LinearLayoutManager(this@ActivitySettings)
                }
            }
        }
    }

    private fun getNoAliment() {
        animatedAlert.animatedCv(binding.cvNoAliment)
        CoroutineScope(Dispatchers.IO).launch {
            val query = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAliment("no")
            launch(Dispatchers.Main) {
                idNoAliment = query[0].Id
                price.priceSplit(query[0].cost) {
                    priceNoAliment = query[0].cost
                    binding.tvNoAliment.text = it
                }
            }
        }
    }

    private fun getYesAliment() {
        animatedAlert.animatedCv(binding.cvYesAliment)
        CoroutineScope(Dispatchers.IO).launch {
            val query =
                AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAliment("yes")
            launch(Dispatchers.Main) {
                idYesAliment = query[0].Id
                price.priceSplit(query[0].cost) {
                    priceYesAliment = query[0].cost
                    binding.tvYesAliment.text = it
                }
            }
        }
    }


    private fun insertNewSetting(setting: SettingEntity, ready: () -> Unit) {
        val newSetting = SettingEntity(
            null,
            setting.feeding,
            setting.cost,
            "active",
            dateFormat.main()
        )
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivitySettings).SettingDao().insertConfig(newSetting)
            AppDataBase.getInstance(this@ActivitySettings).SettingDao()
                .updateConfig(setting.Id, "archived")
            launch(Dispatchers.Main) {
                ready()
                customSnackBar.showCustomSnackBar(
                    binding.textView,
                    getString(R.string.settingsUpdate)
                )
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvSetTingHistory.setPadding(0, 0, 0, 0)
        CoroutineScope(Dispatchers.IO).launch {
            val query = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAlimentArchived()
            launch(Dispatchers.Main) {
                binding.rvSetTingHistory.apply {
                    if (query.isEmpty()) {
                        noHistory()
                    } else {
                        visibilityButton(query.size)
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = adapterRvSettings(query) {}
                    }
                }
            }
        }
    }

    private fun noHistory() {
        title = getString(R.string.settingsAliment)
        binding.btViewAlimentArchived.visibility = View.GONE
        binding.btExit.visibility = View.GONE
    }

    private fun visibilityButton(size: Int) {
        title = getString(R.string.previousPrice)
        binding.btExit.visibility = View.GONE
        if (size > 4)
            binding.btViewAlimentArchived.visibility = View.VISIBLE
        else
            binding.btViewAlimentArchived.visibility = View.GONE
    }

    private fun setupRecyclerViewArchived() {
        val height = resources.displayMetrics.widthPixels
        val setPadding = height.div(3.9)
        binding.rvSetTingHistory.setPadding(0, 0, 0, setPadding.toInt())
        CoroutineScope(Dispatchers.IO).launch {
            val query = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAlimentArchived()
            launch(Dispatchers.Main) {
                binding.rvSetTingHistory.apply {
                    title = getString(R.string.todo)
                    binding.btViewAlimentArchived.visibility = View.GONE
                    binding.btExit.visibility = View.VISIBLE
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = adapterRvSettings(query) {
                        message(it)
                    }
                }
            }
        }
    }

    private fun message(it: SettingEntity) {
        val message: String = if (it.feeding == "yes")
            getString(R.string.yesFeeding)
        else
            getString(R.string.notFeeding)
        val date = dateFormat.format(it.date)
        customSnackBar.showCustomSnackBar(
            binding.rvSetTingHistory,
            "$message\n ${it.cost}\n ${date.first}"
        )
    }
}