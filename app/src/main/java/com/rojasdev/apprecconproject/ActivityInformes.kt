package com.rojasdev.apprecconproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rojasdev.apprecconproject.controller.adsBanner
import com.rojasdev.apprecconproject.databinding.ActivityInformesBinding
import com.rojasdev.apprecconproject.fragments.report.FragmentExcelReport
import com.rojasdev.apprecconproject.fragments.report.FragmentWorkReport

class ActivityInformes : AppCompatActivity() {

    lateinit var binding: ActivityInformesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInformesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.calendarCollection)
        openFragment(FragmentWorkReport())

        adsBanner.initLoadAds(binding.banner)
        //configuracion de la barra de navigation
        binding.bottonNavigationView.setOnNavigationItemSelectedListener {
            meniItem ->
            when(meniItem.itemId){
                R.id.list ->{
                    title = getString(R.string.calendarCollection)
                    openFragment(FragmentWorkReport())
                    true
                }
                R.id.pdf ->{
                    title = getString(R.string.informedTitlePdf)
                    openFragment(FragmentExcelReport())
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ViewPager, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}