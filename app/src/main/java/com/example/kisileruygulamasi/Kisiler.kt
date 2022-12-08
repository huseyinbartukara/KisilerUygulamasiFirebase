package com.example.kisileruygulamasi

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Kisiler (var kisi_id:String? = "", var kisi_ad:String? = "", var kisi_tel:String? = ""){
}