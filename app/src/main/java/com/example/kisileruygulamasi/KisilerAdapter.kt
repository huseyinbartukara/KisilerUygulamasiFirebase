package com.example.kisileruygulamasi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.kisi_card_tasarim.view.*

class KisilerAdapter(private val mContext:Context,private val refKisiler:DatabaseReference, private var kisilerListe:ArrayList<Kisiler>) : RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu>() {



    inner class CardTasarimTutucu(tasarim:View) : RecyclerView.ViewHolder(tasarim){

        var textViewKisiBilgi:TextView
        var imageViewNokta:ImageView

        init {
            textViewKisiBilgi = tasarim.findViewById(R.id.textViewKisiBilgi)
            imageViewNokta = tasarim.findViewById(R.id.imageViewNokta)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.kisi_card_tasarim,parent,false)
        return CardTasarimTutucu(tasarim)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val kisi = kisilerListe.get(position)

        holder.textViewKisiBilgi.text = "${kisi.kisi_ad} - ${kisi.kisi_tel}"

        holder.imageViewNokta.setOnClickListener {

            val popupMenu = PopupMenu(mContext,holder.imageViewNokta)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->

                when(menuItem.itemId){
                    R.id.action_sil ->{
                        Snackbar.make(holder.imageViewNokta,"${kisi.kisi_ad} Silinsin mi?",Snackbar.LENGTH_SHORT)
                            .setAction("EVET"){
                                refKisiler.child(kisi.kisi_id!!).removeValue()
                            }.show()
                        true
                    }

                    R.id.action_duzenle ->{
                        alertGoster(kisi)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()


        }

    }

    override fun getItemCount(): Int {
        return kisilerListe.size
    }

    fun alertGoster(kisi:Kisiler){
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.alert_tasarim,null)
        val editTextAd = tasarim.findViewById(R.id.editTextAd) as EditText
        val editTextTel = tasarim.findViewById(R.id.editTextTel) as EditText

        editTextAd.setText(kisi.kisi_ad)
        editTextTel.setText(kisi.kisi_tel)

        val ad = AlertDialog.Builder(mContext)

        ad.setTitle("Kişi Düzenle")
        ad.setView(tasarim)
        ad.setPositiveButton("Düzenle"){dialogInterface, i ->
            val kisi_ad = editTextAd.text.toString().trim()
            val kisi_tel = editTextTel.text.toString().trim()

            val bilgiler = HashMap<String,Any>()

            bilgiler.put("kisi_ad",kisi_ad)
            bilgiler.put("kisi_tel",kisi_tel)

            refKisiler.child(kisi.kisi_id!!).updateChildren(bilgiler)


            Toast.makeText(mContext,"$kisi_ad - $kisi_tel", Toast.LENGTH_SHORT).show()

        }

        ad.setNegativeButton("İptal"){dialogInterface, i ->

        }
        ad.create().show()
    }


}