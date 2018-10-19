package br.unb.cic.igor.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.unb.cic.igor.R
import kotlinx.android.synthetic.main.fragment_adventure_tabs.*
import kotlinx.android.synthetic.main.fragment_adventure_tabs.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AdventureTabsFragment.OnTabSelectionListener] interface
 * to handle interaction events.
 * Use the [AdventureTabsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AdventureTabsFragment : Fragment() {
    private var listener: OnTabSelectionListener? = null
    private var selectedTab: String = "adventure"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_adventure_tabs, container, false)
        // Inflate the layout for this fragment

        view.playersTab.setOnClickListener {
            if (selectedTab != "players") {
                onTabPressed("players")
                contentView.setImageResource(R.drawable.players_tab)
            }
        }

        view.adventureTab.setOnClickListener {
            if (selectedTab != "adventure") {
                onTabPressed("adventure")
                contentView.setImageResource(R.drawable.adventure_progress_tab)
            }
        }

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onTabPressed(selection: String) {
        selectedTab = selection
        listener?.onFragmentInteraction(selection)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTabSelectionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnTabSelectionListener")
        }
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
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnTabSelectionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(selection: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AdventureTabsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                AdventureTabsFragment()
    }
}
