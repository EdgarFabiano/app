package br.unb.cic.igor.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.unb.cic.igor.R
import kotlinx.android.synthetic.main.fragment_add_session.view.*
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import br.unb.cic.igor.MainActivity
import br.unb.cic.igor.classes.Player
import br.unb.cic.igor.classes.Session
import br.unb.cic.igor.classes.User
import br.unb.cic.igor.extensions.toList
import kotlinx.android.synthetic.main.fragment_add_player.*
import kotlinx.android.synthetic.main.fragment_add_player.view.*
import java.util.*
import kotlinx.android.synthetic.main.fragment_add_session.*
import kotlinx.android.synthetic.main.fragment_player_details.view.*
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 * Use the [AddPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AddPlayerFragment : Fragment() {

    private val ADV_ID_ARG_KEY = "adv_id_arg_key"
    private var listener: AddPlayerListener? = null
    private var adventureId : String? = null
    private var selectedUser : User? = null


    init {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adventureId = arguments?.getString(ADV_ID_ARG_KEY) as String?
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = (activity as MainActivity).currentFragment
        if (fragment is AddPlayerListener) {
            listener = fragment
        } else {
            throw RuntimeException(fragment.toString() + " must implement AddPlayerListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_player, container, false)

        view.addButton.setOnClickListener {
            val char = character.text.toString()
            val atts = atts.text.toString()
            val desc = desc.text.toString()
            if (selectedUser == null || char == "" || atts == "" ) {
                toast("Por favor, é preciso que você digite todos os campos. O único opcional é o de descrição.")
            } else {
                var player = Player(userId = selectedUser!!.id, name = selectedUser!!.username, character = char, attrs = atts, description = desc)
                Player.Insert(player, selectedUser!!, adventureId!!)
                listener!!.playerCreated()
                toast("Jogador adicionado com sucesso!")
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        User.List().addOnSuccessListener{
            val list = it.toList(User::class.java)

            userSpinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_selectable_list_item, list.filter { u -> u.username != User.GetInstance()!!.username }.map{ u -> u.username })

            userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedUser = null
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedUser = list.filter { u -> u.username != User.GetInstance()!!.username }[position]
                }
            }
        }


    }

    interface AddPlayerListener {
        fun playerCreated()
    }

    private fun toast(message: String) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AddPlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(adventureId: String) =
                AddPlayerFragment().apply {
                    val bundle = Bundle()
                    bundle.putString(ADV_ID_ARG_KEY, adventureId)
                    arguments = bundle
                }
    }
}
