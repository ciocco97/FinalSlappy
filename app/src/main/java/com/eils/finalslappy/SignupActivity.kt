package com.eils.finalslappy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.eils.finalslappy.databinding.ActivitySignupBinding
import com.eils.finalslappy.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private lateinit var myAuth: FirebaseAuth
    private lateinit var myDbRef: DatabaseReference

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
            intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }

        binding.btnSignup.setOnClickListener {

            val username = binding.etSignupUsername.text.toString().trim { it <= ' ' }
            val email = binding.etSignupEmail.text.toString().trim { it <= ' ' }
            val password = binding.etSignupPassword.text.toString()
            val retiped_password = binding.etSignupRepeatPassword.text.toString()

            // Register error conditions
            when {
                TextUtils.isEmpty(username) -> {
                    Log.w(tag, "Campo username vuoto")
                }
                TextUtils.isEmpty(email) -> {
                    Log.w(tag, "Campo email vuoto")
                }
                TextUtils.isEmpty(password) -> {
                    Log.w(tag, "Campo email vuoto")
                }
                TextUtils.isEmpty(retiped_password) -> {
                    Log.w(tag, "Campo email vuoto")
                }
                password != retiped_password -> {
                    Log.w(tag, "Le due password non coincidono")
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
        if(currentUser != null){
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
                    firebaseDatabasesignup(username, email, myAuth.currentUser?.uid!!)
                } else {
                    // Sign up fails
                    Log.w(tag, "createUserWithEmail:failure", task.exception)
                }
            }
    }

    private fun firebaseDatabasesignup(username: String, email: String, uid: String) {
        // logic behind adding user to db
//        myDbRef = Firebase.database.reference
//        myDbRef.child("user").child(uid).setValue(User(username, email, uid))
//        toMainActivity()
        val database = Firebase.database
        val myRef = database.getReference("user")

        myRef.child(uid).setValue(User(username, email, uid))
    }

    private fun toMainActivity() {
        intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

}