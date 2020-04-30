package org.wit.shop19.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.wit.shop19.R


class LoginActivity : AppCompatActivity() {
    var googleSignInClient: GoogleSignInClient? = null
    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setOnClickListener { // Launch Sign In
            signInToGoogle()
        }

        // Configure Google Client
        configureGoogleClient()
    }

    private fun configureGoogleClient() {
        // Configure Google Sign In
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) // for the requestIdToken, this is in the values.xml file that
                // is generated from your google-services.json
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set the dimensions of the sign-in button.
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_WIDE)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth!!.currentUser
        if (currentUser != null) {
            Log.d(
                TAG,
                "Currently Signed in: " + currentUser.email
            )
            showToastMessage("Currently Logged in: " + currentUser.email)
        }
    }

    fun signInToGoogle() {
        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent,
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account =
                    task.getResult(ApiException::class.java)
                showToastMessage("Google Sign in Succeeded")
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                showToastMessage("Google Sign in Failed $e")
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account!!.id)
        val credential =
            GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = firebaseAuth!!.currentUser
                    Log.d(
                        TAG,
                        "signInWithCredential:success: currentUser: " + user!!.email
                    )
                    showToastMessage("Firebase Authentication Succeeded ")
                    launchMainActivity(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(
                        TAG,
                        "signInWithCredential:failure",
                        task.exception
                    )
                    showToastMessage("Firebase Authentication failed:" + task.exception)
                }
            }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun launchMainActivity(user: FirebaseUser?) {
        if (user != null) {
            MainActivity.startActivity(
                this,
                user.displayName
            )
            finish()
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 1001
    }
}
