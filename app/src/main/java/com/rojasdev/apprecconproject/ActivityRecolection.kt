package com.rojasdev.apprecconproject

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.rojasdev.apprecconproject.alert.alertAddRecolector
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionBinding
import com.rojasdev.apprecconproject.fragments.FragmentCollecion
import com.rojasdev.apprecconproject.fragments.FragmentCollectors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityRecolection : AppCompatActivity() {
    private lateinit var binding: ActivityRecolectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
          setContentView(binding.root)

        initFragmentCollectors()
        appearNavBar()

        binding.floatingActionButton.setOnClickListener {
            initAlertAddRecolcetor()
        }

    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ViewPagerCollectors, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun hideNavBar(){
        onClickFalse()
        binding.bottomAppBarCollectors.visibility = View.GONE
        binding.bottonNavigationViewCollectors.visibility = View.GONE
        binding.floatingActionButton.visibility = View.GONE
    }

    private fun appearNavBar(){
        onClickTrue()
        binding.bottomAppBarCollectors.visibility = View.VISIBLE
        binding.bottonNavigationViewCollectors.visibility = View.VISIBLE
        binding.floatingActionButton.visibility = View.VISIBLE
    }

    private fun onClickFalse(){
        binding.bottonNavigationViewCollectors.setOnNavigationItemSelectedListener {
                meniItem ->
            when(meniItem.itemId){
                R.id.collectors ->{
                    false
                }
                R.id.collection ->{
                    false
                }
                else -> false
            }
        }
    }
    private fun onClickTrue(){
        binding.bottonNavigationViewCollectors.setOnNavigationItemSelectedListener {
                meniItem ->
            when(meniItem.itemId){
                R.id.collectors ->{
                    initFragmentCollectors()
                    true
                }
                R.id.collection ->{
                 initFragmentCollection()
                    true
                }
                else -> false
            }
        }
    }

    private fun initFragmentCollectors() {
        title = "Recolectores"
        openFragment(FragmentCollectors(
            {
                if (it == "down"){
                    hideNavBar()
                }else if (it == "up"){
                    appearNavBar()
                }
            },{
                preferencesCollecion()
            }
        ))
    }

    private fun initFragmentCollection() {
         title = "Recoleccion"
        openFragment(
            FragmentCollecion(
                {
                    if (it == "down"){
                        hideNavBar()
                    }else if (it == "up"){
                        appearNavBar()
                    }
                },
                {
                    preferencesCollecion()
                }
            )
        )

    }

    private fun initAlertAddRecolcetor() {

        alertAddRecolector(
            {
                insertRecolector(it)
            },
            {
                initFragmentCollectors()
            }
        ).show(supportFragmentManager, "dialog")
    }

    private fun insertRecolector(recolector: RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityRecolection).RecolectoresDao().add(recolector)
        }
    }

    private fun preferencesCollecion() {
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("collection","false")
        editor.apply()
    }
}