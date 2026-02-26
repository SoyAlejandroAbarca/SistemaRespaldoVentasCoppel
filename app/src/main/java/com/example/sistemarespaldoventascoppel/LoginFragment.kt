package com.example.sistemarespaldoventascoppel

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var repository: VentasRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = VentasRepository(requireContext())

        // Referencias del diseño
        val tilUsuario = view.findViewById<TextInputLayout>(R.id.tilUsuario)
        val etUsuario = view.findViewById<TextInputEditText>(R.id.etUsuario)
        val tilPassword = view.findViewById<TextInputLayout>(R.id.tilPassword)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnIniciarSesion)
        val tvCrearCuenta = view.findViewById<TextView>(R.id.tvCrearCuenta)

        // Acción para el botón "Iniciar sesión"
        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            // Limpiar errores previos
            tilUsuario.error = null
            tilPassword.error = null

            // Validación de campos obligatorios
            if (usuario.isEmpty()) {
                tilUsuario.error = "Ingresa tu correo o # de empleado"
                return@setOnClickListener
            }
            if (pass.isEmpty()) {
                tilPassword.error = "Ingresa tu contraseña"
                return@setOnClickListener
            }

            // Validar contra el Repositorio (que maneja la DB cifrada)
            val loginExitoso = repository.validarLogin(usuario, pass)

            if (loginExitoso) {
                // Guardar sesión en SharedPreferences
                val prefs = requireActivity().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)
                prefs.edit().putString("usuario_logueado", usuario).apply()

                // Obtener datos reales del usuario desde el repositorio
                val datosUser = repository.obtenerDatosUsuario(usuario)
                
                val bundle = Bundle()
                if (datosUser != null) {
                    val nombreCompleto = "${datosUser["nombre"]} ${datosUser["apPat"]} ${datosUser["apMat"]}"
                    bundle.putString("nombre_clave", nombreCompleto)
                    bundle.putString("num_empleado_clave", "#${datosUser["numEmpleado"]}")
                } else {
                    bundle.putString("nombre_clave", "Usuario Coppel")
                    bundle.putString("num_empleado_clave", usuario)
                }

                val homeFrag = HomeFragment()
                homeFrag.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedor_fragmentos, homeFrag)
                    .addToBackStack(null)
                    .commit()
                
                Toast.makeText(context, "Bienvenido al sistema", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
            }
        }

        // Acción para "Crear cuenta"
        tvCrearCuenta.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, RegistroFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
