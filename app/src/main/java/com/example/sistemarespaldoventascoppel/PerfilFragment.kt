package com.example.sistemarespaldoventascoppel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    private lateinit var ivFotoPerfil: ImageView
    private lateinit var repository: VentasRepository
    private var usuarioLogueado: String? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            val bitmap: Bitmap? = if (imageUri != null) {
                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                BitmapFactory.decodeStream(inputStream)
            } else {
                result.data?.extras?.get("data") as? Bitmap
            }

            bitmap?.let {
                ivFotoPerfil.setImageBitmap(it)
                guardarFotoEnDB(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = VentasRepository(requireContext())
        val prefs = requireActivity().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)
        usuarioLogueado = prefs.getString("usuario_logueado", null)

        val btnRegresar = view.findViewById<ImageButton>(R.id.btnRegresarPerfil)
        val btnCerrarSesion = view.findViewById<Button>(R.id.btnCerrarSesionPerfil)
        val btnEditarPerfil = view.findViewById<Button>(R.id.btnEditarPerfil)
        val btnCambiarFoto = view.findViewById<FloatingActionButton>(R.id.btnCambiarFoto)
        ivFotoPerfil = view.findViewById(R.id.ivFotoPerfil)
        
        val tvNombre = view.findViewById<TextView>(R.id.tvNombrePerfil)
        val tvNumEmpleado = view.findViewById<TextView>(R.id.tvNumEmpleadoPerfil)
        val tvCentro = view.findViewById<TextView>(R.id.tvCentroPerfil)
        val tvCorreo = view.findViewById<TextView>(R.id.tvCorreoPerfil)
        val tvTelefono = view.findViewById<TextView>(R.id.tvTelefonoPerfil)

        cargarDatosUsuario(tvNombre, tvNumEmpleado, tvCentro, tvCorreo, tvTelefono)

        btnRegresar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnEditarPerfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, EditarPerfilFragment())
                .addToBackStack(null)
                .commit()
        }

        btnCambiarFoto.setOnClickListener {
            mostrarOpcionesImagen()
        }

        btnCerrarSesion.setOnClickListener {
            prefs.edit().clear().apply()
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, LoginFragment())
                .commit()
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarDatosUsuario(tvNombre: TextView, tvNum: TextView, tvCentro: TextView, tvCorreo: TextView, tvTel: TextView) {
        if (usuarioLogueado != null) {
            val datosUser = repository.obtenerDatosUsuario(usuarioLogueado!!)
            if (datosUser != null) {
                val nombre = datosUser["nombre"]
                val apPat = datosUser["apPat"]
                val apMat = datosUser["apMat"]
                val area = datosUser["area"]
                val numEmp = datosUser["numEmpleado"]
                val correo = datosUser["correo"]
                val telefono = datosUser["telefono"] ?: "No registrado"
                
                tvNombre.text = "$nombre $apPat $apMat"
                tvNum.text = "#$numEmp"
                tvCentro.text = "Área: $area"
                tvCorreo.text = "Correo: $correo"
                tvTel.text = "Teléfono: $telefono"
                cargarFotoPerfil()
            }
        }
    }

    private fun cargarFotoPerfil() {
        if (usuarioLogueado == null) return

        val dbHelper = DatabaseHelper.getInstance(requireContext())
        val db = dbHelper.getReadableDb()
        val query = "SELECT ${DatabaseHelper.COLUMN_USER_FOTO} FROM ${DatabaseHelper.TABLE_USUARIOS} WHERE ${DatabaseHelper.COLUMN_USER_CORREO} = ? OR ${DatabaseHelper.COLUMN_USER_NUM_EMPLEADO} = ?"
        val cursor = db.rawQuery(query, arrayOf(usuarioLogueado, usuarioLogueado))
        
        if (cursor.moveToFirst()) {
            val fotoBase64 = cursor.getString(0)
            if (!fotoBase64.isNullOrEmpty()) {
                val decodedByte = Base64.decode(fotoBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
                ivFotoPerfil.setImageBitmap(bitmap)
            }
        }
        cursor.close()
    }

    private fun guardarFotoEnDB(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        val fotoBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)

        if (usuarioLogueado != null) {
            val res = repository.actualizarFotoPerfil(usuarioLogueado!!, fotoBase64)
            if (res > 0) {
                Toast.makeText(requireContext(), "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarOpcionesImagen() {
        val opciones = arrayOf("Cámara", "Galería")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Seleccionar imagen")
        builder.setItems(opciones) { _, which ->
            when (which) {
                0 -> abrirCamara()
                1 -> abrirGaleria()
            }
        }
        builder.show()
    }

    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        pickImageLauncher.launch(intent)
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
}
