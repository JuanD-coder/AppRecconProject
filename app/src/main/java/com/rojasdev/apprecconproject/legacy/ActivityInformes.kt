package com.rojasdev.apprecconproject.legacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rojasdev.apprecconproject.databinding.ActivityInformesBinding

class ActivityInformes : androidx.appcompat.app.AppCompatActivity() {

    lateinit var binding: ActivityInformesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInformesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(_root_ide_package_.com.rojasdev.apprecconproject.R.string.calendarCollection)
        openFragment(_root_ide_package_.com.rojasdev.apprecconproject.legacy.fragments.report.FragmentWorkReport())

        _root_ide_package_.com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)
        //configuracion de la barra de navigation
        binding.bottonNavigationView.setOnNavigationItemSelectedListener {
            meniItem ->
            when(meniItem.itemId){
                _root_ide_package_.com.rojasdev.apprecconproject.R.id.list ->{
                    title = getString(_root_ide_package_.com.rojasdev.apprecconproject.R.string.calendarCollection)
                    openFragment(_root_ide_package_.com.rojasdev.apprecconproject.legacy.fragments.report.FragmentWorkReport())
                    true
                }
                _root_ide_package_.com.rojasdev.apprecconproject.R.id.pdf ->{
                    title = getString(_root_ide_package_.com.rojasdev.apprecconproject.R.string.informedTitlePdf)
                    openFragment(_root_ide_package_.com.rojasdev.apprecconproject.legacy.fragments.report.FragmentPdf())
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(_root_ide_package_.com.rojasdev.apprecconproject.R.id.ViewPager, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}