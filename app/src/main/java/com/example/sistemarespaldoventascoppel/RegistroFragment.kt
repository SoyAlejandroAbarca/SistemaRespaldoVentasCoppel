package com.example.sistemarespaldoventascoppel

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistroFragment : Fragment(R.layout.fragment_registro) {

    private lateinit var repository: VentasRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = VentasRepository(requireContext())

        val btnVolver = view.findViewById<ImageButton>(R.id.btnVolverRegistro)
        btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)


        val etNombre = view.findViewById<TextInputEditText>(R.id.etNombre)
        val etApellidoPat = view.findViewById<TextInputEditText>(R.id.etApellidoPat)
        val etApellidoMat = view.findViewById<TextInputEditText>(R.id.etApellidoMat)
        val etArea = view.findViewById<TextInputEditText>(R.id.etArea)
        val etNumEmpleado = view.findViewById<TextInputEditText>(R.id.etNumEmpleado)
        val etCorreo = view.findViewById<TextInputEditText>(R.id.etCorreo)
        val etPass = view.findViewById<TextInputEditText>(R.id.etPass)
        val etConfirmPass = view.findViewById<TextInputEditText>(R.id.etConfirmPass)


        val tilNombre = view.findViewById<TextInputLayout>(R.id.tilNombre)
        val tilApellidoPat = view.findViewById<TextInputLayout>(R.id.tilApellidoPat)
        val tilApellidoMat = view.findViewById<TextInputLayout>(R.id.tilApellidoMat)
        val tilArea = view.findViewById<TextInputLayout>(R.id.tilArea)
        val tilNumEmpleado = view.findViewById<TextInputLayout>(R.id.tilNumEmpleado)
        val tilCorreo = view.findViewById<TextInputLayout>(R.id.tilCorreo)
        val tilPass = view.findViewById<TextInputLayout>(R.id.tilPass)
        val tilConfirmPass = view.findViewById<TextInputLayout>(R.id.tilConfirmPass)

        btnGuardar.setOnClickListener {

            tilNombre.error = null
            tilApellidoPat.error = null
            tilApellidoMat.error = null
            tilArea.error = null
            tilNumEmpleado.error = null
            tilCorreo.error = null
            tilPass.error = null
            tilConfirmPass.error = null

            var esValido = true

            val nombre = etNombre.text.toString().trim()
            val apPat = etApellidoPat.text.toString().trim()
            val apMat = etApellidoMat.text.toString().trim()
            val area = etArea.text.toString().trim()
            val numEmpleado = etNumEmpleado.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPass.text.toString()

            if (nombre.isEmpty()) {
                tilNombre.error = "Campo obligatorio"
                esValido = false
            }
            if (apPat.isEmpty()) {
                tilApellidoPat.error = "Campo obligatorio"
                esValido = false
            }
            if (apMat.isEmpty()) {
                tilApellidoMat.error = "Campo obligatorio"
                esValido = false
            }
            if (area.isEmpty()) {
                tilArea.error = "Campo obligatorio"
                esValido = false
            }
            if (numEmpleado.isEmpty()) {
                tilNumEmpleado.error = "Campo obligatorio"
                esValido = false
            }
            if (correo.isEmpty()) {
                tilCorreo.error = "Campo obligatorio"
                esValido = false
            }
            
            if (password.isEmpty()) {
                tilPass.error = "Campo obligatorio"
                esValido = false
            } else {

                val tieneMayuscula = password.any { it.isUpperCase() }
                val tieneNumero = password.any { it.isDigit() }
                val tieneSimbolo = password.any { !it.isLetterOrDigit() }
                
                if (password.length < 8) {
                    tilPass.error = "Mínimo 8 caracteres"
                    esValido = false
                } else if (!tieneMayuscula || !tieneNumero || !tieneSimbolo) {
                    tilPass.error = "Debe incluir mayúscula, número y símbolo"
                    esValido = false
                }
            }

            if (etConfirmPass.text.isNullOrBlank()) {
                tilConfirmPass.error = "Campo obligatorio"
                esValido = false
            }


            if (esValido && password != etConfirmPass.text.toString()) {
                tilConfirmPass.error = "Las contraseñas no coinciden"
                esValido = false
            }

            if (esValido) {
                val resultado = repository.registrarUsuario(nombre, apPat, apMat, area, numEmpleado, correo, password)
                
                if (resultado != -1L) {
                    Toast.makeText(requireContext(), "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Error al registrar: El número de empleado o correo ya existen", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, corrige los errores", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
