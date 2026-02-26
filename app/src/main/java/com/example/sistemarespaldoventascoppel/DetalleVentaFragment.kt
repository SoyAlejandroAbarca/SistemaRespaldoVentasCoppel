package com.example.sistemarespaldoventascoppel

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DetalleVentaFragment : Fragment(R.layout.fragment_detalle_venta) {

    private lateinit var dbHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        val args = arguments ?: return

        // Vincular vistas
        val btnVolver = view.findViewById<ImageButton>(R.id.btnVolverDetalle)
        val btnEditar = view.findViewById<Button>(R.id.btnEditarVenta)
        val btnEliminar = view.findViewById<Button>(R.id.btnEliminarVenta)
        val btnGenerarPDF = view.findViewById<Button>(R.id.btnGenerarPDF)
        val btnCompartir = view.findViewById<Button>(R.id.btnCompartir)

        val tvPedido = view.findViewById<TextView>(R.id.tvDetallePedido)
        val tvCliente = view.findViewById<TextView>(R.id.tvDetalleCliente)
        val tvProducto = view.findViewById<TextView>(R.id.tvDetalleProducto)
        val tvDeptoArea = view.findViewById<TextView>(R.id.tvDetalleDeptoArea)
        val tvPrecio = view.findViewById<TextView>(R.id.tvDetallePrecio)
        val tvPagoInicial = view.findViewById<TextView>(R.id.tvDetallePagoInicial)
        val tvFecha = view.findViewById<TextView>(R.id.tvDetalleFecha)
        val tvLugar = view.findViewById<TextView>(R.id.tvDetalleLugar)
        val tvDireccion = view.findViewById<TextView>(R.id.tvDetalleDireccion)
        val tvReferencias = view.findViewById<TextView>(R.id.tvDetalleReferencias)
        val tvTelefonos = view.findViewById<TextView>(R.id.tvDetalleTelefonos)

        // Asignar datos a las vistas
        tvPedido.text = "#" + args.getString("pedido")
        tvCliente.text = "${args.getString("nombre")} ${args.getString("apPat")} ${args.getString("apMat")}"
        tvProducto.text = args.getString("producto")
        tvDeptoArea.text = "${args.getString("depto")} - ${args.getString("area")} (${args.getString("tipo")})"
        tvPrecio.text = "$" + args.getString("precio")
        tvPagoInicial.text = "$" + args.getString("pagoInicial")
        tvFecha.text = args.getString("fechaEntrega")
        tvLugar.text = args.getString("lugarEntrega")
        
        val dir = "${args.getString("calle")} #${args.getString("numExt")} ${args.getString("numInt")}, Col. ${args.getString("colonia")}, ${args.getString("localidad")}, ${args.getString("municipio")}. CP ${args.getString("cp")}"
        tvDireccion.text = dir
        tvReferencias.text = "Entre calles: ${args.getString("entreCalles")}\nRef: ${args.getString("referencias")}"
        tvTelefonos.text = "Tel: ${args.getString("telPrincipal")} / ${args.getString("telAdicional")}"

        btnVolver.setOnClickListener { parentFragmentManager.popBackStack() }

        btnEditar.setOnClickListener {
            val fragmentRegistro = RegistrarVentaFragment().apply {
                arguments = args
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, fragmentRegistro)
                .addToBackStack(null)
                .commit()
        }

        btnEliminar.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Venta")
                .setMessage("¿Estás seguro de que deseas eliminar este registro?")
                .setPositiveButton("Eliminar") { _, _ ->
                    val id = args.getInt("id")
                    dbHelper.eliminarVenta(id)
                    Toast.makeText(requireContext(), "Venta eliminada", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        btnGenerarPDF.setOnClickListener {
            val pdfFile = generarPDFCompleto(args)
            if (pdfFile != null) abrirPDF(pdfFile)
        }

        btnCompartir.setOnClickListener {
            val pdfFile = generarPDFCompleto(args)
            if (pdfFile != null) compartirArchivo(pdfFile)
        }
    }

    private fun generarPDFCompleto(args: Bundle): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Tamaño A4
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // Cabecera
        paint.color = Color.parseColor("#00519E")
        canvas.drawRect(0f, 0f, 595f, 80f, paint)
        
        paint.color = Color.WHITE
        paint.textSize = 20f
        paint.isFakeBoldText = true
        canvas.drawText("SISTEMA DE RESPALDO DE VENTAS COPPEL", 50f, 50f, paint)

        // Cuerpo
        paint.color = Color.BLACK
        paint.textSize = 12f
        paint.isFakeBoldText = false
        var y = 120f
        val x = 50f
        val lineSpacing = 25f

        paint.isFakeBoldText = true
        canvas.drawText("DATOS DEL PEDIDO", x, y, paint); y += lineSpacing
        paint.isFakeBoldText = false
        canvas.drawText("Número de Pedido: ${args.getString("pedido")}", x, y, paint); y += lineSpacing
        canvas.drawText("Cliente: ${args.getString("nombre")} ${args.getString("apPat")} ${args.getString("apMat")}", x, y, paint); y += lineSpacing * 2

        paint.isFakeBoldText = true
        canvas.drawText("DETALLES DEL PRODUCTO", x, y, paint); y += lineSpacing
        paint.isFakeBoldText = false
        canvas.drawText("Producto: ${args.getString("producto")}", x, y, paint); y += lineSpacing
        canvas.drawText("Departamento: ${args.getString("depto")} | Área: ${args.getString("area")}", x, y, paint); y += lineSpacing
        canvas.drawText("Proveedor: ${args.getString("proveedor")}", x, y, paint); y += lineSpacing
        canvas.drawText("Precio: $${args.getString("precio")}", x, y, paint); y += lineSpacing
        canvas.drawText("Pago Inicial: $${args.getString("pagoInicial")}", x, y, paint); y += lineSpacing * 2

        paint.isFakeBoldText = true
        canvas.drawText("DATOS DE ENTREGA", x, y, paint); y += lineSpacing
        paint.isFakeBoldText = false
        canvas.drawText("Fecha Estimada: ${args.getString("fechaEntrega")}", x, y, paint); y += lineSpacing
        canvas.drawText("Lugar: ${args.getString("lugarEntrega")}", x, y, paint); y += lineSpacing
        canvas.drawText("Dirección: ${args.getString("calle")} #${args.getString("numExt")}, Col. ${args.getString("colonia")}", x, y, paint); y += lineSpacing
        canvas.drawText("${args.getString("localidad")}, ${args.getString("municipio")}. CP ${args.getString("cp")}", x, y, paint); y += lineSpacing
        canvas.drawText("Entre calles: ${args.getString("entreCalles")}", x, y, paint); y += lineSpacing
        canvas.drawText("Referencias: ${args.getString("referencias")}", x, y, paint); y += lineSpacing * 2

        paint.isFakeBoldText = true
        canvas.drawText("CONTACTO", x, y, paint); y += lineSpacing
        paint.isFakeBoldText = false
        canvas.drawText("Tel. Principal: ${args.getString("telPrincipal")}", x, y, paint); y += lineSpacing
        canvas.drawText("Tel. Adicional: ${args.getString("telAdicional")}", x, y, paint); y += lineSpacing * 3

        canvas.drawText("____________________________", 50f, y, paint)
        canvas.drawText("Firma de Conformidad", 50f, y + 20, paint)

        pdfDocument.finishPage(page)

        val fileName = "Pedido_${args.getString("pedido")}.pdf"
        val file = File(requireContext().cacheDir, fileName)
        
        return try {
            val outputStream = FileOutputStream(file)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()
            file
        } catch (e: IOException) {
            null
        }
    }

    private fun abrirPDF(file: File) {
        val authority = "${requireContext().packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(requireContext(), authority, file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    }

    private fun compartirArchivo(file: File) {
        val authority = "${requireContext().packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(requireContext(), authority, file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Compartir Venta PDF:"))
    }
}
