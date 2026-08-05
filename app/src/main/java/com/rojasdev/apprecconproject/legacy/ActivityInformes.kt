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

        title = getString(com.rojasdev.apprecconproject.R.string.calendarCollection)
        openFragment(com.rojasdev.apprecconproject.legacy.fragments.report.FragmentWorkReport())

        com.rojasdev.apprecconproject.legacy.controller.adsBanner.initLoadAds(binding.banner)
        //configuracion de la barra de navigation
        binding.bottonNavigationView.setOnNavigationItemSelectedListener {
            meniItem ->
            when(meniItem.itemId){
                com.rojasdev.apprecconproject.R.id.list ->{
                    title = getString(com.rojasdev.apprecconproject.R.string.calendarCollection)
                    openFragment(com.rojasdev.apprecconproject.legacy.fragments.report.FragmentWorkReport())
                    true
                }
                com.rojasdev.apprecconproject.R.id.pdf ->{
                    title = getString(com.rojasdev.apprecconproject.R.string.informedTitlePdf)
                    openFragment(com.rojasdev.apprecconproject.legacy.fragments.report.FragmentPdf())
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(com.rojasdev.apprecconproject.R.id.ViewPager, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}