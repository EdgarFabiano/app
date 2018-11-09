package br.unb.cic.igor.Fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import br.unb.cic.igor.R
import br.unb.cic.igor.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.form_register.view.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegisterFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RegisterFragment : Fragment(){

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mDatabase: FirebaseFirestore

    private var mAuth: FirebaseAuth? = null
    private var manager: FragmentManager? = null

    private val genderList = arrayOf("Feminino", "Masculino")
    private var selectedGender: String? = "Feminino"

    private var birthdate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseFirestore.getInstance()
        manager = fragmentManager

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_register, container, false)

        view.form_register.register_button.setOnClickListener {
            register(view)
        }

        view.form_register.cancel_register_button.setOnClickListener {
            activity?.onBackPressed()
        }

        // Return the fragment view/layout
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        form_register.register_gender.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, genderList)

        form_register.register_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedGender = null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGender = genderList[position]
            }
        }
    }

    private fun register(v: View) {
        if(!validateForm()){
            Toast.makeText(activity, "Form not valid.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = form_register.register_email.text.toString().trim() // email address format
        val password = form_register.register_password.text.toString().trim()
        val birthdate = form_register.register_birthdate.text.toString().trim()
        val username = form_register.register_username.text.toString().trim()

        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity as Activity) { task ->
                    Log.d("Register", "createUserWithEmail:onComplete: ${task.isSuccessful}");

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Toast.makeText(activity, "Auth fail.", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(activity, "Auth success!.", Toast.LENGTH_SHORT).show()
                        val firebaseUser = mAuth!!.currentUser
                        if(firebaseUser != null){
                            val user = User(firebaseUser.uid, firebaseUser.email!!, username, null, selectedGender)
                            User.Insert(user, mDatabase)
                            showLoginFragment()
                        }
                    }
                }
    }

    fun showLoginFragment(){
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.login_fragment_holder, LoginFragment(), "LoginFragment Transaction")
        ft.addToBackStack(null)
        ft.commit()
    }


    private fun validateForm(): Boolean {
        var valid = true
        val myIcon = resources.getDrawable(R.drawable.igor_attention)
        myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth()/2, myIcon.getIntrinsicHeight()/2)

        val email = form_register.register_email.text.toString()
        if (TextUtils.isEmpty(email)) {
            //register_email.error = "Required."
            form_register.register_email.setError("Required", myIcon)
            valid = false
        } else {
            form_register.register_email.error = null
        }

        val password = form_register.register_password.text.toString()
        if (TextUtils.isEmpty(password)) {
            form_register.register_password.setError("Required", myIcon)
            valid = false
        } else if(password.length < 6) {
            form_register.register_password.setError("At least 6 characters on password.", myIcon)
            valid = false
        } else{
            form_register.register_password.error = null
        }

        val username = form_register.register_username.text.toString()
        if (TextUtils.isEmpty(username)) {
            form_register.register_username.setError("Required", myIcon)
            valid = false
        } else {
            form_register.register_username.error = null
        }

        val birthdate = form_register.register_birthdate.text.toString()
        if (TextUtils.isEmpty(birthdate)) {
            form_register.register_birthdate.setError("Required", myIcon)
            valid = false
        } else {
            form_register.register_birthdate.error = null
        }

        return valid
    }

    companion object{

        fun newInstance(): RegisterFragment{
            return RegisterFragment()
        }
    }


}
