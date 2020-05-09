package org.wit.shop19.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import org.wit.shop19.R


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var firebaseAuth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textViewWelcome)
        if (intent.hasExtra(ARG_NAME)) {
            textView.text = String.format(
                "Welcome - %s",
                intent.getStringExtra(ARG_NAME)
            )
        }
        findViewById<View>(R.id.buttonLogout).setOnClickListener(this)
        findViewById<View>(R.id.buttonDisconnect).setOnClickListener(this)
        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        firebaseAuth = FirebaseAuth.getInstance()

        //button
        val mStartActBtn = findViewById<Button>(R.id.startActBtn)
        //handle button click
        mStartActBtn.setOnClickListener {
            //start activity intent
            startActivity(Intent(this@MainActivity, ShopActivity::class.java))
        }



            //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "back to SignIn"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonLogout -> signOut()
            R.id.buttonDisconnect -> revokeAccess()
        }
    }

    private fun signOut() {
        // Firebase sign out
        firebaseAuth!!.signOut()

        // Google sign out
        googleSignInClient!!.signOut().addOnCompleteListener(
            this
        ) { // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Signed out of google")
        }
    }
////////////// did not use this part
    private fun revokeAccess() {
        // Firebase sign out
        firebaseAuth!!.signOut()

        // Google revoke access
        googleSignInClient!!.revokeAccess().addOnCompleteListener(
            this
        ) { // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Revoked Access")
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val ARG_NAME = "username"
        fun startActivity(context: Context, username: String?) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(ARG_NAME, username)
            context.startActivity(intent)
        }
    }
}