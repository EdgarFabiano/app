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
import android.widget.Toast
import br.unb.cic.igor.MainActivity
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Player
import br.unb.cic.igor.classes.Session
import br.unb.cic.igor.classes.User
import java.util.*
import kotlinx.android.synthetic.main.fragment_add_session.*
import kotlinx.android.synthetic.main.fragment_player_details.*
import kotlinx.android.synthetic.main.fragment_player_details.view.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [PlayerDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PlayerDetailsFragment : Fragment() {
    private val PLAYER_ARG_KEY = "player_arg_key"
    private val ADV_ID_ARG_KEY = "adv_id_arg_key"
    private var player: Player? = null
    private var listener: OnShowMessagesListener? = null
    private var adventureId : String? = null

    init {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = arguments?.getSerializable(PLAYER_ARG_KEY) as Player?
        adventureId = arguments?.getString(ADV_ID_ARG_KEY) as String?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_player_details, container, false)

        view.character.text = player!!.character
        view.playerName.text = player!!.name
        view.attributes.text = player!!.attrs
        view.description.text = player!!.description

        // at first, he cannot see the messages
        view.messages_button.visibility = View.INVISIBLE

        // get the current adventure and check if this user should be able to see the messages or not
        Adventure.Get(adventureId!!).addOnSuccessListener {
            if (it != null) {
                val adventure = it.toObject(Adventure::class.java)
                val currentUser = User.GetInstance()!!
                if(adventure!!.master.userId == currentUser.id ||
                        player!!.userId == currentUser.id){
                    view.messages_button.visibility = View.VISIBLE
                }
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val fragment = (activity as MainActivity).currentFragment
        if (fragment is OnShowMessagesListener) {
            listener = fragment
        } else {
            throw RuntimeException(fragment.toString() + " must implement OnShowMessagesListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        messages_button.setOnClickListener {
            listener?.onShowMessagesClick(player, adventureId!!)
        }
    }

    interface OnShowMessagesListener {
        fun onShowMessagesClick(player: Player?, adventureId: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PlayerDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(player: Player?, adventureId: String) =
                PlayerDetailsFragment().apply {
                    val bundle = Bundle()
                    bundle.putSerializable(PLAYER_ARG_KEY, player)
                    bundle.putString(ADV_ID_ARG_KEY, adventureId)
                    arguments = bundle
                }
    }
}
