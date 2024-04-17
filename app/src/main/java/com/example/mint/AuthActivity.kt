package com.example.mint

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import java.security.Provider

class AuthActivity : AppCompatActivity() {
    lateinit var authButton : Button
    lateinit var authText: TextView
    lateinit var emailTxt: TextInputEditText
    lateinit var pwdTxt: TextInputEditText
    var isLogin: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                cambiarInicioSesión()
                isLogin = true
            }
        }
        setup()

    }

    private fun setup() {
        title = "Auth"

        authButton.setOnClickListener {
            if (!isLogin) {
                if (emailTxt.text!!.isNotEmpty() && pwdTxt.text!!.isNotEmpty()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        emailTxt.text.toString(),
                        pwdTxt.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Ha sido registrado correctamente, " +
                                    "inicie sesión", Toast.LENGTH_SHORT).show()
                            cambiarInicioSesión()
                        } else {
                            showAlert()
                        }

                    }
                }
            }
            else{
                if (emailTxt.text!!.isNotEmpty() && pwdTxt.text!!.isNotEmpty()){
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(emailTxt.text.toString(),
                            pwdTxt.text.toString()).addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(this, "Dentro"
                                        , Toast.LENGTH_SHORT).show()
                                }
                        }
                }
            }

        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autenticación")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMainMenu(){

    }

    fun cambiarInicioSesión() {
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
    fun cambiarRegistrar() {
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

}