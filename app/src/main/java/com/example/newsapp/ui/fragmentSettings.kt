package com.example.newsapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentDiscoveryBinding
import com.example.newsapp.databinding.FragmentSettingsBinding
import com.example.newsapp.utils.CustomArrayAdapter
import com.example.newsapp.utils.SharedPreferenceInstance


class fragmentSettings : Fragment() {

    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferenceInstance
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPreferenceInstance.getInstance(requireContext())
        val isNightMode = sharedPref.getBoolean("night", false)
        binding.switchTheme.isChecked = isNightMode // Set switch state based on saved preference

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            sharedPref.saveBoolean("night", isChecked)
        }

        val countryArray = resources.getStringArray(R.array.country)

        val countryCodes = mapOf(
            "Argentina" to "ar",
            "Australia" to "au",
            "Austria" to "at",
            "Belgium" to "be",
            "Brazil" to "br",
            "Bulgaria" to "bg",
            "Canada" to "ca",
            "China" to "cn",
            "Colombia" to "co",
            "Cuba" to "cu",
            "Czech Republic" to "cz",
            "Egypt" to "eg",
            "France" to "fr",
            "Germany" to "de",
            "Greece" to "gr",
            "Hong Kong" to "hk",
            "Hungary" to "hu",
            "India" to "in",
            "Indonesia" to "id",
            "Ireland" to "ie",
            "Israel" to "il",
            "Italy" to "it",
            "Japan" to "jp",
            "Latvia" to "lv",
            "Lithuania" to "lt",
            "Malaysia" to "my",
            "Mexico" to "mx",
            "Netherlands" to "nl",
            "New Zealand" to "nz",
            "Nigeria" to "ng",
            "Norway" to "no",
            "Philippines" to "ph",
            "Poland" to "pl",
            "Portugal" to "pt",
            "Romania" to "ro",
            "Russia" to "ru",
            "Saudi Arabia" to "sa",
            "Serbia" to "rs",
            "Singapore" to "sg",
            "Slovakia" to "sk",
            "South Africa" to "za",
            "South Korea" to "kr",
            "Sweden" to "se",
            "Switzerland" to "ch",
            "Taiwan" to "tw",
            "Thailand" to "th",
            "Turkey" to "tr",
            "Ukraine" to "ua",
            "United Arab Emirates" to "ae",
            "United Kingdom" to "gb",
            "United States" to "us",
            "Venezuela" to "ve"
        )

        val customList = countryArray.mapNotNull { countryName ->
            countryCodes[countryName]
        }
        val adapter = activity?.let{
            CustomArrayAdapter(it, R.layout.spinner_item_layout,countryArray.toList(), countryCodes)
        }


        binding.spinnerCountry.adapter = adapter

        val settingCountry = sharedPref.getString("country", "us")
        println("COUNTRY: $settingCountry")
        val defaultSelectedPosition = customList.indexOf(settingCountry)
        binding.spinnerCountry.setSelection(defaultSelectedPosition)

        binding.spinnerCountry.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountryCode = customList[position]
                sharedPref.saveString("country", selectedCountryCode)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}