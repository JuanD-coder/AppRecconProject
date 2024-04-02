package com.rojasdev.apprecconprojectPro.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import org.w3c.dom.Entity
import java.util.stream.Collector

object serviceCollector {
    fun insertCollector(fireStoreDB:FirebaseFirestore,Uid:String,dates:RecolectoresEntity): Task<Void> {
        return fireStoreDB.collection("/Finca/$Uid/Recolectores").document().set(dates)
    }

    fun deleteCollector(fireStoreDB:FirebaseFirestore,id:Int): Task<Void>? {
        val docId = getDocumenIdCollector(fireStoreDB,id)
        return if (docId.equals(null)){
            null
        }else{
            fireStoreDB.collection("/Finca/${getPerfil()}/Recolectores").document().delete()
        }
    }

    fun getDocumenIdCollector(fireStoreDB:FirebaseFirestore,idSqlite: Int): String? {
        var idFirebase : String?
        idFirebase = null
        fireStoreDB.collection("/Finca/${getPerfil()}/Recolectores").get().addOnSuccessListener { result ->
            for (document in result){
                val id = document.data["id"].toString().toInt()
                if (id == idSqlite){
                    idFirebase = document.id
                }
            }
        }
        return idFirebase
    }

    fun getPerfil():String{
        val mAuth = FirebaseAuth.getInstance()
        return mAuth.currentUser!!.uid
    }
}