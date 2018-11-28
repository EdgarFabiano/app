package br.unb.cic.igor.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.unb.cic.igor.MainActivity
import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Player

import kotlinx.android.synthetic.main.fragment_messages_list.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [MessagesFragment.OnMessagesFragmentInteractionListener] interface.
 */
class MessagesFragment : Fragment() {

    // TODO: Customize parameters
    private var player: Player? = null
    private var adventureId : String? = null

    private var listener: OnMessagesFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            player = arguments?.getSerializable(PLAYER_ARG_KEY) as Player?
            adventureId = arguments?.getString(ADV_ID_ARG_KEY) as String?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_messages_list, container, false)

        // Set the adapter
        if (view.messages_list is RecyclerView) {
            with(view.messages_list) {
                layoutManager = LinearLayoutManager(context)
                adapter = MessagesAdapter(player!!.messages, listener)
            }
        }

        view.sendButton.setOnClickListener {
            player = Player.AddMessage(view.message.text.toString(), player!!, adventureId!!)
            view.message.setText("")
            // Reset the adapter
            if (view.messages_list is RecyclerView) {
                with(view.messages_list) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = MessagesAdapter(player!!.messages, listener)
                }
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val fragment = (activity as MainActivity).currentFragment
        if (fragment is OnMessagesFragmentInteractionListener) {
            listener = fragment
        }
//        else {
//            throw RuntimeException(fragment.toString() + " must implement OnMessagesFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnMessagesFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onMessagesFragmentInteraction(item: String?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val PLAYER_ARG_KEY = "player_arg_key"
        const val ADV_ID_ARG_KEY = "adv_id_arg_key"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(player: Player?, adventureId: String) =
                MessagesFragment().apply {
                    arguments = Bundle().apply {
                        putString(ADV_ID_ARG_KEY, adventureId)
                        putSerializable(PLAYER_ARG_KEY, player)
                    }
                }
    }
}
