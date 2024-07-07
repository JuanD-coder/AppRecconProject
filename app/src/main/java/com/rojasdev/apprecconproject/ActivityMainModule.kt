package com.rojasdev.apprecconproject

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.rojasdev.apprecconproject.alert.alertAddRecolector
import com.rojasdev.apprecconproject.alert.alertApoyo
import com.rojasdev.apprecconproject.alert.alertCountDown
import com.rojasdev.apprecconproject.alert.alertHelp
import com.rojasdev.apprecconproject.alert.alertMessage
import com.rojasdev.apprecconproject.alert.alertSettings
import com.rojasdev.apprecconproject.alert.alertWelcome
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.customSnackBar
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.controller.recconApp
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.ActivityMainModuleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class ActivityMainModule : AppCompatActivity() {

    private lateinit var consentInformation: ConsentInformation

    lateinit var binding: ActivityMainModuleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainModuleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        title = getString(R.string.priceTitle)

        this.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })

        getRGPD()

        checkRegister()

        binding.cvInformes.setOnClickListener {
            checkRegister()
            animatedAlert.animatedClick(binding.cvInformes)
            val intent = Intent(this,ActivityInformes::class.java)
            intent.putExtra("fragment","")
            startActivity(intent)
        }

        binding.cvCollection.setOnClickListener {
            checkRegister()
            animatedAlert.animatedClick(binding.cvCollection)
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
                recconApp()
            },
            {
                Log.i("eoo", it.message)
            }
        )
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.support -> help("¡Holaa amigos de RECCON!")
            R.id.apoyo -> alertApoyo().show(supportFragmentManager,"dialog")
            R.id.delete -> alertDeleteTODO()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun alertDeleteTODO() {
                alertMessage(
                    getString(R.string.deleteA),
                    getString(R.string.deleteB),
                    getString(R.string.deleteBtn),
                    getString(R.string.txtStop),
                    getString(R.string.cuidado)
                ){
                    if (it == "yes"){
                        deleteDB()
                    }else{
                        startActivity(Intent(this,ActivityMainModule::class.java))
                    }
                }.show(supportFragmentManager,"dialog")
    }

    private fun deleteDB() {
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivityMainModule).RecollectionDao().getFkIdCollectors()
            launch(Dispatchers.Main) {
                if (query.isEmpty()){
                    alertCountDown{
                        CoroutineScope(Dispatchers.IO).launch{
                            AppDataBase.getInstance(this@ActivityMainModule).RecollectionDao().delete()
                            AppDataBase.getInstance(this@ActivityMainModule).RecolectoresDao().delete()
                            AppDataBase.getInstance(this@ActivityMainModule).SettingDao().delete()
                            launch(Dispatchers.Main) {
                                startActivity(Intent(this@ActivityMainModule, ActivityMainModule::class.java))
                                customSnackBar.showCustomSnackBar(binding.textView, "")
                                alerts()
                            }
                        }
                    }.show(supportFragmentManager,"dialog")
                }else{
                    customSnackBar.showCustomSnackBar(binding.textView,getString(R.string.errorDeleteDates))
                }
            }
        }
    }

    private fun help(message: String) {
        alertHelp{
            try {
                val phone = "573170157414"
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_VIEW
                val uri = "whatsapp://send?phone=${phone}&text=${message}"
                sendIntent.data = Uri.parse(uri)
                startActivity(sendIntent)
            } catch (e: ActivityNotFoundException){
                alertMessage(
                    getString(R.string.install),
                    getString(R.string.message),
                    getString(R.string.playSore),
                    getString(R.string.ready),
                    getString(R.string.noWhatsApp)
                ){
                    if (it == "yes"){
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=whatsapp&c=apps&hl=es_419&gl=US"))
                        startActivity(intent)
                    }else{
                        startActivity(Intent(this,ActivityMainModule::class.java))
                    }
                }.show(supportFragmentManager,"dialog")
            }
        }.show(supportFragmentManager,"dialog")
    }

    private fun alerts(){
        preferencesAssistant()
        alertWelcome{
            alertSettings{
                insertSettings(it)
            }.show(supportFragmentManager, "dialog")
        }.show(supportFragmentManager,"dialog")
    }


    private fun insertSettings(settings: SettingEntity){
        preferences()
        CoroutineScope(Dispatchers.IO).launch{
            AppDataBase.getInstance(this@ActivityMainModule).SettingDao().insertConfig(settings)
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
        editor.putString("assistant","true")
        editor.apply()
    }

    private fun preferencesAssistant (){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("assistant","true")
        editor.apply()
    }


    private fun checkRegister(){
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivityMainModule).SettingDao().getAliment("yes")
            launch(Dispatchers.Main) {
                if(query.isNotEmpty()){
                    val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
                    val register = preferences.getString("register","")
                    if(register != "true"){
                        alerts()
                    }else{
                        CoroutineScope(Dispatchers.IO).launch{
                            val query1 = AppDataBase.getInstance(this@ActivityMainModule).SettingDao().getAliment("yes")
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
            val query = AppDataBase.getInstance(this@ActivityMainModule).SettingDao().getAliment("no")
            launch(Dispatchers.Main) {
                price.priceSplit(query[0].cost){
                    binding.tvNoAliment.text = it
                }
            }
        }
    }

    private fun getYesAliment(){
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivityMainModule).SettingDao().getAliment("yes")
            launch(Dispatchers.Main) {
                if(query.isNotEmpty()){
                    price.priceSplit(query[0].cost){
                        binding.tvYesAliment.text = it
                    }
                }
            }
        }
    }

    private fun checkCollection(){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val collection = preferences.getString("collection","")
        if(collection != "true"){
            alertMessage(
                "${binding.tvNoAliment.text}\n ${getString(R.string.notAliment)}",
                "${binding.tvYesAliment.text}\n ${getString(R.string.yesAliment)}",
                getString(R.string.btCorrec),
                getString(R.string.noCorrec),
                getString(R.string.checkAliment)
            ){
                if(it == "yes"){
                    alertAddRecolcetor()
                }else{
                    startActivity(Intent(this,ActivitySettings::class.java))
                }
            }.show(supportFragmentManager,"dialog")
        }else{
            startActivity(Intent(this,ActivityRecolection::class.java))
        }
    }

    private fun alertAddRecolcetor() {
        alertAddRecolector(
            {
                insertRecolector(it)
            },
            {
                if(it){
                    startActivity(Intent(this,ActivityRecolection::class.java))
                }else{
                    startActivity(Intent(this,ActivityMainModule::class.java))
                }
            }
        ).show(supportFragmentManager, "dialog")
    }

    private fun insertRecolector(recolector: RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityMainModule).RecolectoresDao().add(recolector)
        }
        preferencesCollecion()
    }

    private fun preferencesCollecion() {
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("collection","true")
        editor.apply()
    }

}
