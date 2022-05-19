package com.example.authorizationapp.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.authorizationapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignIn : AppCompatActivity() {
    private lateinit var googleButton: Button

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        init()

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            }
            catch (e: ApiException){
                Log.d("Tag", "ApiException")
            }
        }
        checkAuthState()
        googleButton.setOnClickListener(googleListener)

    }

    private fun getClient(): GoogleSignInClient{
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    private fun singInWithGoogle(){
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private var googleListener: View.OnClickListener = View.OnClickListener {
        singInWithGoogle()
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this, "Авторизация прошла успешно!!!", Toast.LENGTH_LONG).show()
                checkAuthState()
            }
            else{
                Toast.makeText(this, "Ошибка авторизации", Toast.LENGTH_LONG).show()
                Log.d("Tag", "Ошибка авторизации")
            }
        }
    }

    private fun checkAuthState(){
        if(auth.currentUser != null){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    private fun init(){
        googleButton = findViewById(R.id.loginGoogleButton)
        auth = Firebase.auth

    }
}