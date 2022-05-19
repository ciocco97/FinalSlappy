package com.eils.finalslappy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.eils.finalslappy.databinding.ActivitySignupBinding
import com.eils.finalslappy.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private lateinit var myAuth: FirebaseAuth
    private val db = Firebase.firestore

    private val tag: String = "FinalSlappy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // To remove the action bar from the sign up activity
        supportActionBar?.hide()

        // Obtaining the shared "Firebase auth" instance
        myAuth = FirebaseAuth.getInstance()

        // Switch to login activity
        binding.tvLogin.setOnClickListener {
            toLoginActivity()
        }

        binding.btnSignup.setOnClickListener {

            val username = binding.etSignupUsername.text.toString().trim { it <= ' ' }
            val email = binding.etSignupEmail.text.toString().trim { it <= ' ' }
            val password = binding.etSignupPassword.text.toString()
            val retipedPassword = binding.etSignupRepeatPassword.text.toString()

            // Register error conditions
            when {
                TextUtils.isEmpty(username) -> {
                    Log.w(tag, "Username field empty")
                }
                TextUtils.isEmpty(email) -> {
                    Log.w(tag, "Email field empty")
                }
                TextUtils.isEmpty(password) -> {
                    Log.w(tag, "Password field empty")
                }
                TextUtils.isEmpty(retipedPassword) -> {
                    Log.w(tag, "Retype password field empty")
                }
                password != retipedPassword -> {
                    Log.w(tag, "The two passwords don't match")
                }

                else -> {
                    firebaseAuthSignup(username, email, password)
                }
            }
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = myAuth.currentUser
        if (currentUser != null) {
            Log.d(tag, "Utente già loggato")
            toMainActivity()
        }
    }

    private fun firebaseAuthSignup(username: String, email: String, password: String) {
        myAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success
                    Log.d(tag, "createUserWithEmail:success")
                    firebaseDatabaseSignup(username, email, myAuth.currentUser?.uid!!)
                } else {
                    // Sign up fails
                    Log.w(tag, "createUserWithEmail:failure", task.exception)
                }
            }
    }

    private fun firebaseDatabaseSignup(username: String, email: String, uid: String) {
        // Create a new user with a username and an email
        val user = User(username, email)

        // Add a new document (user) with the same id of the FirebaseAuth one
        db.collection("user").document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.d(tag, "DocumentSnapshot added with ID: uid")
                toMainActivity()
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
            }
    }

    private fun toLoginActivity() {
        intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun toMainActivity() {
        intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

}