package br.unb.cic.igor.Fragments

import android.app.Activity
import android.content.Intent
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
import br.unb.cic.igor.classes.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*


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

    private val TAG = "LoginFragment"

    private var mAuth: FirebaseAuth? = null
    private val manager = fragmentManager

    lateinit var mDatabase: FirebaseFirestore

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions

    val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseFirestore.getInstance()

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity as Activity, gso)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)

        view.login_button.setOnClickListener {
            emailPasswordLogin(view)
        }

        view.login_fragment_create_account_btn.setOnClickListener{
            ShowRegisterFragment()
        }

        view.login_frag_google.setOnClickListener{
            googleSignIn()
        }

        // Return the fragment view/layout
        return view
    }

    private fun googleSignIn(){
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleResult(task)
        }
    }

    private fun handleGoogleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException){
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            onLoginFail(false)
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        login_fragment_progress.visibility = ProgressBar.VISIBLE
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null);
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(activity as Activity) { task ->
                    Log.d(TAG, "firebaseAuthWithGoogle:onComplete: ${task.isSuccessful}. ${task.exception.toString()}");


                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        onLoginFail()
                    } else{
                        onLoginSuccess()
                    }

                    login_fragment_progress.visibility = ProgressBar.GONE
                }
    }
    // [END auth_with_google]

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
                        onLoginFail()
                    } else{
                        onLoginSuccess()
                    }

                    login_fragment_progress.visibility = ProgressBar.GONE
                }
    }

    private fun onLoginSuccess(){
        Toast.makeText(activity, "Auth success!", Toast.LENGTH_SHORT).show()
        val firebaseUser = mAuth!!.currentUser
        if(firebaseUser != null){
           User.Get(firebaseUser!!.uid, mDatabase).addOnSuccessListener{
                task ->
                    var user = task.toObject(User::class.java)
                    if(user == null){
                        ShowCompleteRegistrationFragment()
                    } else{
                        User.SetInstance(user)
                        startActivity(Intent(activity, MainActivity::class.java))
                    }
            }

        }
        //finish()

    }

    private fun onLoginFail(show: Boolean = true){
        if(show){
            Toast.makeText(activity, "Auth fail.", Toast.LENGTH_SHORT).show()
        }

    }

    fun ShowCompleteRegistrationFragment(){
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.login_fragment_holder, CompleteRegistrationFragment(), "CompleteRegistrationFragment Transaction")
        ft.addToBackStack(null)
        ft.commit()
    }

    fun ShowRegisterFragment(){
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.login_fragment_holder, RegisterFragment(), "RegisterFragment Transaction")
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun validateForm(): Boolean {
        var valid = true
        val myIcon = resources.getDrawable(R.drawable.igor_attention)
        myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth()/2, myIcon.getIntrinsicHeight()/2)

        val email = login_frag_email.text.toString()
        if (TextUtils.isEmpty(email)) {
            login_frag_email.setError("Required", myIcon)
            valid = false
        } else {
            login_frag_email.error = null
        }

        val password = login_frag_password.text.toString()
        if (TextUtils.isEmpty(password)) {
            login_frag_password.setError("Required", myIcon)
            valid = false
        } else {
            login_frag_password.error = null
        }

        return valid
    }

    companion object{

        fun newInstance(): LoginFragment{
            return LoginFragment()
        }
    }

}
