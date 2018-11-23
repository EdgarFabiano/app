package br.unb.cic.igor.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import br.unb.cic.igor.MainActivity
import br.unb.cic.igor.R
import br.unb.cic.igor.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_complete_registration.*
import kotlinx.android.synthetic.main.fragment_complete_registration.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CompleteRegistrationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CompleteRegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CompleteRegistrationFragment : Fragment() {

    lateinit var mDatabase: FirebaseFirestore
    lateinit var mAuth: FirebaseAuth

    private var fBaseUser: FirebaseUser? = null

    private val genderList = arrayOf("Feminino", "Masculino")
    private var selectedGender: String? = "Feminino"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseFirestore.getInstance()

        fBaseUser = mAuth.currentUser
        if(fBaseUser == null){
            Toast.makeText(activity, "Error on getting Firebase user.", Toast.LENGTH_LONG).show()
            showLoginFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_complete_registration, container, false)

        view.complete_reg_button.setOnClickListener {
            register()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        complete_reg_gender.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, genderList)

        complete_reg_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedGender = null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGender = genderList[position]
            }
        }
    }

    private fun register() {
        if(!validateForm() || fBaseUser == null || selectedGender == null){
            Toast.makeText(activity, "Form not valid.", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(fBaseUser!!.uid,  fBaseUser!!.email!!, complete_reg_username.text.toString().trim(), null, selectedGender)
        User.Insert(user)

        val u = User.Get(user.id).addOnSuccessListener{
            task ->
            val u = task.toObject(User::class.java)
            if(u == null){
                Toast.makeText(activity, "Error on registration.", Toast.LENGTH_SHORT).show()
            } else{
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        val myIcon = resources.getDrawable(R.drawable.igor_attention)
        myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth()/2, myIcon.getIntrinsicHeight()/2)

        val username = complete_reg_username.text.toString()
        if (TextUtils.isEmpty(username)) {
            complete_reg_username.setError("Required", myIcon)
            valid = false
        } else {
            complete_reg_username.error = null
        }

        val birthdate = complete_reg_birthdate.text.toString()
        if (TextUtils.isEmpty(birthdate)) {
            complete_reg_birthdate.setError("Required", myIcon)
            valid = false
        } else {
            complete_reg_birthdate.error = null
        }

        return valid
    }

    fun showLoginFragment(){
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.login_fragment_holder, LoginFragment(), "LoginFragment Transaction")
        ft.addToBackStack(null)
        ft.commit()
    }

    companion object{

        fun newInstance(): CompleteRegistrationFragment{
            return CompleteRegistrationFragment()
        }
    }

}
