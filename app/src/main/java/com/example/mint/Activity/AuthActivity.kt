package com.example.mint.Activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mint.Controler.viewControler
import com.example.mint.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthActivity : AppCompatActivity() {

    var controlador: viewControler? = null
    lateinit var activity: Activity

    private lateinit var authButton : Button
    private lateinit var authText: TextView
    private lateinit var emailTxt: TextInputEditText
    private lateinit var pwdTxt: TextInputEditText
    private var isLogin: Boolean = true
    var database: FirebaseDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controlador = viewControler(this)
        activity = this
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance()
        setContentView(R.layout.activity_auth)
        authButton = findViewById(R.id.logInButton)
        authText = findViewById(R.id.registerText)
        emailTxt = findViewById(R.id.emailEditText)
        pwdTxt = findViewById(R.id.passwordEditText)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authText.setOnClickListener { view ->
            if(isLogin){
                cambiarRegistrar()
                isLogin = false
            } else {
                cambiarInicioSesion()
                isLogin = true
            }
        }
        setup()

    }

    private fun setup() {
        authButton.setOnClickListener { view ->
            modoSesion(view) { resultado ->
                if (resultado) {
                    controlador?.cambiarVentana(this, MainMenuActivity::class.java)
                    finish()
                }
            }
        }
    }



    private fun cambiarInicioSesion() {
        findViewById<TextInputLayout>(R.id.passwordConfirmEditTextLayout).visibility = TextInputLayout.GONE
        findViewById<TextInputLayout>(R.id.passwordConfirmEditTextLayout).startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_out).apply {
                duration = 100
            }
        )
        findViewById<TextView>(R.id.passwordConfirmEditText).text = ""
        findViewById<Button>(R.id.logInButton).text = "Iniciar Sesión"
        findViewById<TextView>(R.id.registerText).text = "No tienes cuenta? Registrate"
    }

    /**
     * Cambia la interfaz para el modo de registro.
     */
    private fun cambiarRegistrar() {
        findViewById<TextInputLayout>(R.id.passwordConfirmEditTextLayout).visibility = TextInputLayout.VISIBLE
        findViewById<TextInputLayout>(R.id.passwordConfirmEditTextLayout).startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_in).apply {
                duration = 500
            }
        )
        findViewById<TextView>(R.id.passwordConfirmEditText).text = ""
        findViewById<Button>(R.id.logInButton).text = "Registrarse"
        findViewById<TextView>(R.id.registerText).text = "Ya tienes cuenta? Inicia Sesión"
    }

    private fun iniciarSesion(view: View, callback: (Boolean) -> Unit) {

        val email = emailTxt.text.toString()
        val password = pwdTxt.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "Ha iniciado sesión con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                        callback(true)
                    } else {
                        Toast.makeText(
                            this, "Error de inicio de sesión: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        callback(false)
                    }
                }
        } else {
            Toast.makeText(
                this, "Por favor, complete los campos solicitados",
                Toast.LENGTH_SHORT
            ).show()
            callback(false)
        }
    }

    private fun registroUsuario(view: View, callback: (Boolean) -> Unit) {

        val email = emailTxt.text.toString()
        val password = pwdTxt.text.toString()
        val confirmPassword = findViewById<TextInputEditText>(R.id.passwordConfirmEditText)
            .text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Se ha registrado correctamente, inicie sesión",
                                Toast.LENGTH_SHORT).show()
                            callback(true)
                        } else {
                            Toast.makeText(this, "Error en el registro: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                            callback(false)
                        }
                    }
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT).show()
                callback(false)
            }
        } else {
            Toast.makeText(this, "Complete todos los campos",
                Toast.LENGTH_SHORT).show()
            callback(false)
        }
    }

    private fun modoSesion(view: View, callback: (Boolean) -> Unit) {
        if (isLogin) {
            iniciarSesion(view) { resultado ->
                callback(resultado)
            }
        } else {
            registroUsuario(view) { resultado ->
                callback(resultado)
            }
        }
    }

}