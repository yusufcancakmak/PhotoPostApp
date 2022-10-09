package com.yusufcancakmak.fotografpaylasmaapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.DateValidatorPointBackward.now
import com.google.firebase.Timestamp.now
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_fotograf_paylasma.*
import java.sql.Timestamp
import java.time.Instant.now
import java.time.LocalDate.now
import java.time.LocalDateTime.now
import java.time.MonthDay.now
import java.time.YearMonth.now
import java.util.UUID

class FotografPaylasmaActivity : AppCompatActivity() {
    var secilenGorsel : Uri? = null
    var secilenBitmap :Bitmap? =null
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database :FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)

        storage =FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


        button3.setOnClickListener(View.OnClickListener {
            //Paylas butonu
            //depo işlemleri
            //UUID -> UNİVERSAL UNİQE İD
            val uuid =UUID.randomUUID()
            val gorselIsmi ="${uuid}.jpg"
            val reference =storage.reference

            val gorselReference = reference.child("images").child(gorselIsmi)
            if (secilenGorsel !=null){
                gorselReference.putFile(secilenGorsel!!).addOnSuccessListener {
                    val yuklenenGorslReferance = FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)
                    yuklenenGorslReferance.downloadUrl.addOnSuccessListener {
                        val dowloadUrl =it.toString()
                        val guncelkullaniciEmaili=auth.currentUser!!.email.toString()
                        val kullaniciYorumu =yorumtext.text.toString()
                        val tarih =com.google.firebase.Timestamp.now()
                        // Veritabani işlemlerii

                        val postHashMap = hashMapOf<String,Any>()
                        postHashMap.put("gorselurl",dowloadUrl)
                        postHashMap.put("kullaniciemail",guncelkullaniciEmaili)
                        postHashMap.put("kullaniciyorum",kullaniciYorumu)
                        postHashMap.put("tarih",tarih)

                        database.collection("Post").add(postHashMap).addOnCompleteListener {
                            if (it.isSuccessful){
                                finish()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }


                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()

                    }
                }

            }


        })


        imageView.setOnClickListener(View.OnClickListener {
            //fotograf seç
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                //izin almışız
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }else{
                // izin zaten varsa
                val galeriIntent =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }


        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==1){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // izin verilince yaplacakalr
                val galeriIntent =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode ==2 && resultCode== Activity.RESULT_OK && data !=null){
           secilenGorsel= data.data
            if (secilenGorsel != null){
                if (Build.VERSION.SDK_INT >=28){
                    val source =ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap =ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(secilenBitmap)
                }else{
                    secilenBitmap =MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    imageView.setImageBitmap(secilenBitmap)
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    }
