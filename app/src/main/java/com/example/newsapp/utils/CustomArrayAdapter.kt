package com.example.newsapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.newsapp.R

class CustomArrayAdapter(
    context: Context,
    private val layoutResource: Int,
    private val countryList: List<String>,
    private val countryCodes: Map<String, String>
) : ArrayAdapter<String>(context, layoutResource, countryList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)

        val countryFlagImageView = view.findViewById<ImageView>(R.id.countryFlagImageView)
        val countryTextView = view.findViewById<TextView>(R.id.tvCountry)

        val countryName = countryList[position]
        val countryCode = countryCodes[countryName]
        val flagResource = getCountryFlagResource(countryCode)

        countryFlagImageView.setImageResource(flagResource)
        countryTextView.text = countryName

        return view
    }

    private fun getCountryFlagResource(countryCode: String?): Int {
        return when (countryCode) {
            "ar" -> R.drawable.flag_ar
            "au" -> R.drawable.flag_au
            "at" -> R.drawable.flag_at
            "be" -> R.drawable.flag_be
            "br" -> R.drawable.flag_br
            "bg" -> R.drawable.flag_bg
            "ca" -> R.drawable.flag_ca
            "cn" -> R.drawable.flag_cn
            "co" -> R.drawable.flag_co
            "cu" -> R.drawable.flag_cu
            "cz" -> R.drawable.flag_cz
            "eg" -> R.drawable.flag_eg
            "fr" -> R.drawable.flag_fr
            "de" -> R.drawable.flag_de
            "gr" -> R.drawable.flag_gr
            "hk" -> R.drawable.flag_hk
            "hu" -> R.drawable.flag_hu
            "in" -> R.drawable.flag_in
            "id" -> R.drawable.flag_id
            "ie" -> R.drawable.flag_ie
            "il" -> R.drawable.flag_il
            "it" -> R.drawable.flag_it
            "jp" -> R.drawable.flag_jp
            "lv" -> R.drawable.flag_lv
            "lt" -> R.drawable.flag_lt
            "my" -> R.drawable.flag_my
            "mx" -> R.drawable.flag_mx
            "ma" -> R.drawable.flag_ma
            "nl" -> R.drawable.flag_nl
            "nz" -> R.drawable.flag_nz
            "ng" -> R.drawable.flag_ng
            "no" -> R.drawable.flag_no
            "ph" -> R.drawable.flag_ph
            "pl" -> R.drawable.flag_pl
            "pt" -> R.drawable.flag_pt
            "ro" -> R.drawable.flag_ro
            "ru" -> R.drawable.flag_ru
            "sa" -> R.drawable.flag_sa
            "rs" -> R.drawable.flag_rs
            "sg" -> R.drawable.flag_sg
            "sk" -> R.drawable.flag_sk
            "za" -> R.drawable.flag_za
            "kr" -> R.drawable.flag_kr
            "se" -> R.drawable.flag_se
            "ch" -> R.drawable.flag_ch
            "tw" -> R.drawable.flag_tw
            "th" -> R.drawable.flag_th
            "tr" -> R.drawable.flag_tr
            "ua" -> R.drawable.flag_ua
            "ae" -> R.drawable.flag_ae
            "gb" -> R.drawable.flag_uk
            "us" -> R.drawable.flag_us
            "ve" -> R.drawable.flag_ve
            else -> R.drawable.ic_launcher_background // Default flag icon
        }
    }


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
