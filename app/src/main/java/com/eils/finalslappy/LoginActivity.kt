package com.eils.finalslappy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import com.eils.finalslappy.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var myAuth: FirebaseAuth

    private val tag: String = "FinalSlappy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // To remove the action bar from the login activity
        supportActionBar?.hide()

        // Obtaining the shared "Firebase auth" instance
        myAuth = FirebaseAuth.getInstance()

        binding.tvSignup.setOnClickListener {
            Log.d("FinalSlappy", "Bottone cliccato")
            intent = Intent(this, SignupActivity::class.java)
            finish()
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {

            val email = binding.etLoginEmail.text.toString().trim { it <= ' ' }
            val password = binding.etLoginPassword.text.toString()

            // Register error conditions
            when {
                TextUtils.isEmpty(email) -> {
                    Log.w(tag, "Campo email vuoto")
                }
                TextUtils.isEmpty(password) -> {
                    Log.w(tag, "Campo email vuoto")
                }

                else -> {
                    firebaseAuthLogin(email, password)
                }
            }
        }

    }

    private fun firebaseAuthLogin(email: String, password: String) {
        myAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login success
                    Log.d(tag, "signInWithEmail:success")
                    login()
                } else {
                    // Login in fails
                    Log.w(tag, "signInWithEmail:failure", task.exception)
                }
            }
    }

    private fun login() {
        intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = myAuth.currentUser
        if(currentUser != null){
            Log.w(tag, "Utente già loggato")
        }
    }

}