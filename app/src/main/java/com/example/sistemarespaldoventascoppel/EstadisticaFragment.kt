package com.example.sistemarespaldoventascoppel

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EstadisticaFragment : Fragment(R.layout.fragment_estadistica) {

    private lateinit var repository: VentasRepository
    private var totalVendido = 0.0
    private val format = NumberFormat.getCurrencyInstance(Locale("es", "MX"))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = VentasRepository(requireContext())

        val etMeta = view.findViewById<EditText>(R.id.etMetaMensual)
        val pbProgreso = view.findViewById<ProgressBar>(R.id.pbProgresoMeta)
        val tvPorcentaje = view.findViewById<TextView>(R.id.tvPorcentajeProgreso)
        val tvRopa = view.findViewById<TextView>(R.id.tvVentasRopa)
        val tvMuebles = view.findViewById<TextView>(R.id.tvVentasMuebles)
        val tvTotalVendidoPesos = view.findViewById<TextView>(R.id.tvTotalVendidoPesos)
        val tvMetaAsignadaPesos = view.findViewById<TextView>(R.id.tvMetaAsignadaPesos)
        val btnActualizar = view.findViewById<Button>(R.id.btnActualizarEstadisticas)
        val btnVolver = view.findViewById<ImageButton>(R.id.btnVolverEstadistica)

        // Cargar datos reales al iniciar
        cargarEstadisticas(tvRopa, tvMuebles, tvTotalVendidoPesos, tvMetaAsignadaPesos, etMeta, pbProgreso, tvPorcentaje)

        btnActualizar.setOnClickListener {
            val metaStr = etMeta.text.toString()
            if (metaStr.isNotEmpty()) {
                val meta = metaStr.toDouble()
                if (meta > 0) {
                    val sdfMes = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                    val mesActual = sdfMes.format(Date())

                    // Guardar meta en SharedPreferences para persistencia
                    val prefs = requireActivity().getSharedPreferences("Metas", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putFloat("meta_mensual", meta.toFloat())
                        .putString("mes_meta", mesActual)
                        .apply()
                    
                    // Recargar estadísticas por si hubo ventas nuevas antes de actualizar el progreso
                    totalVendido = repository.getTotalVendidoMes()
                    tvTotalVendidoPesos.text = format.format(totalVendido)
                    
                    actualizarInterfazMeta(meta, pbProgreso, tvPorcentaje, tvMetaAsignadaPesos)
                } else {
                    Toast.makeText(requireContext(), "La meta debe ser mayor a 0", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Ingresa una meta mensual", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun cargarEstadisticas(
        tvRopa: TextView, 
        tvMuebles: TextView, 
        tvTotal: TextView, 
        tvMetaTxt: TextView,
        etMeta: EditText,
        pb: ProgressBar,
        tvPorc: TextView
    ) {
        totalVendido = repository.getTotalVendidoMes()
        val ropaCount = repository.getCountByArea("Ropa")
        val mueblesCount = repository.getCountByArea("Muebles")

        tvRopa.text = ropaCount.toString()
        tvMuebles.text = mueblesCount.toString()
        tvTotal.text = format.format(totalVendido)

        // Recuperar meta guardada de SharedPreferences
        val prefs = requireActivity().getSharedPreferences("Metas", Context.MODE_PRIVATE)
        
        val sdfMes = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val mesActual = sdfMes.format(Date())
        val mesGuardado = prefs.getString("mes_meta", "")

        var metaGuardada = prefs.getFloat("meta_mensual", 0f).toDouble()

        // Lógica de reseteo automático el día 1 de cada mes
        if (mesActual != mesGuardado) {
            metaGuardada = 0.0
            prefs.edit()
                .putFloat("meta_mensual", 0f)
                .putString("mes_meta", mesActual)
                .apply()
            etMeta.setText("")
        }

        if (metaGuardada > 0) {
            etMeta.setText(metaGuardada.toInt().toString())
            actualizarInterfazMeta(metaGuardada, pb, tvPorc, tvMetaTxt)
        } else {
            tvMetaTxt.text = format.format(0.0)
            pb.progress = 0
            tvPorc.text = "0%"
        }
    }

    private fun actualizarInterfazMeta(meta: Double, pb: ProgressBar, tvPorc: TextView, tvMetaTxt: TextView) {
        val porcentaje = if (meta > 0) (totalVendido / meta) * 100 else 0.0
        val porcentajeInt = porcentaje.toInt().coerceAtMost(100)
        
        pb.progress = porcentajeInt
        tvPorc.text = "$porcentajeInt%"
        tvMetaTxt.text = format.format(meta)

        if (porcentajeInt >= 100) {
            Toast.makeText(requireContext(), "¡Felicidades! Meta alcanzada", Toast.LENGTH_SHORT).show()
        }
    }
}
