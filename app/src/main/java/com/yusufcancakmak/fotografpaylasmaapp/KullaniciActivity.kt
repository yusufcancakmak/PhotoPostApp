package com.yusufcancakmak.fotografpaylasmaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.handleCoroutineException
import org.checkerframework.common.returnsreceiver.qual.This

class KullaniciActivity : AppCompatActivity() {
    private lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth= FirebaseAuth.getInstance()

        val guncelkullanici =auth.currentUser
        if (guncelkullanici != null){
            val intent =Intent(this,HaberlerActivity::class.java)
            startActivity(intent)
            finish()
        }

        // giriş yap
    button.setOnClickListener(View.OnClickListener {
    auth.signInWithEmailAndPassword(emailtext.text.toString(),passwordtext.text.toString()).addOnCompleteListener { task ->
        if (task.isSuccessful){

            val guncelkullanici = auth.currentUser?.email.toString()
            Toast.makeText(applicationContext,"Hoşgeldin: " +guncelkullanici,Toast.LENGTH_SHORT).show()

            val intent = Intent(this,HaberlerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }.addOnFailureListener { exception ->
        Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
    }
    })

        //kayıt ol
        button2.setOnClickListener(View.OnClickListener {
            val email =emailtext.text.toString()
            val sifre =passwordtext.text.toString()

            auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener { task ->
                //asenkron
                if (task.isSuccessful){
                val intent =Intent(this,HaberlerActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exeception  ->
                Toast.makeText(applicationContext,exeception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        })

    }
}