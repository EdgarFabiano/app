package br.unb.cic.igor

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val user = mAuth!!.currentUser

        main_email.setText(user!!.email)
        main_id.setText(user!!.uid)

        main_logout_button.setOnClickListener{
            logout()
        }

    }

    fun logout(){
        mAuth!!.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }


}
