package com.rojasdev.apprecconprojectPro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.rojasdev.apprecconprojectPro.alert.alertRefactorPassword
import com.rojasdev.apprecconprojectPro.controller.connected
import com.rojasdev.apprecconprojectPro.controller.customSnackBar
import com.rojasdev.apprecconprojectPro.controller.requireInput
import com.rojasdev.apprecconprojectPro.data.dataBase.AppDataBase
import com.rojasdev.apprecconprojectPro.data.entities.FincaEntity
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecollectionEntity
import com.rojasdev.apprecconprojectPro.data.entities.SettingEntity
import com.rojasdev.apprecconprojectPro.databinding.ActivityLoginBinding
import com.rojasdev.apprecconprojectPro.fragments.FragmentRegisterUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActivityLogin : AppCompatActivity() {
    val firestore = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseAuth: FirebaseAuth
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        firebaseAnalytics = Firebase.analytics
        firebaseAuth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

        this.onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finishAndRemoveTask()
                }
            })

    }

    private fun initView() {
        val miList = listOf(
            binding.hitCorreo,
            binding.hitPassword
        )

        binding.btnLoginAccess.setOnClickListener {
            if(requireInput.validate(miList,this)){
                if (connected.isConnected(this)){
                    setup()
                }else{
                    customSnackBar.showCustomSnackBar(binding.imageView,getString(R.string.noConnected))
                }
            }
        }

        binding.btnRegisterUser.setOnClickListener {
            binding.cvWelcome.visibility = View.GONE
            binding.ViewFragmentRegister.visibility = View.VISIBLE
            openFragment(FragmentRegisterUser())
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ViewFragmentRegister, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setup() {
        showProgress()
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(binding.hitCorreo.text.toString(),
                binding.hitPassword.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    getFinca()
                }else{
                    goneProgress()
                    showRefactorPassword()
                    customSnackBar.showCustomSnackBar(binding.imageView,getString(R.string.errorInit))
                }
        }
    }

    private fun getFinca() {
        showProgress()
        val auth = FirebaseAuth.getInstance()

        // Obtener el ID de usuario
        val uid = auth.currentUser?.uid

        // Consultar el documento del usuario
        firestore.collection("users").document(uid!!).get().addOnSuccessListener {
            // El documento del usuario se ha obtenido correctamente
            val documentId = it.id

            firestore.collection("Finca").document(documentId).get()
                .addOnSuccessListener {
                    val nameUser = it.get("userName")
                    insertFinca(nameUser.toString())
                    getSettings()
                }
        }
    }

    fun showProgress(){
       binding.progressBar.visibility = View.VISIBLE
       binding.tvLoading.visibility = View.VISIBLE
       binding.cvWelcome.visibility = View.GONE
    }

    fun goneProgress(){
        binding.progressBar.visibility = View.GONE
        binding.tvLoading.visibility = View.GONE
        binding.cvWelcome.visibility = View.VISIBLE
    }

    fun showRefactorPassword(){
        binding.btnNoPassword.visibility = View.VISIBLE
        binding.btnNoPassword.setOnClickListener {
            alertRefactorPassword{
                showProgress()
                mAuth.setLanguageCode("es")
                mAuth.sendPasswordResetEmail(it).addOnCompleteListener {task->
                    if (task.isSuccessful){
                        goneProgress()
                        customSnackBar.showCustomSnackBar(binding.imageView,getString(R.string.emailView))
                        goneRefactorPassword()
                    }else{
                        goneProgress()
                        customSnackBar.showCustomSnackBar(binding.imageView,"pailas")
                    }
                }
            }.show(supportFragmentManager,"dialog")
        }
    }

    private fun preferences (email:String){
        preferencesRegister()
        val preferences = getSharedPreferences( "logIn", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("user","true")
        editor.putString("email",email)
        editor.apply()
    }

    private fun preferencesRegister (){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("register","true")
        editor.putString("collection","false")
        editor.apply()
    }
    fun goneRefactorPassword(){
        binding.btnNoPassword.visibility = View.GONE
    }

    fun getLotes() {
        val auth = FirebaseAuth.getInstance()

        // Obtener el ID de usuario
        val uid = auth.currentUser?.uid

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("/Finca/$uid/Lotes")

        // Realiza la consulta
        val snapshot = collectionRef.get()
        snapshot.addOnCompleteListener {
        val documents = snapshot.result.toList()

        // Accede a los datos
         val lotes = mutableListOf<LoteEntity>()

         for (doc in documents) {

             val dates = LoteEntity(
                 doc.data["id"].toString().toInt(),
                 doc.data["name"].toString(),
                 doc.data["type"].toString(),
                 true,
                 doc.data["finca"].toString().toInt()
            )

        lotes.add(dates)
    }
    insertLotes(lotes)
    getCollectors()
}


    }

    private fun getSettings() {
        val auth = FirebaseAuth.getInstance()

        // Obtener el ID de usuario
        val uid = auth.currentUser?.uid

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("/Finca/$uid/Settings")

        val snapshot = collectionRef.get()
        snapshot.addOnCompleteListener {
            val documents = snapshot.result.toList()

            // Accede a los datos
            val settings = mutableListOf<SettingEntity>()

            for (doc in documents) {

                val dates = SettingEntity(
                    doc.data["id"].toString().toInt(),
                    doc.data["aliment"].toString(),
                    doc.data["price"].toString().toInt(),
                    doc.data["state"].toString(),
                    doc.data["date"].toString()
                )
                settings.add(dates)
            }
            insertSettings(settings)

            getLotes()

        }
    }

    private fun insertLotes(dates: List<LoteEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityLogin).LoteDao().addM(dates)
        }
    }

    private fun insertSettings(dates: List<SettingEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityLogin).SettingDao().InsertconfigNew(dates)
        }
    }

    private fun insertFinca(finca: String) {
        val mFinca = FincaEntity(
            null,
            finca
        )
        CoroutineScope(Dispatchers.IO).launch{
            AppDataBase.getInstance(this@ActivityLogin).FincaDao().add(mFinca)
        }
        preferences(binding.hitCorreo.text.toString())
        goneProgress()
        startActivity(Intent(this,ActivityMainModule::class.java))
    }

    private fun getCollectors() {
        val auth = FirebaseAuth.getInstance()

        // Obtener el ID de usuario
        val uid = auth.currentUser?.uid

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("/Finca/$uid/Recolectores")

        val snapshot = collectionRef.get()
        snapshot.addOnCompleteListener {
            val documents = snapshot.result.toList()

            if (documents.isNotEmpty()){
                // Accede a los datos
                val collectors = mutableListOf<RecolectoresEntity>()

                for (doc in documents) {

                    val dates = RecolectoresEntity(
                        doc.data["id"].toString().toInt(),
                        doc.data["name"].toString(),
                        doc.data["state"].toString(),
                        true
                    )
                    collectors.add(dates)
                }
                insertRecolector(collectors)
                getCollection()
            }
        }
    }

    private fun getCollection() {
        val auth = FirebaseAuth.getInstance()

        // Obtener el ID de usuario
        val uid = auth.currentUser?.uid

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("/Finca/$uid/Recoleccion")

        val snapshot = collectionRef.get()
        snapshot.addOnCompleteListener {
            val documents = snapshot.result.toList()

            if (documents.isNotEmpty()){
                // Accede a los datos
                val recolection = mutableListOf<RecollectionEntity>()

                for (doc in documents) {

                    val dates = RecollectionEntity(
                        doc.data["id"].toString().toInt(),
                        doc.data["cantidad"].toString().toDouble(),
                        doc.data["date"].toString(),
                        doc.data["state"].toString(),
                        doc.data["recolector"].toString().toInt(),
                        doc.data["configuracion"].toString().toInt(),
                        doc.data["lote"].toString().toInt(),
                        true
                    )
                    recolection.add(dates)
                }
                insertRecolection(recolection)
                goneProgress()
            }
        }
    }

    private fun insertRecolection(recolection: List<RecollectionEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityLogin).RecollectionDao().addM(recolection)
        }
    }

    private fun insertRecolector(recolector: List<RecolectoresEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityLogin).RecolectoresDao().addM(recolector)
        }

    }
}