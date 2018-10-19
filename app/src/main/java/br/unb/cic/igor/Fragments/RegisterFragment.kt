package br.unb.cic.igor.Fragments

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.unb.cic.igor.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*
import android.widget.Toast
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.text.TextUtils
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import br.unb.cic.igor.R.id.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import java.time.Instant
import java.util.*




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

    private var mAuth: FirebaseAuth? = null
    private var manager: FragmentManager? = null

    private val genderList = arrayOf("Feminino", "Masculino")
    private var selectedGender: String? = null

    private var birthdate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        mAuth = FirebaseAuth.getInstance();
        manager = fragmentManager

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_register, container, false)

        view.register_button.setOnClickListener {
            register(view)
        }

        // Return the fragment view/layout
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_gender.adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, genderList)

        register_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

        val email = register_email.text.toString().trim() // email address format
        val password = register_password.text.toString().trim()

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
                        showLoginFragment()
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

        val email = register_email.text.toString()
        if (TextUtils.isEmpty(email)) {
            register_email.error = "Required."
            valid = false
        } else {
            register_email.error = null
        }

        val password = register_password.text.toString()
        if (TextUtils.isEmpty(password)) {
            register_password.error = "Required."
            valid = false
        } else if(password.length < 6) {
            register_password.error = "At least 6 characters on password."
            valid = false
        } else{
            register_password.error = null
        }

        return valid
    }

    companion object{

        fun newInstance(): RegisterFragment{
            return RegisterFragment()
        }
    }


}
