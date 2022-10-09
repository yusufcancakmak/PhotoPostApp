package com.yusufcancakmak.fotografpaylasmaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_haberler.*

class HaberlerActivity : AppCompatActivity() {
    private lateinit var auth :FirebaseAuth
    private lateinit var database :FirebaseFirestore
    private lateinit var adapter :HaberRvAdapter

    var postListesi = ArrayList<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haberler)
        auth= FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        verilerial()
        var layoutManager =LinearLayoutManager(this)
        rv.layoutManager=layoutManager
        adapter =HaberRvAdapter(postListesi)
        rv.adapter=adapter
    }

    //menu ekleme fonksiyonu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater =menuInflater
        menuInflater.inflate(R.menu.secenekler_menusu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun verilerial(){
        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING)
            .addSnapshotListener{ snapshot, exception ->
            if (exception!= null){
                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_SHORT).show()
            }else{
                if (snapshot!= null){
                    if (snapshot.isEmpty==false){
                        val documents = snapshot.documents
                        postListesi.clear()
                        for (document in documents){
                         val kullaniciemail=   document.get("kullaniciemail") as String
                            val kullaniciYorum =document.get("kullaniciyorum") as String
                            val gorselurl =document.get("gorselurl") as String

                            val indirilenpost =Post(kullaniciemail,kullaniciYorum,gorselurl)
                            postListesi.add(indirilenpost)
                        }
                    adapter.notifyDataSetChanged()
                    }
                    }
                    }
                    }
                    }

    //itemarın idlerini yönlendirme
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.fotografpaylas){
            //fotograf paylaşma aktiviisne gidilecek

            val intent =Intent(this,FotografPaylasmaActivity::class.java)
            startActivity(intent)


        }else if(item.itemId==R.id.cıkısyap){
            auth.signOut()
            val intent =Intent(this,KullaniciActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}