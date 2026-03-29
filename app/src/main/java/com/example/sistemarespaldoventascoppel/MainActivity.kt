package com.example.sistemarespaldoventascoppel

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottomNavigation)

        val btnSettings = findViewById<ImageButton>(R.id.btnSettingsMain)
        val btnHome = findViewById<ImageButton>(R.id.btnHomeMain)
        val btnExit = findViewById<ImageButton>(R.id.btnExitMain)

        btnSettings.setOnClickListener {
            cambiarFragmento(ConfiguracionFragment(), "Configuracion")
        }

        btnHome.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.contenedor_fragmentos)
            if (currentFragment !is HomeFragment) {
                cambiarFragmento(HomeFragment(), "Home")
            } else {
                Toast.makeText(this, "Ya estás en el Inicio", Toast.LENGTH_SHORT).show()
            }
        }

        btnExit.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, LoginFragment())
                .commit()
            ocultarBottomNav()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        }


        supportFragmentManager.addOnBackStackChangedListener {
            actualizarVisibilidadBarra()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragmentos, LoginFragment())
                .commit()
            ocultarBottomNav()
        }
    }

    private fun cambiarFragmento(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedor_fragmentos, fragment)
            .addToBackStack(tag)
            .commit()
    }

    fun mostrarBottomNav() {
        bottomNavigation.visibility = View.VISIBLE
    }

    fun ocultarBottomNav() {
        bottomNavigation.visibility = View.GONE
    }

    private fun actualizarVisibilidadBarra() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.contenedor_fragmentos)
        if (currentFragment is LoginFragment || currentFragment is RegistroFragment) {
            ocultarBottomNav()
        } else {
            mostrarBottomNav()
        }
    }
}