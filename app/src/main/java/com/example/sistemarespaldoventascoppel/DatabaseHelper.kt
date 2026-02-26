package com.example.sistemarespaldoventascoppel

import android.content.ContentValues
import android.content.Context
import net.sqlcipher.Cursor
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val passphrase = SecurityUtils.getDatabasePassphrase(context)

    companion object {
        private const val DATABASE_NAME = "SistemaVentas.db"
        private const val DATABASE_VERSION = 3

        @Volatile
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context.applicationContext).also {
                    instance = it
                    SQLiteDatabase.loadLibs(context.applicationContext)
                }
            }
        }

        // Tabla Usuarios
        const val TABLE_USUARIOS = "usuarios"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NOMBRE = "nombre"
        const val COLUMN_USER_AP_PAT = "apellido_paterno"
        const val COLUMN_USER_AP_MAT = "apellido_materno"
        const val COLUMN_USER_AREA = "area"
        const val COLUMN_USER_NUM_EMPLEADO = "numero_empleado"
        const val COLUMN_USER_CORREO = "correo"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_TELEFONO = "telefono"
        const val COLUMN_USER_FOTO = "foto"

        // Tabla Ventas
        const val TABLE_VENTAS = "ventas"
        const val COLUMN_VENTA_ID = "id"
        const val COLUMN_VENTA_PEDIDO = "numero_pedido"
        const val COLUMN_VENTA_NOM_CLIENTE = "nombre_cliente"
        const val COLUMN_VENTA_AP_PAT_CLIENTE = "ap_paterno_cliente"
        const val COLUMN_VENTA_AP_MAT_CLIENTE = "ap_materno_cliente"
        const val COLUMN_VENTA_DESC_PROD = "descripcion_producto"
        const val COLUMN_VENTA_TIPO = "tipo"
        const val COLUMN_VENTA_PROVEEDOR = "proveedor"
        const val COLUMN_VENTA_AREA = "area"
        const val COLUMN_VENTA_DEPTO = "departamento"
        const val COLUMN_VENTA_PRECIO = "precio"
        const val COLUMN_VENTA_PAGO_INICIAL = "pago_inicial"
        const val COLUMN_VENTA_FECHA_ENTREGA = "fecha_entrega"
        const val COLUMN_VENTA_LUGAR_ENTREGA = "lugar_entrega"
        const val COLUMN_VENTA_CP = "cp"
        const val COLUMN_VENTA_CALLE = "calle"
        const val COLUMN_VENTA_NUM_EXT = "num_ext"
        const val COLUMN_VENTA_NUM_INT = "num_int"
        const val COLUMN_VENTA_COLONIA = "colonia"
        const val COLUMN_VENTA_LOCALIDAD = "localidad"
        const val COLUMN_VENTA_MUNICIPIO = "municipio"
        const val COLUMN_VENTA_ENTRE_CALLES = "entre_calles"
        const val COLUMN_VENTA_REFERENCIAS = "referencias"
        const val COLUMN_VENTA_TEL_PRINCIPAL = "telefono_principal"
        const val COLUMN_VENTA_TEL_ADICIONAL = "telefono_adicional"
        const val COLUMN_VENTA_FECHA_REGISTRO = "fecha_registro"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableUsuarios = ("CREATE TABLE " + TABLE_USUARIOS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NOMBRE + " TEXT,"
                + COLUMN_USER_AP_PAT + " TEXT,"
                + COLUMN_USER_AP_MAT + " TEXT,"
                + COLUMN_USER_AREA + " TEXT,"
                + COLUMN_USER_NUM_EMPLEADO + " TEXT UNIQUE,"
                + COLUMN_USER_CORREO + " TEXT UNIQUE,"
                + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_USER_TELEFONO + " TEXT,"
                + COLUMN_USER_FOTO + " TEXT" + ")")
        
        val createTableVentas = ("CREATE TABLE " + TABLE_VENTAS + "("
                + COLUMN_VENTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_VENTA_PEDIDO + " TEXT,"
                + COLUMN_VENTA_NOM_CLIENTE + " TEXT,"
                + COLUMN_VENTA_AP_PAT_CLIENTE + " TEXT,"
                + COLUMN_VENTA_AP_MAT_CLIENTE + " TEXT,"
                + COLUMN_VENTA_DESC_PROD + " TEXT,"
                + COLUMN_VENTA_TIPO + " TEXT,"
                + COLUMN_VENTA_PROVEEDOR + " TEXT,"
                + COLUMN_VENTA_AREA + " TEXT,"
                + COLUMN_VENTA_DEPTO + " TEXT,"
                + COLUMN_VENTA_PRECIO + " TEXT,"
                + COLUMN_VENTA_PAGO_INICIAL + " TEXT,"
                + COLUMN_VENTA_FECHA_ENTREGA + " TEXT,"
                + COLUMN_VENTA_LUGAR_ENTREGA + " TEXT,"
                + COLUMN_VENTA_CP + " TEXT,"
                + COLUMN_VENTA_CALLE + " TEXT,"
                + COLUMN_VENTA_NUM_EXT + " TEXT,"
                + COLUMN_VENTA_NUM_INT + " TEXT,"
                + COLUMN_VENTA_COLONIA + " TEXT,"
                + COLUMN_VENTA_LOCALIDAD + " TEXT,"
                + COLUMN_VENTA_MUNICIPIO + " TEXT,"
                + COLUMN_VENTA_ENTRE_CALLES + " TEXT,"
                + COLUMN_VENTA_REFERENCIAS + " TEXT,"
                + COLUMN_VENTA_TEL_PRINCIPAL + " TEXT,"
                + COLUMN_VENTA_TEL_ADICIONAL + " TEXT,"
                + COLUMN_VENTA_FECHA_REGISTRO + " TEXT" + ")")

        db?.execSQL(createTableUsuarios)
        db?.execSQL(createTableVentas)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_VENTAS ADD COLUMN $COLUMN_VENTA_FECHA_REGISTRO TEXT")
        }
        if (oldVersion < 3) {
            db?.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_USER_TELEFONO TEXT")
            db?.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_USER_FOTO TEXT")
        }
    }

    fun getWritableDb(): SQLiteDatabase {
        return this.getWritableDatabase(passphrase)
    }

    fun getReadableDb(): SQLiteDatabase {
        return this.getReadableDatabase(passphrase)
    }

    // --- Métodos para Usuarios ---

    fun registrarUsuario(nombre: String, apPat: String, apMat: String, area: String, numEmp: String, correo: String, pass: String): Long {
        val db = getWritableDb()
        val values = ContentValues()
        values.put(COLUMN_USER_NOMBRE, nombre)
        values.put(COLUMN_USER_AP_PAT, apPat)
        values.put(COLUMN_USER_AP_MAT, apMat)
        values.put(COLUMN_USER_AREA, area)
        values.put(COLUMN_USER_NUM_EMPLEADO, numEmp)
        values.put(COLUMN_USER_CORREO, correo)
        values.put(COLUMN_USER_PASSWORD, pass)

        val result = db.insert(TABLE_USUARIOS, null, values)
        return result
    }

    fun validarLogin(usuario: String, pass: String): Boolean {
        val db = getReadableDb()
        val query = "SELECT $COLUMN_USER_ID FROM $TABLE_USUARIOS WHERE ($COLUMN_USER_CORREO = ? OR $COLUMN_USER_NUM_EMPLEADO = ?) AND $COLUMN_USER_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(usuario, usuario, pass))
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    fun actualizarFotoPerfil(usuario: String, fotoBase64: String): Int {
        val db = getWritableDb()
        val values = ContentValues()
        values.put(COLUMN_USER_FOTO, fotoBase64)
        val result = db.update(TABLE_USUARIOS, values, "$COLUMN_USER_CORREO = ? OR $COLUMN_USER_NUM_EMPLEADO = ?", arrayOf(usuario, usuario))
        return result
    }

    fun actualizarDatosPerfil(usuarioActual: String, nombre: String, correo: String, telefono: String, area: String): Int {
        val db = getWritableDb()
        val values = ContentValues()
        values.put(COLUMN_USER_NOMBRE, nombre)
        values.put(COLUMN_USER_CORREO, correo)
        values.put(COLUMN_USER_TELEFONO, telefono)
        values.put(COLUMN_USER_AREA, area)
        val result = db.update(TABLE_USUARIOS, values, "$COLUMN_USER_CORREO = ? OR $COLUMN_USER_NUM_EMPLEADO = ?", arrayOf(usuarioActual, usuarioActual))
        return result
    }

    fun actualizarPassword(usuario: String, nuevaPass: String): Int {
        val db = getWritableDb()
        val values = ContentValues()
        values.put(COLUMN_USER_PASSWORD, nuevaPass)
        val result = db.update(TABLE_USUARIOS, values, "$COLUMN_USER_CORREO = ? OR $COLUMN_USER_NUM_EMPLEADO = ?", arrayOf(usuario, usuario))
        return result
    }

    fun obtenerDatosUsuario(usuario: String): Map<String, String>? {
        val db = getReadableDb()
        val query = "SELECT $COLUMN_USER_NOMBRE, $COLUMN_USER_AP_PAT, $COLUMN_USER_AP_MAT, $COLUMN_USER_NUM_EMPLEADO, $COLUMN_USER_AREA, $COLUMN_USER_CORREO, $COLUMN_USER_TELEFONO FROM $TABLE_USUARIOS WHERE $COLUMN_USER_CORREO = ? OR $COLUMN_USER_NUM_EMPLEADO = ?"
        val cursor = db.rawQuery(query, arrayOf(usuario, usuario))
        
        var result: Map<String, String>? = null
        if (cursor.moveToFirst()) {
            result = mapOf(
                "nombre" to (cursor.getString(0) ?: ""),
                "apPat" to (cursor.getString(1) ?: ""),
                "apMat" to (cursor.getString(2) ?: ""),
                "numEmpleado" to (cursor.getString(3) ?: ""),
                "area" to (cursor.getString(4) ?: ""),
                "correo" to (cursor.getString(5) ?: ""),
                "telefono" to (cursor.getString(6) ?: "No registrado")
            )
        }
        cursor.close()
        return result
    }

    fun getUsuario(usuario: String): Cursor {
        val db = getReadableDb()
        val query = "SELECT * FROM $TABLE_USUARIOS WHERE $COLUMN_USER_CORREO = ? OR $COLUMN_USER_NUM_EMPLEADO = ?"
        return db.rawQuery(query, arrayOf(usuario, usuario)) as Cursor
    }

    // --- Métodos para Ventas ---

    fun registrarVenta(datos: Map<String, String>): Long {
        val db = getWritableDb()
        val values = ContentValues()
        
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaHoy = sdf.format(Date())

        values.put(COLUMN_VENTA_PEDIDO, datos["pedido"])
        values.put(COLUMN_VENTA_NOM_CLIENTE, datos["nombre"])
        values.put(COLUMN_VENTA_AP_PAT_CLIENTE, datos["apPat"])
        values.put(COLUMN_VENTA_AP_MAT_CLIENTE, datos["apMat"])
        values.put(COLUMN_VENTA_DESC_PROD, datos["producto"])
        values.put(COLUMN_VENTA_TIPO, datos["tipo"])
        values.put(COLUMN_VENTA_PROVEEDOR, datos["proveedor"])
        values.put(COLUMN_VENTA_AREA, datos["area"])
        values.put(COLUMN_VENTA_DEPTO, datos["depto"])
        values.put(COLUMN_VENTA_PRECIO, datos["precio"])
        values.put(COLUMN_VENTA_PAGO_INICIAL, datos["pagoInicial"])
        values.put(COLUMN_VENTA_FECHA_ENTREGA, datos["fechaEntrega"])
        values.put(COLUMN_VENTA_LUGAR_ENTREGA, datos["lugarEntrega"])
        values.put(COLUMN_VENTA_CP, datos["cp"])
        values.put(COLUMN_VENTA_CALLE, datos["calle"])
        values.put(COLUMN_VENTA_NUM_EXT, datos["numExt"])
        values.put(COLUMN_VENTA_NUM_INT, datos["numInt"])
        values.put(COLUMN_VENTA_COLONIA, datos["colonia"])
        values.put(COLUMN_VENTA_LOCALIDAD, datos["localidad"])
        values.put(COLUMN_VENTA_MUNICIPIO, datos["municipio"])
        values.put(COLUMN_VENTA_ENTRE_CALLES, datos["entreCalles"])
        values.put(COLUMN_VENTA_REFERENCIAS, datos["referencias"])
        values.put(COLUMN_VENTA_TEL_PRINCIPAL, datos["telPrincipal"])
        values.put(COLUMN_VENTA_TEL_ADICIONAL, datos["telAdicional"])
        values.put(COLUMN_VENTA_FECHA_REGISTRO, fechaHoy)

        val result = db.insert(TABLE_VENTAS, null, values)
        return result
    }

    fun actualizarVenta(id: Int, datos: Map<String, String>): Int {
        val db = getWritableDb()
        val values = ContentValues()
        values.put(COLUMN_VENTA_PEDIDO, datos["pedido"])
        values.put(COLUMN_VENTA_NOM_CLIENTE, datos["nombre"])
        values.put(COLUMN_VENTA_AP_PAT_CLIENTE, datos["apPat"])
        values.put(COLUMN_VENTA_AP_MAT_CLIENTE, datos["apMat"])
        values.put(COLUMN_VENTA_DESC_PROD, datos["producto"])
        values.put(COLUMN_VENTA_TIPO, datos["tipo"])
        values.put(COLUMN_VENTA_PROVEEDOR, datos["proveedor"])
        values.put(COLUMN_VENTA_AREA, datos["area"])
        values.put(COLUMN_VENTA_DEPTO, datos["depto"])
        values.put(COLUMN_VENTA_PRECIO, datos["precio"])
        values.put(COLUMN_VENTA_PAGO_INICIAL, datos["pagoInicial"])
        values.put(COLUMN_VENTA_FECHA_ENTREGA, datos["fechaEntrega"])
        values.put(COLUMN_VENTA_LUGAR_ENTREGA, datos["lugarEntrega"])
        values.put(COLUMN_VENTA_CP, datos["cp"])
        values.put(COLUMN_VENTA_CALLE, datos["calle"])
        values.put(COLUMN_VENTA_NUM_EXT, datos["numExt"])
        values.put(COLUMN_VENTA_NUM_INT, datos["numInt"])
        values.put(COLUMN_VENTA_COLONIA, datos["colonia"])
        values.put(COLUMN_VENTA_LOCALIDAD, datos["localidad"])
        values.put(COLUMN_VENTA_MUNICIPIO, datos["municipio"])
        values.put(COLUMN_VENTA_ENTRE_CALLES, datos["entreCalles"])
        values.put(COLUMN_VENTA_REFERENCIAS, datos["referencias"])
        values.put(COLUMN_VENTA_TEL_PRINCIPAL, datos["telPrincipal"])
        values.put(COLUMN_VENTA_TEL_ADICIONAL, datos["telAdicional"])

        val result = db.update(TABLE_VENTAS, values, "$COLUMN_VENTA_ID = ?", arrayOf(id.toString()))
        return result
    }

    fun eliminarVenta(id: Int): Int {
        val db = getWritableDb()
        val result = db.delete(TABLE_VENTAS, "$COLUMN_VENTA_ID = ?", arrayOf(id.toString()))
        return result
    }

    fun buscarVenta(query: String): Cursor {
        val db = getReadableDb()
        val searchTerm = "%$query%"
        val sql = "SELECT * FROM $TABLE_VENTAS WHERE $COLUMN_VENTA_PEDIDO LIKE ? OR $COLUMN_VENTA_NOM_CLIENTE LIKE ? OR $COLUMN_VENTA_AP_PAT_CLIENTE LIKE ? OR $COLUMN_VENTA_AP_MAT_CLIENTE LIKE ?"
        return db.rawQuery(sql, arrayOf(searchTerm, searchTerm, searchTerm, searchTerm)) as Cursor
    }

    fun getVentasHoy(): Cursor {
        val db = getReadableDb()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaHoy = sdf.format(Date())
        val sql = "SELECT * FROM $TABLE_VENTAS WHERE $COLUMN_VENTA_FECHA_REGISTRO = ? ORDER BY $COLUMN_VENTA_ID DESC"
        return db.rawQuery(sql, arrayOf(fechaHoy)) as Cursor
    }

    fun getTotalVendidoMes(): Double {
        val db = getReadableDb()
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val mesActual = sdf.format(Date()) + "%"
        val query = "SELECT SUM(CAST($COLUMN_VENTA_PRECIO AS REAL)) FROM $TABLE_VENTAS WHERE $COLUMN_VENTA_FECHA_REGISTRO LIKE ?"
        val cursor = db.rawQuery(query, arrayOf(mesActual))
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        return total
    }

    fun getCountByArea(area: String): Int {
        val db = getReadableDb()
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val mesActual = sdf.format(Date()) + "%"
        val query = "SELECT COUNT(*) FROM $TABLE_VENTAS WHERE $COLUMN_VENTA_AREA LIKE ? AND $COLUMN_VENTA_FECHA_REGISTRO LIKE ?"
        val cursor = db.rawQuery(query, arrayOf(area, mesActual))
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }
}
