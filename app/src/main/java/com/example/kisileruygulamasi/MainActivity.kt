package com.example.kisileruygulamasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var kisilerListe:ArrayList<Kisiler>
    private lateinit var adapter: KisilerAdapter
    private lateinit var refKisiler : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "Kisiler Uygulaması"
        setSupportActionBar(toolbar)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this@MainActivity)

        val db = FirebaseDatabase.getInstance()
        refKisiler = db.getReference("kisiler")

        kisilerListe = ArrayList()

        adapter = KisilerAdapter(this,refKisiler,kisilerListe)

        rv.adapter = adapter

        tumKisiler()



        fab.setOnClickListener {
            alertGoster()
        }

    }




    fun alertGoster(){
        val tasarim = LayoutInflater.from(this).inflate(R.layout.alert_tasarim,null)
        val editTextAd = tasarim.findViewById(R.id.editTextAd) as EditText
        val editTextTel = tasarim.findViewById(R.id.editTextTel) as EditText

        val ad = AlertDialog.Builder(this)

        ad.setTitle("Kişi Ekle")
        ad.setView(tasarim)
        ad.setPositiveButton("Ekle"){dialogInterface, i ->
            val kisi_ad = editTextAd.text.toString().trim()
            val kisi_tel = editTextTel.text.toString().trim()

            val kisi = Kisiler("",kisi_ad,kisi_tel)

            refKisiler.push().setValue(kisi)

        }

        ad.setNegativeButton("İptal"){dialogInterface, i ->

        }
        ad.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)

        val item = menu?.findItem(R.id.action_ara)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onQueryTextSubmit(query: String): Boolean {
        aramaYap(query)
        Log.e("Gönderilen Arama",query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        aramaYap(newText)
        Log.e("Değişken Arama",newText)
        return true
    }




    fun aramaYap(aramaKelime:String){

        refKisiler.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(d: DataSnapshot) {
                kisilerListe.clear()
                for(c in d.children){
                    val kisi = c.getValue(Kisiler::class.java)

                    if(kisi != null){

                            if(kisi.kisi_ad!!.contains(aramaKelime)){
                                kisi.kisi_id = c.key
                                kisilerListe.add(kisi)
                            }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        adapter = KisilerAdapter(this@MainActivity,refKisiler,kisilerListe)

        rv.adapter = adapter
    }

    fun tumKisiler(){
        refKisiler.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(d: DataSnapshot) {
                kisilerListe.clear()
                for(c in d.children){
                    val kisi = c.getValue(Kisiler::class.java)

                    if(kisi != null){
                        kisi.kisi_id = c.key
                        kisilerListe.add(kisi)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}