package com.rojasdev.apprecconprojectPro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkRegister()
    }

    private fun checkRegister(){
        val preferences = getSharedPreferences( "logIn", Context.MODE_PRIVATE)
        val register = preferences.getString("user","")
        if(register != "true"){
            startActivity(Intent(this,ActivityLogin::class.java))
            finish()
        }else{
            startActivity(Intent(this,ActivityMainModule::class.java))
            finish()
        }
    }
}