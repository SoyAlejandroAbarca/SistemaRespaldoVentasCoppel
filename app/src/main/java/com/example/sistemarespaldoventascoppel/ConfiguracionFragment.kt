package com.example.sistemarespaldoventascoppel

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class ConfiguracionFragment : Fragment(R.layout.fragment_configuracion) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegresar = view.findViewById<ImageButton>(R.id.btnRegresarConfig)
        val switchDarkMode = view.findViewById<Switch>(R.id.switchDarkMode)

        btnRegresar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Toast.makeText(requireContext(), "Modo Oscuro activado", Toast.LENGTH_SHORT).show()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Toast.makeText(requireContext(), "Modo Claro activado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
