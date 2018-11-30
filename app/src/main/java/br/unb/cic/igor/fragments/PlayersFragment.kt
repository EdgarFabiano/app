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
import br.unb.cic.igor.classes.PlayerContent

import br.unb.cic.igor.fragments.dummy.DummyContent
import br.unb.cic.igor.fragments.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.fragment_players_list.*
import kotlinx.android.synthetic.main.fragment_players_list.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [PlayersFragment.OnPlayersFragmentInteractionListener] interface.
 */
class PlayersFragment : Fragment() {

    private var listener: OnPlayersFragmentInteractionListener? = null
    private var players: List<Player> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_players_list, container, false)

        // Set the adapter
        if (view.players_list is RecyclerView) {
            with(view.players_list) {
                layoutManager = LinearLayoutManager(context)
                adapter = PlayersAdapter(players, listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val fragment = (activity as MainActivity).currentFragment
        if (fragment is OnPlayersFragmentInteractionListener) {
            listener = fragment
        } else {
            throw RuntimeException(fragment.toString() + " must implement OnPlayersFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun updatePlayers(players: List<Player>) {
        this.players = players
        if (players_list != null) {
            val sorted = players.sortedBy {
                it.name
            }

            // Set the adapter
            if (players_list is RecyclerView) {
                with(players_list) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = PlayersAdapter(players, listener)
                }
            }
        }
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
    interface OnPlayersFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onPlayersFragmentInteraction(item: Player?)
    }

    companion object {
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance() = PlayersFragment()
    }
}
