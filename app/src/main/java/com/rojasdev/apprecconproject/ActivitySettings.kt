package com.rojasdev.apprecconproject

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adapterRvSettings
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
    private var idSetting: Int? = null
    private var priceSetting: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.previousPrice)

        adsBanner.initLoadAds(binding.banner)

        getPriceKg()

        binding.btUpdateNoAliment.setOnClickListener {
            animatedAlert.animatedClick(binding.cvNoAliment)
            alertSettingsUpdate(
                getString(R.string.notFeeding),
                idSetting!!,
                priceSetting!!,
                false
            ) {
                insertNewSetting(it) {
                    getPriceKg()
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

    private fun getPriceKg() {
        animatedAlert.animatedCv(binding.cvNoAliment)
        CoroutineScope(Dispatchers.IO).launch {
            val query = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAliment()
            launch(Dispatchers.Main) {
                idSetting = query[0].Id
                price.priceSplit(query[0].cost) {
                    priceSetting = query[0].cost
                    binding.tvAliment.text = it
                }
            }
        }
    }

    private fun insertNewSetting(setting: SettingEntity, ready: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val settingDao = AppDataBase.getInstance(this@ActivitySettings).SettingDao()
            val newSetting = SettingEntity(
                null,
                setting.cost,
                "active",
                dateFormat.main()
            )
            settingDao.insertConfig(newSetting)
            settingDao.updateConfig(setting.Id, "archived")
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
            val query =
                AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAlimentArchived()
            launch(Dispatchers.Main) {
                binding.rvSetTingHistory.apply {
                    if (query.isEmpty()) {
                        noHistory()
                    } else {
                        visibilityButton(query.size)
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
            val query =
                AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAlimentArchived()
            launch(Dispatchers.Main) {
                binding.rvSetTingHistory.apply {
                    title = getString(R.string.todo)
                    binding.btViewAlimentArchived.visibility = View.GONE
                    binding.btExit.visibility = View.VISIBLE
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = adapterRvSettings(query) {
                        message(it)
                    }
                }
            }
        }
    }

    private fun message(it: SettingEntity) {
        val message: String = getString(R.string.notFeeding)
        val date = dateFormat.format(it.date)

        customSnackBar.showCustomSnackBar(
            binding.rvSetTingHistory,
            "$message\n ${it.cost}\n ${date.first}"
        )
    }
}