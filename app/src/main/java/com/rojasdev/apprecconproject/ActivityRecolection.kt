package com.rojasdev.apprecconproject

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.rojasdev.apprecconproject.alert.collection.alertAddRecolector
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.controller.controllerTheme
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionBinding
import com.rojasdev.apprecconproject.fragments.collection.FragmentCollectors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityRecolection : AppCompatActivity() {
    private lateinit var binding: ActivityRecolectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adsBanner.initLoadAds(binding.banner)

        initFragmentCollectors()
        appearNavBar()

        binding.floatingActionButton.setOnClickListener {
            initAlertAddRecolcetor()
        }

        // binding.bottonNavigationViewCollectors.inflateMenu(R.menu.nav_menu_collectors)

        controllerTheme.main(
            this,
            day = {
                binding.floatingActionButton.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Cinnabar))
            },
            night = {
                binding.floatingActionButton.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Thunderbird))
            }
        )

        binding.floatingActionButton.invalidate()
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ViewPagerCollectors, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /*private fun hideNavBar() {
        // onClickFalse()
        binding.floatingActionButton.visibility = View.GONE
    }*/

    private fun appearNavBar() {
        // onClickTrue()
        binding.floatingActionButton.visibility = View.VISIBLE
    }

    /*private fun onClickFalse() {
        binding.bottonNavigationViewCollectors.setOnNavigationItemSelectedListener { meniItem ->
            when (meniItem.itemId) {
                R.id.collectors -> {
                    false
                }

                R.id.collection -> {
                    false
                }

                else -> false
            }
        }
    }

    private fun onClickTrue() {
        binding.bottonNavigationViewCollectors.setOnNavigationItemSelectedListener { meniItem ->
            when (meniItem.itemId) {
                R.id.collectors -> {
                    initFragmentCollectors()
                    true
                }

                R.id.collection -> {
                    initFragmentCollection()
                    true
                }

                else -> false
            }
        }
    }*/

    private fun initFragmentCollectors() {
        title = getString(R.string.collectors)
        openFragment(
            FragmentCollectors
            /*{
            if (it == "down") {
                hideNavBar()
            } else if (it == "up") {
                appearNavBar()
            }
        },*/ {
                preferencesCollecion()
            })
    }

    /*private fun initFragmentCollection() {
        title = getString(R.string.collection)
        openFragment(
            FragmentCollection(
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

    }*/

    private fun initAlertAddRecolcetor() {
        alertAddRecolector(
            style = false,
            {
                insertRecolector(it)
            },
            {
                initFragmentCollectors()
            },
            resetNextTemporalCollectorId = false
        ).show(supportFragmentManager, "dialog")
    }

    private fun insertRecolector(recolector: RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityRecolection).RecolectoresDao().add(recolector)
        }
    }

    private fun preferencesCollecion() {
        val preferences = getSharedPreferences("register", Context.MODE_PRIVATE)
        preferences.edit {
            putString("collection", "false")
        }
    }


}