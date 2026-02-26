package com.example.sistemarespaldoventascoppel

import android.os.Bundle

data class Venta(
    val id: Int,
    val pedido: String,
    val nombre: String,
    val apPat: String,
    val apMat: String,
    val producto: String,
    val tipo: String,
    val proveedor: String,
    val area: String,
    val depto: String,
    val precio: String,
    val pagoInicial: String,
    val fechaEntrega: String,
    val lugarEntrega: String,
    val cp: String,
    val calle: String,
    val numExt: String,
    val numInt: String,
    val colonia: String,
    val localidad: String,
    val municipio: String,
    val entreCalles: String,
    val referencias: String,
    val telPrincipal: String,
    val telAdicional: String,
    val fechaRegistro: String
) {
    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("pedido", pedido)
        bundle.putString("nombre", nombre)
        bundle.putString("apPat", apPat)
        bundle.putString("apMat", apMat)
        bundle.putString("producto", producto)
        bundle.putString("tipo", tipo)
        bundle.putString("proveedor", proveedor)
        bundle.putString("area", area)
        bundle.putString("depto", depto)
        bundle.putString("precio", precio)
        bundle.putString("pagoInicial", pagoInicial)
        bundle.putString("fechaEntrega", fechaEntrega)
        bundle.putString("lugarEntrega", lugarEntrega)
        bundle.putString("cp", cp)
        bundle.putString("calle", calle)
        bundle.putString("numExt", numExt)
        bundle.putString("numInt", numInt)
        bundle.putString("colonia", colonia)
        bundle.putString("localidad", localidad)
        bundle.putString("municipio", municipio)
        bundle.putString("entreCalles", entreCalles)
        bundle.putString("referencias", referencias)
        bundle.putString("telPrincipal", telPrincipal)
        bundle.putString("telAdicional", telAdicional)
        return bundle
    }
}
