package com.rojasdev.apprecconproject

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rojasdev.apprecconproject.alert.collection.alertAddRecolector
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.controllerTheme
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionBinding
import com.rojasdev.apprecconproject.fragments.collection.FragmentCollection
import com.rojasdev.apprecconproject.fragments.collection.FragmentCollectors
import com.rojasdev.apprecconproject.fragments.work.FragmentWork
import com.rojasdev.apprecconproject.fragments.work.FragmentWorkCancelet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityWork : AppCompatActivity() {
    private lateinit var binding: ActivityRecolectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initFragmentCollectors()
        appearNavBar()
        adsBanner.initLoadAds(binding.banner)

        binding.bottonNavigationViewCollectors.inflateMenu(R.menu.menu_work)
        binding.floatingActionButton.invalidate()

        controllerTheme.main(
            this,
            day = {
                binding.floatingActionButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Orange))
            },
            night = {
                binding.floatingActionButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.OrangeDark))
            }
        )

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
        title = getString(R.string.workMen)
        openFragment(FragmentWork(
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
        title = getString(R.string.work)
        openFragment(
            FragmentWorkCancelet(
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
            true,
            {
                val newMen = RecolectoresEntity(
                    null,
                    it.name,
                    "work-active"
                )
                insertRecolector(newMen)
            },
            {
                initFragmentCollectors()
            }
        ).show(supportFragmentManager, "dialog")
    }

    private fun insertRecolector(recolector: RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityWork).RecolectoresDao().add(recolector)
        }
    }

    private fun preferencesCollecion() {
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("workMen","false")
        editor.apply()
    }

}