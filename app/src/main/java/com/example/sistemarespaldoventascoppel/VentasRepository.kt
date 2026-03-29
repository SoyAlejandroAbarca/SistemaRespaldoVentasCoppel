package com.example.sistemarespaldoventascoppel

import android.content.Context
import net.sqlcipher.Cursor

class VentasRepository(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun registrarUsuario(nombre: String, apPat: String, apMat: String, area: String, numEmp: String, correo: String, pass: String): Long {
        return dbHelper.registrarUsuario(nombre, apPat, apMat, area, numEmp, correo, pass)
    }

    fun validarLogin(usuario: String, pass: String): Boolean {
        return dbHelper.validarLogin(usuario, pass)
    }

    fun obtenerDatosUsuario(usuario: String): Map<String, String>? {
        return dbHelper.obtenerDatosUsuario(usuario)
    }

    fun actualizarFotoPerfil(usuario: String, fotoBase64: String): Int {
        return dbHelper.actualizarFotoPerfil(usuario, fotoBase64)
    }

    fun actualizarDatosPerfil(usuarioActual: String, nombre: String, correo: String, telefono: String, area: String): Int {
        return dbHelper.actualizarDatosPerfil(usuarioActual, nombre, correo, telefono, area)
    }

    fun actualizarPassword(usuario: String, nuevaPass: String): Int {
        return dbHelper.actualizarPassword(usuario, nuevaPass)
    }

    fun registrarVenta(datos: Map<String, String>): Long {
        return dbHelper.registrarVenta(datos)
    }

    fun actualizarVenta(id: Int, datos: Map<String, String>): Int {
        return dbHelper.actualizarVenta(id, datos)
    }

    fun eliminarVenta(id: Int): Int {
        return dbHelper.eliminarVenta(id)
    }

    fun buscarVentas(query: String): List<Venta> {
        val cursor = dbHelper.buscarVenta(query)
        return mapCursorToVentas(cursor)
    }

    fun getVentasHoy(): List<Venta> {
        val cursor = dbHelper.getVentasHoy()
        return mapCursorToVentas(cursor)
    }

    fun getTotalVendidoMes(): Double {
        return dbHelper.getTotalVendidoMes()
    }

    fun getCountByArea(area: String): Int {
        return dbHelper.getCountByArea(area)
    }

    private fun mapCursorToVentas(cursor: Cursor): List<Venta> {
        val lista = mutableListOf<Venta>()
        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_ID)
            val pedIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_PEDIDO)
            val nomIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_NOM_CLIENTE)
            val apPIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_AP_PAT_CLIENTE)
            val apMIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_AP_MAT_CLIENTE)
            val proIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_DESC_PROD)
            val tipIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_TIPO)
            val prvIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_PROVEEDOR)
            val areIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_AREA)
            val depIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_DEPTO)
            val preIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_PRECIO)
            val pagIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_PAGO_INICIAL)
            val fenIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_FECHA_ENTREGA)
            val lugIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_LUGAR_ENTREGA)
            val cpIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_CP)
            val calIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_CALLE)
            val nxtIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_NUM_EXT)
            val nitIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_NUM_INT)
            val colIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_COLONIA)
            val locIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_LOCALIDAD)
            val munIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_MUNICIPIO)
            val entIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_ENTRE_CALLES)
            val refIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_REFERENCIAS)
            val telPIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_TEL_PRINCIPAL)
            val telAIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_TEL_ADICIONAL)
            val frIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_VENTA_FECHA_REGISTRO)

            do {
                lista.add(Venta(
                    id = if (idIdx != -1) cursor.getInt(idIdx) else 0,
                    pedido = if (pedIdx != -1) cursor.getString(pedIdx) ?: "" else "",
                    nombre = if (nomIdx != -1) cursor.getString(nomIdx) ?: "" else "",
                    apPat = if (apPIdx != -1) cursor.getString(apPIdx) ?: "" else "",
                    apMat = if (apMIdx != -1) cursor.getString(apMIdx) ?: "" else "",
                    producto = if (proIdx != -1) cursor.getString(proIdx) ?: "" else "",
                    tipo = if (tipIdx != -1) cursor.getString(tipIdx) ?: "" else "",
                    proveedor = if (prvIdx != -1) cursor.getString(prvIdx) ?: "" else "",
                    area = if (areIdx != -1) cursor.getString(areIdx) ?: "" else "",
                    depto = if (depIdx != -1) cursor.getString(depIdx) ?: "" else "",
                    precio = if (preIdx != -1) cursor.getString(preIdx) ?: "" else "",
                    pagoInicial = if (pagIdx != -1) cursor.getString(pagIdx) ?: "" else "",
                    fechaEntrega = if (fenIdx != -1) cursor.getString(fenIdx) ?: "" else "",
                    lugarEntrega = if (lugIdx != -1) cursor.getString(lugIdx) ?: "" else "",
                    cp = if (cpIdx != -1) cursor.getString(cpIdx) ?: "" else "",
                    calle = if (calIdx != -1) cursor.getString(calIdx) ?: "" else "",
                    numExt = if (nxtIdx != -1) cursor.getString(nxtIdx) ?: "" else "",
                    numInt = if (nitIdx != -1) cursor.getString(nitIdx) ?: "" else "",
                    colonia = if (colIdx != -1) cursor.getString(colIdx) ?: "" else "",
                    localidad = if (locIdx != -1) cursor.getString(locIdx) ?: "" else "",
                    municipio = if (munIdx != -1) cursor.getString(munIdx) ?: "" else "",
                    entreCalles = if (entIdx != -1) cursor.getString(entIdx) ?: "" else "",
                    referencias = if (refIdx != -1) cursor.getString(refIdx) ?: "" else "",
                    telPrincipal = if (telPIdx != -1) cursor.getString(telPIdx) ?: "" else "",
                    telAdicional = if (telAIdx != -1) cursor.getString(telAIdx) ?: "" else "",
                    fechaRegistro = if (frIdx != -1) cursor.getString(frIdx) ?: "" else ""
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}
