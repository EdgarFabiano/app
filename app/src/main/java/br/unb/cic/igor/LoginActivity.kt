package br.unb.cic.igor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.widget.Toast
import br.unb.cic.igor.Fragments.LoginFragment
import br.unb.cic.igor.Fragments.RegisterFragment
import br.unb.cic.igor.R.id.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 23 //the request code could be any Integer
    // [START declare_auth]
    private lateinit var mAuth: FirebaseAuth
    // [END declare_auth]
    private val manager = supportFragmentManager

    lateinit var registerFragment: RegisterFragment
    lateinit var loginFragment: LoginFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        registerFragment = RegisterFragment.newInstance()
        loginFragment = LoginFragment.newInstance()

        ShowLoginFragment()
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        //updateUI(currentUser)
    }

    fun ShowLoginFragment(){
        manager.beginTransaction()
            .replace(R.id.login_fragment_holder,  loginFragment)
            .addToBackStack(loginFragment.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }



    companion object {
        private val TAG = "EmailPassword"
    }
}
