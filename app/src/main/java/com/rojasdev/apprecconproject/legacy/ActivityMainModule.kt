package com.rojasdev.apprecconproject.legacy

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.rojasdev.apprecconproject.databinding.ActivityMainModuleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityMainModule : androidx.appcompat.app.AppCompatActivity() {

    private lateinit var consentInformation: ConsentInformation

    lateinit var binding: ActivityMainModuleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainModuleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        title = getString(com.rojasdev.apprecconproject.R.string.priceTitle)

        this.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })

        getRGPD()
        checkRegister()

        binding.cvWork.setOnClickListener {
            checkRegister()
            com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedClick(binding.cvWork)
            checkWork()
        }

        binding.cvInformes.setOnClickListener {
            checkRegister()
            com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedClick(binding.cvInformes)
            val intent = Intent(this,ActivityInformes::class.java)
            intent.putExtra("fragment","")
            startActivity(intent)
        }

        binding.cvCollection.setOnClickListener {
            checkRegister()
            com.rojasdev.apprecconproject.legacy.controller.animatedAlert.animatedClick(binding.cvCollection)
                checkCollection()
        }

        binding.btSettings.setOnClickListener {
            checkRegister()
            startActivity(Intent(this,ActivitySettings::class.java))
        }

    }

    private fun getRGPD() {
        val params = ConsentRequestParameters
            .Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(
            this
        )
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                com.rojasdev.apprecconproject.legacy.controller.recconApp()
            },
            {
                Log.i("eoo", it.message)
            }
        )
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.rojasdev.apprecconproject.R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            com.rojasdev.apprecconproject.R.id.support -> help("¡Holaa amigos de RECCON!")
            com.rojasdev.apprecconproject.R.id.apoyo -> com.rojasdev.apprecconproject.legacy.alert.messagin.alertApoyo()
                .show(supportFragmentManager,"dialog")
            com.rojasdev.apprecconproject.R.id.delete -> alertDeleteTODO()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun alertDeleteTODO() {
                com.rojasdev.apprecconproject.legacy.alert.messagin.alertMessage(
                    getString(com.rojasdev.apprecconproject.R.string.deleteA),
                    getString(com.rojasdev.apprecconproject.R.string.deleteB),
                    getString(com.rojasdev.apprecconproject.R.string.deleteBtn),
                    getString(com.rojasdev.apprecconproject.R.string.txtStop),
                    getString(com.rojasdev.apprecconproject.R.string.cuidado)
                ) {
                    if (it == "yes") {
                        deleteDB()
                    } else {
                        startActivity(Intent(this, ActivityMainModule::class.java))
                    }
                }.show(supportFragmentManager,"dialog")
    }

    private fun deleteDB() {
        CoroutineScope(Dispatchers.IO).launch{
            val query = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityMainModule).RecollectionDao().getFkIdCollectors()
            launch(Dispatchers.Main) {
                if (query.isEmpty()){
                    com.rojasdev.apprecconproject.legacy.alert.messagin.alertCountDown {
                        CoroutineScope(Dispatchers.IO).launch {
                            com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(
                                this@ActivityMainModule
                            ).RecollectionDao().delete()
                            com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(
                                this@ActivityMainModule
                            ).RecolectoresDao().delete()
                            com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(
                                this@ActivityMainModule
                            ).SettingDao().delete()
                            launch(Dispatchers.Main) {
                                startActivity(
                                    Intent(
                                        this@ActivityMainModule,
                                        ActivityMainModule::class.java
                                    )
                                )
                                com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(
                                    binding.textView,
                                    ""
                                )
                                alerts()
                            }
                        }
                    }.show(supportFragmentManager,"dialog")
                }else{
                    com.rojasdev.apprecconproject.legacy.controller.customSnackBar.showCustomSnackBar(binding.textView,getString(
                        com.rojasdev.apprecconproject.R.string.errorDeleteDates))
                }
            }
        }
    }

    private fun help(message: String) {
        com.rojasdev.apprecconproject.legacy.alert.messagin.alertHelp {
            try {
                val phone = "573170157414"
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_VIEW
                val uri = "whatsapp://send?phone=${phone}&text=${message}"
                sendIntent.data = Uri.parse(uri)
                startActivity(sendIntent)
            } catch (e: ActivityNotFoundException) {
                com.rojasdev.apprecconproject.legacy.alert.messagin.alertMessage(
                    getString(com.rojasdev.apprecconproject.R.string.install),
                    getString(com.rojasdev.apprecconproject.R.string.message),
                    getString(com.rojasdev.apprecconproject.R.string.playSore),
                    getString(com.rojasdev.apprecconproject.R.string.ready),
                    getString(com.rojasdev.apprecconproject.R.string.noWhatsApp)
                ) {
                    if (it == "yes") {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/search?q=whatsapp&c=apps&hl=es_419&gl=US")
                        )
                        startActivity(intent)
                    } else {
                        startActivity(Intent(this, ActivityMainModule::class.java))
                    }
                }.show(supportFragmentManager, "dialog")
            }
        }.show(supportFragmentManager,"dialog")
    }

    private fun alerts(){
        com.rojasdev.apprecconproject.legacy.alert.messagin.alertWelcome {
            com.rojasdev.apprecconproject.legacy.alert.settings.alertSettings {
                insertSettings(it)
            }.show(supportFragmentManager, "dialog")
        }.show(supportFragmentManager,"dialog")
    }


    private fun insertSettings(settings: com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity){
        preferences()
        CoroutineScope(Dispatchers.IO).launch{
            com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityMainModule).SettingDao().insertConfig(settings)
            launch {
                checkRegister()
            }
        }
    }

    private fun preferences (){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("register","true")
        editor.putString("collection","false")
        editor.apply()
    }

    private fun preferencesWork (){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("work","true")
        editor.apply()
    }


    private fun checkRegister(){
        CoroutineScope(Dispatchers.IO).launch{
            val query = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityMainModule).SettingDao().getAliment("yes")
            launch(Dispatchers.Main) {
                if(query.isNotEmpty()){
                    val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
                    val register = preferences.getString("register","")
                    if(register != "true"){
                        alerts()
                    }else{
                        CoroutineScope(Dispatchers.IO).launch{
                            val query1 = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityMainModule).SettingDao().getAliment("yes")
                            launch(Dispatchers.Main) {
                                if(query1.isNotEmpty()){
                                    getYesAliment()
                                    getNoAliment()
                                }else{
                                    val preferences2 = getSharedPreferences( "register", Context.MODE_PRIVATE)
                                    val register2 = preferences2.getString("register","")
                                    if(register2 != "true"){
                                        alerts()
                                    }else{
                                        checkRegister()
                                    }
                                }
                            }
                        }
                    }
                }else{
                    alerts()
                }
            }
        }
    }

    private fun getNoAliment(){
        CoroutineScope(Dispatchers.IO).launch{
            val query = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityMainModule).SettingDao().getAliment("no")
            launch(Dispatchers.Main) {
                com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(query[0].cost){
                    binding.tvNoAliment.text = it
                }
            }
        }
    }

    private fun getYesAliment(){
        CoroutineScope(Dispatchers.IO).launch{
            val query = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityMainModule).SettingDao().getAliment("yes")
            launch(Dispatchers.Main) {
                if(query.isNotEmpty()){
                    com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(query[0].cost){
                        binding.tvYesAliment.text = it
                    }
                }
            }
        }
    }

    private fun checkWork(){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val work = preferences.getString("work","")
        if(work != "true"){
            alertAddWork()
        }else{
            checkWorkMen()
        }
    }

    private fun checkWorkMen(){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val work = preferences.getString("workMen","")
        if(work != "true"){
            alertAddWorkMen()
        }else{
            startActivity(Intent(this,ActivityWork::class.java))
        }
    }

    private fun checkCollection(){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val collection = preferences.getString("collection","")
        if(collection != "true"){
                CoroutineScope(Dispatchers.IO).launch{
                    val query = com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityMainModule).RecollectionDao().getFkIdCollectors()
                    launch(Dispatchers.Main) {
                        if(query.isEmpty()){
                            com.rojasdev.apprecconproject.legacy.alert.messagin.alertMessage(
                                "${binding.tvNoAliment.text}\n ${getString(com.rojasdev.apprecconproject.R.string.notAliment)}",
                                "${binding.tvYesAliment.text}\n ${getString(com.rojasdev.apprecconproject.R.string.yesAliment)}",
                                getString(com.rojasdev.apprecconproject.R.string.btCorrec),
                                getString(com.rojasdev.apprecconproject.R.string.noCorrec),
                                getString(com.rojasdev.apprecconproject.R.string.checkAliment)
                            ) {
                                if (it == "yes") {
                                    alertAddRecolcetor()
                                } else {
                                    startActivity(
                                        Intent(
                                            this@ActivityMainModule,
                                            ActivitySettings::class.java
                                        )
                                    )
                                }
                            }.show(supportFragmentManager,"dialog")
                        }else{
                            startActivity(Intent(this@ActivityMainModule,ActivityRecolection::class.java))
                        }
                    }
                }
        }else{
            startActivity(Intent(this,ActivityRecolection::class.java))
        }
    }

    private fun alertAddRecolcetor() {
        com.rojasdev.apprecconproject.legacy.alert.collection.alertAddRecolector(
            false,
            {
                insertRecolector(it)
            },
            {
                if (it) {
                    startActivity(Intent(this, ActivityRecolection::class.java))
                } else {
                    startActivity(Intent(this, ActivityMainModule::class.java))
                }
            }
        ).show(supportFragmentManager, "dialog")
    }

    private fun alertAddWorkMen() {
        com.rojasdev.apprecconproject.legacy.alert.collection.alertAddRecolector(
            true,
            {
                val newMen =
                    com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity(
                        id = null,
                        name = it.name,
                        state = "work-active"
                    )
                insertRecolector(newMen)
                preferencesWorkMen()
            },
            {
                if (it) {
                    startActivity(Intent(this, ActivityWork::class.java))
                } else {
                    startActivity(Intent(this, ActivityMainModule::class.java))
                }
            }
        ).show(supportFragmentManager, "dialog")
    }

    private fun alertAddWork() {
        com.rojasdev.apprecconproject.legacy.alert.settings.alertAddPriceWork(
            {
                insertSettings(it)
                preferencesWork()
            },
            {
                if (it) {
                    checkWorkMen()
                } else {
                    startActivity(Intent(this, ActivityMainModule::class.java))
                }
            }
        ).show(supportFragmentManager, "dialog")
    }

    private fun insertRecolector(recolector: com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityMainModule).RecolectoresDao().add(recolector)
        }
        preferencesCollecion()
    }

    private fun preferencesCollecion() {
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("collection","true")
        editor.apply()
    }

    private fun preferencesWorkMen() {
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("workMen","true")
        editor.apply()
    }

}
