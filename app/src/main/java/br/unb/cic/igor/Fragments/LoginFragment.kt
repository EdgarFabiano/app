package br.unb.cic.igor.Fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import br.unb.cic.igor.MainActivity

import br.unb.cic.igor.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mAuth = FirebaseAuth.getInstance();
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)

        view.login_button.setOnClickListener {
            emailPasswordLogin(view)
        }

        // Return the fragment view/layout
        return view
    }

    private fun emailPasswordLogin(view: View){
        if(!validateForm()){
            Toast.makeText(activity, "Form not valid.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = login_frag_email.text.toString() // email address format
        val password = login_frag_password.text.toString()

        login_fragment_progress.visibility = ProgressBar.VISIBLE

        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity as Activity) { task ->
                    Log.d("Login", "signInWithEmailAndPassword:onComplete: ${task.isSuccessful}. ${task.exception.toString()}");


                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Toast.makeText(activity, "Auth fail.", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(activity, "Auth success!", Toast.LENGTH_SHORT).show()
                        //finish()
                        startActivity(Intent(activity, MainActivity::class.java))
                    }

                    login_fragment_progress.visibility = ProgressBar.GONE
                }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = login_frag_email.text.toString()
        if (TextUtils.isEmpty(email)) {
            login_frag_email.error = "Required."
            valid = false
        } else {
            login_frag_email.error = null
        }

        val password = login_frag_password.text.toString()
        if (TextUtils.isEmpty(password)) {
            login_frag_password.error = "Required."
            valid = false
        } else {
            login_frag_password.error = null
        }

        return valid
    }

}
