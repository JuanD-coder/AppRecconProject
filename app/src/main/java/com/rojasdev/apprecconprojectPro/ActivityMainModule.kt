package com.rojasdev.apprecconprojectPro

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.rojasdev.apprecconprojectPro.alert.alertAddLotes
import com.rojasdev.apprecconprojectPro.alert.alertAddRecolector
import com.rojasdev.apprecconprojectPro.alert.alertDatesFinca
import com.rojasdev.apprecconprojectPro.alert.alertSettings
import com.rojasdev.apprecconprojectPro.alert.alertWelcome
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.controller.connected
import com.rojasdev.apprecconprojectPro.controller.customSnackBar
import com.rojasdev.apprecconprojectPro.controller.price
import com.rojasdev.apprecconprojectPro.data.dataBase.AppDataBase
import com.rojasdev.apprecconprojectPro.data.entities.FincaEntity
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import com.rojasdev.apprecconprojectPro.data.entities.SettingEntity
import com.rojasdev.apprecconprojectPro.databinding.ActivityMainModuleBinding
import com.rojasdev.apprecconprojectPro.services.serviceCollector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityMainModule : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private val fireStoreDB = FirebaseFirestore.getInstance()
    val database = FirebaseDatabase.getInstance()
    lateinit var binding: ActivityMainModuleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainModuleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        verifyDatesFirebase()

        binding.cvDatesFirebase.setOnClickListener {
            initUpdateFirebase()
        }

        this.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })

        binding.cvAliment.setOnClickListener {
            startActivity(Intent(this,ActivitySettings::class.java))
        }

        binding.cvLotes.setOnClickListener {
            startActivity(Intent(this,ActivityLotes::class.java))
        }

        if(connected.isConnected(this)){
            checkRegister()
        }else{
            Toast.makeText(this, "sin internet", Toast.LENGTH_SHORT).show()
        }

        alertPerfil()

        binding.cvInformes.setOnClickListener {
            animatedAlert.animatedClick(binding.cvInformes)
            startActivity(Intent(this,ActivityInformes::class.java))
        }

        binding.cvCollection.setOnClickListener {
            animatedAlert.animatedClick(binding.cvCollection)
                checkCollection()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.support -> startActivity(Intent(this,ActivityLogin::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun alerts(){
        alertWelcome{
            alertSettings{
                insertSettings(it)
                alertAddLote()
            }.show(supportFragmentManager, "dialog")
        }.show(supportFragmentManager,"dialog")
    }

    private fun insertSettings(settings: List<SettingEntity>){
        preferences()
        CoroutineScope(Dispatchers.IO).launch{
            AppDataBase.getInstance(this@ActivityMainModule).SettingDao().InsertconfigNew(settings)
            launch {
                insertSettingsFirebse()
            }
        }
    }

    private fun updateFinca(newFinca: FincaEntity){
        CoroutineScope(Dispatchers.IO).launch{
            AppDataBase.getInstance(this@ActivityMainModule).FincaDao().update(newFinca.id,newFinca.name)
            launch(Dispatchers.Main) {
                getFinca()
                alertPerfil()
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

    private fun checkRegister(){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val register = preferences.getString("register","")
        if(register != "true"){
            alerts()
        }else{
            CoroutineScope(Dispatchers.IO).launch{
                val query = AppDataBase.getInstance(this@ActivityMainModule).SettingDao().getAliment("yes")
                launch(Dispatchers.Main) {
                    if(query.isNotEmpty()){
                        getYesAliment()
                    }else{
                        checkRegister()
                    }
                }
            }
        }
    }

    private fun getFinca(){
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivityMainModule).FincaDao().get()
            launch(Dispatchers.Main) {
                if (query.isNotEmpty()){
                    binding.tvNameFinca.text = query[0].name
                }
                getLotes()
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
                getFinca()
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
                    getNoAliment()
                }
            }
        }
    }

    private fun checkCollection(){
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivityMainModule).RecolectoresDao().getAllRecolector()
            launch(Dispatchers.Main) {
                if (query.isEmpty()){
                    alertAddRecolcetor()
                }else{
                    startActivity(Intent(this@ActivityMainModule,ActivityRecolection::class.java))
                }
            }
        }
    }

    private fun alertAddRecolcetor() {
        alertAddRecolector(
            {
                insertRecolector(it)
            },
            {
                if (it == true){
                    Toast.makeText(this, "si", Toast.LENGTH_SHORT).show()
                    insertCollestorsFirebase()
                }else{
                    Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()
                }
            }
        ).show(supportFragmentManager, "dialog")
    }

    private fun insertCollestorsFirebase() {
        showProgress()
        CoroutineScope(Dispatchers.IO).launch {
            val recolectores = AppDataBase.getInstance(this@ActivityMainModule).RecolectoresDao().getAllRecolectorFirebase(false)
            launch(Dispatchers.Main) {
                for (recolector in recolectores){
                    val userId = mAuth.currentUser!!.uid

                    val userData = hashMapOf(
                        "id" to  recolector.id,
                        "name" to recolector.name,
                        "state" to recolector.state,
                        "firebase" to true
                    )

                    serviceCollector.insertCollector(fireStoreDB,userId,recolector)
                        .addOnSuccessListener {
                            goneProgress()
                            startActivity(Intent(this@ActivityMainModule,ActivityRecolection::class.java))
                            updateStateCollector(recolector.id!!)
                        }
                }
            }
        }
    }

    private fun updateStateCollector(id:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityMainModule).RecolectoresDao().updateCollectorStateFirebase(id,true)
        }
    }

    private fun insertRecolector(recolector: RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityMainModule).RecolectoresDao().add(recolector)
        }
        preferencesCollecion()
        Toast.makeText(this@ActivityMainModule,  "${getString(R.string.btnAddRecolector)} ${recolector.name}", Toast.LENGTH_SHORT).show()
    }

    private fun insertLote(lote: LoteEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityMainModule).LoteDao().add(lote)
        }
    }

    private fun preferencesCollecion() {
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("collection","true")
        editor.apply()
    }

    private fun alertAddLote() {
        CoroutineScope(Dispatchers.IO).launch {
            val finca = AppDataBase.getInstance(this@ActivityMainModule).FincaDao().getId()
            launch(Dispatchers.Main) {
                alertAddLotes(
                    finca,
                    {
                        insertLote(it)
                    },
                    {
                        insertLoteFireBase()
                    }
                ).show(supportFragmentManager, "dialog")
            }
        }
    }

    private fun insertLoteFireBase() {
        showProgress()
        CoroutineScope(Dispatchers.IO).launch {
            val lotes = AppDataBase.getInstance(this@ActivityMainModule).LoteDao().get()
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
                            goneProgress()
                            startActivity(Intent(this@ActivityMainModule,ActivityMainModule::class.java))
                        }
                }
            }
        }
    }

    private fun insertSettingsFirebse() {
        CoroutineScope(Dispatchers.IO).launch {
            val settings = AppDataBase.getInstance(this@ActivityMainModule).SettingDao().getAlimentTo()
            launch(Dispatchers.Main) {
                for (setting in settings){
                    val userId = mAuth.currentUser!!.uid

                    val data = hashMapOf(
                        "id" to setting.Id,
                        "price" to setting.cost,
                        "aliment" to setting.feeding,
                        "state" to setting.status,
                        "date" to setting.date
                    )

                    fireStoreDB.collection("/Finca/$userId/Settings").document()
                        .set(data)
                        .addOnSuccessListener {

                        }
                }
            }
        }

    }

    private fun getLotes(){
        CoroutineScope(Dispatchers.IO).launch {
            val lotes = AppDataBase.getInstance(this@ActivityMainModule).LoteDao().get()
            launch(Dispatchers.Main) {
                if(lotes.isEmpty()){
                    checkRegister()
                }else{
                    if(lotes.size >= 4){
                        binding.tvLote.text = lotes[0].name
                        binding.tvLote1.text = lotes[1].name
                        binding.tvLote2.text = lotes[2].name
                        binding.tvLote3.text = lotes[3].name
                    }else if (lotes.size == 3){
                        binding.tvLote.text = lotes[0].name
                        binding.tvLote1.text = lotes[1].name
                        binding.tvLote2.text = lotes[2].name
                    }else if (lotes.size == 2){
                        binding.tvLote.text = lotes[0].name
                        binding.tvLote1.text = lotes[1].name
                    } else{
                        binding.tvLote.text = lotes[0].name
                    }
                }
            }
        }
    }

    private fun alertPerfil(){
        CoroutineScope(Dispatchers.IO).launch {
            val finca = AppDataBase.getInstance(this@ActivityMainModule).FincaDao().get()
            launch(Dispatchers.Main) {
                binding.lyFinca.setOnClickListener {
                    alertDatesFinca(
                        finca
                    ){
                      updateFinca(it)
                    }.show(supportFragmentManager,"dialog")
                }
            }
        }
    }

    private fun insertColletionFirebase() {
        showProgress()
        CoroutineScope(Dispatchers.IO).launch {
            val collection = AppDataBase.getInstance(this@ActivityMainModule).RecollectionDao().getAllCollectionFirebase(false)
            launch(Dispatchers.Main) {
                for (itCollection in collection){
                    val userId = mAuth.currentUser!!.uid

                    val userData = hashMapOf(
                        "id" to  itCollection.ID,
                        "cantidad" to itCollection.total,
                        "fecha" to itCollection.date,
                        "state" to itCollection.state,
                        "recolector" to itCollection.collector,
                        "configuracion" to itCollection.setting,
                        "lote" to itCollection.lote,
                        "firebase" to true
                    )

                    fireStoreDB.collection("/Finca/$userId/Recoleccion").document()
                        .set(userData)
                        .addOnSuccessListener {
                            goneProgress()
                            startActivity(Intent(this@ActivityMainModule, ActivityRecolection::class.java))
                            updateStateCollection(itCollection.ID!!)
                        }
                }
            }
        }
    }

    private fun updateStateCollection(id:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityMainModule).RecollectionDao().updateCollectionStateFirebase(id,true)
            launch(Dispatchers.Main) {
                goneProgress()
            }
        }
    }

private fun verifyDatesFirebase(){
    CoroutineScope(Dispatchers.IO).launch {
        val stateFirebaseCollector = AppDataBase.getInstance(this@ActivityMainModule).RecolectoresDao().getCollectorStateFirebase()
        launch(Dispatchers.Main){
            if (stateFirebaseCollector.isNotEmpty()){
                animatedAlert.animatedNoDates(binding.cvDatesFirebase)
                binding.cvDatesFirebase.visibility = View.VISIBLE
            }else{
                launch(Dispatchers.IO) {
                    val stateFirebaseCollection = AppDataBase.getInstance(this@ActivityMainModule).RecollectionDao().getCollectionStateFirebase()
                    launch(Dispatchers.Main) {
                        if (stateFirebaseCollection.isNotEmpty()){
                            if (connected.isConnected(this@ActivityMainModule)){
                                animatedAlert.animatedNoDates(binding.cvDatesFirebase)
                                binding.cvDatesFirebase.visibility = View.VISIBLE
                                initUpdateFirebase()
                            }
                        }else{
                            binding.cvDatesFirebase.visibility = View.GONE
                        }
                    }
                }

            }
        }
    }
}

    private fun initUpdateFirebase() {
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
        showProgress()
        insertCollestorsFirebase()
        insertColletionFirebase()
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