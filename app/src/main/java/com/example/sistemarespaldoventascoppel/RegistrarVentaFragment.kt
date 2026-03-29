package com.example.sistemarespaldoventascoppel

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

class RegistrarVentaFragment : Fragment(R.layout.fragment_registrar_venta) {

    private lateinit var dbHelper: DatabaseHelper
    private var ventaId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        val btnVolver = view.findViewById<ImageButton>(R.id.btnVolverVenta)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarVenta)

        val etNumeroPedido = view.findViewById<TextInputEditText>(R.id.etNumeroPedido)
        val etNombreCliente = view.findViewById<TextInputEditText>(R.id.etNombreCliente)
        val etApPaternoCliente = view.findViewById<TextInputEditText>(R.id.etApPaternoCliente)
        val etApMaternoCliente = view.findViewById<TextInputEditText>(R.id.etApMaternoCliente)
        val etDescripcionProducto = view.findViewById<TextInputEditText>(R.id.etDescripcionProducto)

        val etTipo = view.findViewById<AutoCompleteTextView>(R.id.etTipo)
        val etProveedor = view.findViewById<AutoCompleteTextView>(R.id.etProveedor)
        val etArea = view.findViewById<AutoCompleteTextView>(R.id.etArea)
        val etLugarEntrega = view.findViewById<AutoCompleteTextView>(R.id.etLugarEntrega)
        
        val etDepartamento = view.findViewById<TextInputEditText>(R.id.etDepartamento)
        val etPrecio = view.findViewById<TextInputEditText>(R.id.etPrecio)
        val etPagoInicial = view.findViewById<TextInputEditText>(R.id.etPagoInicial)
        val etFechaEntrega = view.findViewById<TextInputEditText>(R.id.etFechaEntrega)
        
        val etCP = view.findViewById<TextInputEditText>(R.id.etCP)
        val etCalle = view.findViewById<TextInputEditText>(R.id.etCalle)
        val etNumExt = view.findViewById<TextInputEditText>(R.id.etNumExt)
        val etNumInt = view.findViewById<TextInputEditText>(R.id.etNumInt)
        val etColonia = view.findViewById<TextInputEditText>(R.id.etColonia)
        val etLocalidad = view.findViewById<TextInputEditText>(R.id.etLocalidad)
        val etMunicipio = view.findViewById<TextInputEditText>(R.id.etMunicipio)
        val etEntreCalles = view.findViewById<TextInputEditText>(R.id.etEntreCalles)
        val etReferencias = view.findViewById<TextInputEditText>(R.id.etReferencias)
        val etTelefonoPrincipal = view.findViewById<TextInputEditText>(R.id.etTelefonoPrincipal)
        val etTelefonoAdicional = view.findViewById<TextInputEditText>(R.id.etTelefonoAdicional)

        val opcionesTipo = arrayOf("Crédito", "Contado")
        val adapterTipo = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, opcionesTipo)
        etTipo.setAdapter(adapterTipo)

        val opcionesProveedor = arrayOf("Coppel", "Externo")
        val adapterProveedor = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, opcionesProveedor)
        etProveedor.setAdapter(adapterProveedor)

        val opcionesArea = arrayOf("Ropa", "Muebles")
        val adapterArea = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, opcionesArea)
        etArea.setAdapter(adapterArea)

        val opcionesEntrega = arrayOf("Tienda", "Domicilio")
        val adapterEntrega = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, opcionesEntrega)
        etLugarEntrega.setAdapter(adapterEntrega)

        val fields = listOf(
            etNumeroPedido, etNombreCliente, etApPaternoCliente, etApMaternoCliente,
            etDescripcionProducto, etTipo, etProveedor, etArea, etDepartamento,
            etPrecio, etPagoInicial, etFechaEntrega, etLugarEntrega, etCP, etCalle,
            etNumExt, etNumInt, etColonia, etLocalidad, etMunicipio, etEntreCalles,
            etReferencias, etTelefonoPrincipal, etTelefonoAdicional
        )

        ventaId = arguments?.getInt("id", -1) ?: -1
        if (ventaId != -1) {
            btnGuardar.text = "Actualizar Registro"
            etNumeroPedido.setText(arguments?.getString("pedido"))
            etNombreCliente.setText(arguments?.getString("nombre"))
            etApPaternoCliente.setText(arguments?.getString("apPat"))
            etApMaternoCliente.setText(arguments?.getString("apMat"))
            etDescripcionProducto.setText(arguments?.getString("producto"))

            etTipo.setText(arguments?.getString("tipo"), false)
            etProveedor.setText(arguments?.getString("proveedor"), false)
            etArea.setText(arguments?.getString("area"), false)
            etLugarEntrega.setText(arguments?.getString("lugarEntrega"), false)
            
            etDepartamento.setText(arguments?.getString("depto"))
            etPrecio.setText(arguments?.getString("precio"))
            etPagoInicial.setText(arguments?.getString("pagoInicial"))
            etFechaEntrega.setText(arguments?.getString("fechaEntrega"))
            etCP.setText(arguments?.getString("cp"))
            etCalle.setText(arguments?.getString("calle"))
            etNumExt.setText(arguments?.getString("numExt"))
            etNumInt.setText(arguments?.getString("numInt"))
            etColonia.setText(arguments?.getString("colonia"))
            etLocalidad.setText(arguments?.getString("localidad"))
            etMunicipio.setText(arguments?.getString("municipio"))
            etEntreCalles.setText(arguments?.getString("entreCalles"))
            etReferencias.setText(arguments?.getString("referencias"))
            etTelefonoPrincipal.setText(arguments?.getString("telPrincipal"))
            etTelefonoAdicional.setText(arguments?.getString("telAdicional"))
        }

        btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnGuardar.setOnClickListener {
            var allFilled = true
            
            for (field in fields) {
                if (field.text.toString().trim().isEmpty()) {
                    field.error = "Este campo es obligatorio"
                    allFilled = false
                } else {
                    field.error = null
                }
            }

            if (allFilled) {
                val datosVenta = mapOf(
                    "pedido" to etNumeroPedido.text.toString(),
                    "nombre" to etNombreCliente.text.toString(),
                    "apPat" to etApPaternoCliente.text.toString(),
                    "apMat" to etApMaternoCliente.text.toString(),
                    "producto" to etDescripcionProducto.text.toString(),
                    "tipo" to etTipo.text.toString(),
                    "proveedor" to etProveedor.text.toString(),
                    "area" to etArea.text.toString(),
                    "depto" to etDepartamento.text.toString(),
                    "precio" to etPrecio.text.toString(),
                    "pagoInicial" to etPagoInicial.text.toString(),
                    "fechaEntrega" to etFechaEntrega.text.toString(),
                    "lugarEntrega" to etLugarEntrega.text.toString(),
                    "cp" to etCP.text.toString(),
                    "calle" to etCalle.text.toString(),
                    "numExt" to etNumExt.text.toString(),
                    "numInt" to etNumInt.text.toString(),
                    "colonia" to etColonia.text.toString(),
                    "localidad" to etLocalidad.text.toString(),
                    "municipio" to etMunicipio.text.toString(),
                    "entreCalles" to etEntreCalles.text.toString(),
                    "referencias" to etReferencias.text.toString(),
                    "telPrincipal" to etTelefonoPrincipal.text.toString(),
                    "telAdicional" to etTelefonoAdicional.text.toString()
                )

                val resultado: Long
                if (ventaId != -1) {
                    resultado = dbHelper.actualizarVenta(ventaId, datosVenta).toLong()
                } else {
                    resultado = dbHelper.registrarVenta(datosVenta)
                }

                if (resultado > 0) {
                    val msg = if (ventaId != -1) "Registro actualizado" else "Venta guardada con éxito"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Error al procesar la información", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
