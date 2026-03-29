package com.example.sistemarespaldoventascoppel

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ConsultarVentasFragment : Fragment(R.layout.fragment_consultar_ventas) {

    private lateinit var repository: VentasRepository
    private lateinit var ventaAdapter: VentaAdapter
    private lateinit var rvVentas: RecyclerView
    private lateinit var tvTituloListado: TextView
    private lateinit var tvSinResultados: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = VentasRepository(requireContext())

        val btnVolver = view.findViewById<ImageButton>(R.id.btnVolverConsultar)
        val etBusqueda = view.findViewById<EditText>(R.id.etBusquedaVenta)
        val btnBuscar = view.findViewById<Button>(R.id.btnBuscarVenta)
        tvTituloListado = view.findViewById(R.id.tvTituloListado)
        tvSinResultados = view.findViewById(R.id.tvSinResultados)
        rvVentas = view.findViewById(R.id.rvVentas)

        rvVentas.layoutManager = LinearLayoutManager(requireContext())
        ventaAdapter = VentaAdapter(emptyList()) { venta ->
            // Navegar al detalle de la venta
            val fragmentDetalle = DetalleVentaFragment().apply {
                arguments = venta.toBundle()
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, fragmentDetalle)
                .addToBackStack(null)
                .commit()
        }
        rvVentas.adapter = ventaAdapter

        cargarVentasDelDia()

        btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnBuscar.setOnClickListener {
            val query = etBusqueda.text.toString().trim()
            if (query.isNotEmpty()) {
                buscarVentas(query)
            } else {
                cargarVentasDelDia()
            }
        }
    }

    private fun cargarVentasDelDia() {
        tvTituloListado.text = "Ventas del día"
        val ventas = repository.getVentasHoy()
        actualizarUI(ventas)
    }

    private fun buscarVentas(query: String) {
        tvTituloListado.text = "Resultados de búsqueda: $query"
        val ventas = repository.buscarVentas(query)
        actualizarUI(ventas)
    }

    private fun actualizarUI(ventas: List<Venta>) {
        ventaAdapter.actualizarLista(ventas)
        
        if (ventas.isEmpty()) {
            tvSinResultados.visibility = View.VISIBLE
            rvVentas.visibility = View.GONE
        } else {
            tvSinResultados.visibility = View.GONE
            rvVentas.visibility = View.VISIBLE
        }
    }
}
