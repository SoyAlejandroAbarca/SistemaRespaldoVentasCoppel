package com.example.sistemarespaldoventascoppel

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditarPerfilFragment : Fragment(R.layout.fragment_editar_perfil) {

    private lateinit var dbHelper: DatabaseHelper
    private var usuarioLogueado: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        val prefs = requireActivity().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)
        usuarioLogueado = prefs.getString("usuario_logueado", null)

        val btnRegresar = view.findViewById<ImageButton>(R.id.btnRegresarEditar)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCambios)
        
        val tilNombre = view.findViewById<TextInputLayout>(R.id.tilNombreEditar)
        val tilCorreo = view.findViewById<TextInputLayout>(R.id.tilCorreoEditar)
        val tilTelefono = view.findViewById<TextInputLayout>(R.id.tilTelefonoEditar)
        val tilCentro = view.findViewById<TextInputLayout>(R.id.tilCentroEditar)
        val tilPassActual = view.findViewById<TextInputLayout>(R.id.tilPasswordActual)
        val tilPassNueva = view.findViewById<TextInputLayout>(R.id.tilPasswordNueva)
        val tilPassConfirmar = view.findViewById<TextInputLayout>(R.id.tilPasswordConfirmar)

        val etNombre = view.findViewById<TextInputEditText>(R.id.etNombreEditar)
        val etCorreo = view.findViewById<TextInputEditText>(R.id.etCorreoEditar)
        val etTelefono = view.findViewById<TextInputEditText>(R.id.etTelefonoEditar)
        val etCentro = view.findViewById<TextInputEditText>(R.id.etCentroEditar)
        
        val etPasswordActual = view.findViewById<TextInputEditText>(R.id.etPasswordActual)
        val etPasswordNueva = view.findViewById<TextInputEditText>(R.id.etPasswordNueva)
        val etPasswordConfirmar = view.findViewById<TextInputEditText>(R.id.etPasswordConfirmar)

        // Cargar datos actuales desde la DB
        cargarDatosActuales(etNombre, etCorreo, etTelefono, etCentro)

        btnRegresar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val centro = etCentro.text.toString().trim()
            
            val passActual = etPasswordActual.text.toString()
            val passNueva = etPasswordNueva.text.toString()
            val passConfirmar = etPasswordConfirmar.text.toString()
            
            tilNombre.error = null
            tilCorreo.error = null
            tilTelefono.error = null
            tilCentro.error = null
            tilPassActual.error = null
            tilPassNueva.error = null
            tilPassConfirmar.error = null

            var esValido = true

            if (nombre.isEmpty()) { tilNombre.error = "Campo obligatorio"; esValido = false }
            if (correo.isEmpty()) { tilCorreo.error = "Campo obligatorio"; esValido = false }
            if (telefono.isEmpty()) { tilTelefono.error = "Campo obligatorio"; esValido = false }
            if (centro.isEmpty()) { tilCentro.error = "Campo obligatorio"; esValido = false }

            if (esValido && usuarioLogueado != null) {
                // Actualizar datos básicos
                val res = dbHelper.actualizarDatosPerfil(usuarioLogueado!!, nombre, correo, telefono, centro)
                
                // Si intenta cambiar contraseña
                if (passActual.isNotEmpty() || passNueva.isNotEmpty() || passConfirmar.isNotEmpty()) {
                    if (!dbHelper.validarLogin(usuarioLogueado!!, passActual)) {
                        tilPassActual.error = "Contraseña actual incorrecta"
                        esValido = false
                    } else if (passNueva.isEmpty()) {
                        tilPassNueva.error = "Ingresa la nueva contraseña"
                        esValido = false
                    } else if (!validarSeguridadPassword(passNueva)) {
                        tilPassNueva.error = "Debe tener 8+ caracteres, mayúscula, número y símbolo"
                        esValido = false
                    } else if (passNueva != passConfirmar) {
                        tilPassConfirmar.error = "Las contraseñas no coinciden"
                        esValido = false
                    } else {
                        dbHelper.actualizarPassword(usuarioLogueado!!, passNueva)
                    }
                }

                if (esValido) {
                    // Si el correo cambió, actualizamos la sesión
                    if (correo != usuarioLogueado) {
                        prefs.edit().putString("usuario_logueado", correo).apply()
                    }
                    Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun cargarDatosActuales(etNombre: TextInputEditText, etCorreo: TextInputEditText, etTel: TextInputEditText, etCentro: TextInputEditText) {
        if (usuarioLogueado != null) {
            val cursor = dbHelper.getUsuario(usuarioLogueado!!)
            if (cursor.moveToFirst()) {
                etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NOMBRE)))
                etCorreo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_CORREO)))
                etTel.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_TELEFONO)) ?: "")
                etCentro.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_AREA)))
            }
            cursor.close()
        }
    }

    private fun validarSeguridadPassword(password: String): Boolean {
        val regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$".toRegex()
        return regex.matches(password)
    }
}
