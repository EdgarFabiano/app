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
import kotlinx.android.synthetic.main.fragment_player_edit.*
import kotlinx.android.synthetic.main.fragment_player_edit.view.*
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 * Use the [PlayerEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PlayerEditFragment : Fragment() {

    private var listener: PlayerEditListener? = null
    private var adventureId : String? = null
    private var player : Player? = null

    init {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adventureId = arguments?.getString(ADV_ID_ARG_KEY) as String?
        player = arguments?.getSerializable(PLAYER_ARG_KEY) as Player?
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = (activity as MainActivity).currentFragment
        if (fragment is PlayerEditListener) {
            listener = fragment
        } else {
            throw RuntimeException(fragment.toString() + " must implement PlayerEditListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_player_edit, container, false)

        view.character.setText(player!!.character)
        view.atts.setText(player!!.attrs)
        view.desc.setText(player!!.description)

        view.updateButton.setOnClickListener {
            val char = character.text.toString()
            val atts = atts.text.toString()
            val desc = desc.text.toString()
            if (char == "" || atts == "" ) {
                toast("Por favor, é preciso que você digite todos os campos. O único opcional é o de descrição.")
            } else {
                player?.character = char
                player?.attrs = atts
                player?.description = desc
                Player.Update(player!!, adventureId!!)
                listener!!.playerChanged()
                toast("Jogador atualizado com sucesso!")
            }
        }

        return view
    }

    interface PlayerEditListener {
        fun playerChanged()
    }

    private fun toast(message: String) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val ADV_ID_ARG_KEY = "adv_id_arg_key"
        const val PLAYER_ARG_KEY = "player_arg_key"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PlayerEditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(player: Player?, adventureId: String) =
                PlayerEditFragment().apply {
                    val bundle = Bundle()
                    bundle.putString(ADV_ID_ARG_KEY, adventureId)
                    bundle.putSerializable(PLAYER_ARG_KEY, player)
                    arguments = bundle
                }
    }
}
