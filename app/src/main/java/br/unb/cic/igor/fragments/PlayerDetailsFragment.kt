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
import android.widget.Toast
import br.unb.cic.igor.classes.Player
import br.unb.cic.igor.classes.Session
import java.util.*
import kotlinx.android.synthetic.main.fragment_add_session.*
import kotlinx.android.synthetic.main.fragment_player_details.view.*
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 * Use the [PlayerDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PlayerDetailsFragment : Fragment() {
    private val PLAYER_ARG_KEY = "session_arg_key"
    private var player: Player? = null

    init {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = arguments?.getSerializable(PLAYER_ARG_KEY) as Player?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_player_details, container, false)

        view.character.text = player!!.character
        view.playerName.text = player!!.name
        view.attributes.text = player!!.attrs
        view.description.text = player!!.description

        return view
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
        fun newInstance(player: Player?) =
                PlayerDetailsFragment().apply {
                    val bundle = Bundle()
                    bundle.putSerializable(PLAYER_ARG_KEY, player)
                    arguments = bundle
                }
    }
}
