package com.example.sistemarespaldoventascoppel

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Referencias a los TextViews de bienvenida y fecha
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreEmpleado)
        val tvNumEmpleado = view.findViewById<TextView>(R.id.tvNumEmpleadoHome)
        val tvFechaHora = view.findViewById<TextView>(R.id.tvFechaHora)

        // Recuperamos los datos del usuario
        val nombreRecibido = arguments?.getString("nombre_clave")
        val numRecibido = arguments?.getString("num_empleado_clave")

        if (nombreRecibido != null) tvNombre.text = nombreRecibido
        if (numRecibido != null) tvNumEmpleado.text = numRecibido

        // Mostrar Fecha y Hora de ingreso actual
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDateTime = sdf.format(Date())
        tvFechaHora.text = "Ingreso: $currentDateTime"

        // 2. Referencias a los Botones de Funciones
        val btnRegistrarVenta = view.findViewById<Button>(R.id.btnRegistrarVenta)
        val btnConsultarVentas = view.findViewById<Button>(R.id.btnConsultarVentas)
        val btnEstadistica = view.findViewById<Button>(R.id.btnEstadistica)
        val btnMiCuenta = view.findViewById<Button>(R.id.btnMiCuenta)

        // --- Configuración de Eventos (Listeners) ---

        btnRegistrarVenta.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, RegistrarVentaFragment())
                .addToBackStack(null)
                .commit()
        }

        btnConsultarVentas.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, ConsultarVentasFragment())
                .addToBackStack(null)
                .commit()
        }

        btnEstadistica.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, EstadisticaFragment())
                .addToBackStack(null)
                .commit()
        }

        btnMiCuenta.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, PerfilFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
