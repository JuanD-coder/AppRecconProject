package com.rojasdev.apprecconproject.legacy

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityWork : androidx.appcompat.app.AppCompatActivity() {
    private lateinit var binding: ActivityRecolectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initFragmentCollectors()
        appearNavBar()
        com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)

        binding.bottonNavigationViewCollectors.inflateMenu(com.rojasdev.apprecconproject.R.menu.menu_work)
        binding.floatingActionButton.invalidate()

        com.rojasdev.apprecconproject.legacy.controller.controllerTheme.main(
            this,
            day = {
                binding.floatingActionButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, com.rojasdev.apprecconproject.R.color.Orange))
            },
            night = {
                binding.floatingActionButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, com.rojasdev.apprecconproject.R.color.OrangeDark))
            }
        )

        binding.floatingActionButton.setOnClickListener {
            initAlertAddRecolcetor()
        }
    }


    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(com.rojasdev.apprecconproject.R.id.ViewPagerCollectors, fragment)
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
                com.rojasdev.apprecconproject.R.id.collectors ->{
                    false
                }
                com.rojasdev.apprecconproject.R.id.collection ->{
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
                com.rojasdev.apprecconproject.R.id.collectors ->{
                    initFragmentCollectors()
                    true
                }
                com.rojasdev.apprecconproject.R.id.collection ->{
                    initFragmentCollection()
                    true
                }
                else -> false
            }
        }
    }


    private fun initFragmentCollectors() {
        title = getString(com.rojasdev.apprecconproject.R.string.workMen)
        openFragment(
            com.rojasdev.apprecconproject.legacy.fragments.work.FragmentWork(
                {
                if (it == "down") {
                    hideNavBar()
                } else if (it == "up") {
                    appearNavBar()
                }
            }, {
                preferencesCollecion()
            }
        ))
    }

    private fun initFragmentCollection() {
        title = getString(com.rojasdev.apprecconproject.R.string.work)
        openFragment(
            com.rojasdev.apprecconproject.legacy.fragments.work.FragmentWorkCancelet(
                {
                    if (it == "down") {
                        hideNavBar()
                    } else if (it == "up") {
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
        com.rojasdev.apprecconproject.legacy.alert.collection.alertAddRecolector(
            true,
            {
                val newMen =
                    com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity(
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

    private fun insertRecolector(recolector: com.rojasdev.apprecconproject.legacy.data.entities.RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase.Companion.getInstance(this@ActivityWork).RecolectoresDao().add(recolector)
        }
    }

    private fun preferencesCollecion() {
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("workMen","false")
        editor.apply()
    }

}