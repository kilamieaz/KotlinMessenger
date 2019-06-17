package com.example.kotlinmessenger.registerlogin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.messages.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()
            //check is empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Log.d("Login", "Attempt login with email/pw: $email/***")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    // else if successful
                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    //clear off all of the previous activities on your stack or your activity stack
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    Log.d("Login", "Successfully Login user with uid: ${it.result!!.user.uid}")
                }
                .addOnFailureListener{
                    Log.d("Login", "Failed to Login: ${it.message}")
                    Toast.makeText(this, "Failed to Login: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        back_to_register_textview.setOnClickListener {
            finish()
        }
    }
}