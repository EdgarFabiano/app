package br.unb.cic.igor.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Session
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
class AdventureTabsFragment : Fragment(), AdventureFragment.OnSessionSelectedListener {
    private var state: State = State.ADVENTURE
    private var adventureFragment: Fragment = AdventureFragment.newInstance()
    private var playersFragment: Fragment = PlayersFragment.newInstance(1)
    private var currentFragment: Fragment = adventureFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handles action bar item clicks here.
        when (item.itemId) {
            R.id.action_editar -> {
                toast("${resources.getString(R.string.editar)} $state")
                return true
            }
            R.id.action_ordenar -> {
                toast("${resources.getString(R.string.ordenar)} $state")
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun toast(message: String) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_adventure_tabs, container, false)
        // Inflate the layout for this fragment

        view.playersTab.setOnClickListener {
            if (state != State.PLAYERS) {
                onTabPressed(State.PLAYERS)
                contentView.setImageResource(R.drawable.players_tab)
            }
        }

        view.adventureTab.setOnClickListener {
            if (state != State.ADVENTURE) {
                onTabPressed(State.ADVENTURE)
                contentView.setImageResource(R.drawable.adventure_progress_tab)
            }
        }

        view.addButton.setOnClickListener {
            onAddButtonPressed()
        }

        val ft = fragmentManager?.beginTransaction();
        ft?.replace(R.id.contentFrame, adventureFragment);
        ft?.commit()

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onTabPressed(selection: State) {
        state = selection

        val fragment: Fragment = when (selection) {
            State.ADVENTURE -> adventureFragment
            else -> playersFragment
        }

        switchContent(fragment)
    }

    private fun onAddButtonPressed() {
        when (state) {
            State.ADVENTURE -> {
                state = State.SESSION_CREATE
                switchContent(AddSessionFragment())
            }
            else -> toast("wrong state")
        }
    }

    private fun switchContent(contentFragment: Fragment) {
        currentFragment = contentFragment

        val ft = fragmentManager?.beginTransaction()

        ft?.replace(R.id.contentFrame, contentFragment)

        ft?.commit()
    }


    public fun onBackPressed() {
        val frag : Fragment? = when (state) {
            State.SESSION, State.SESSION_CREATE -> {
                state = State.ADVENTURE
                adventureFragment
            }
            else -> null
        }

        if (frag != null) {
            switchContent(frag)
        }
    }


    override fun onDetach() {
        super.onDetach()
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

    override fun onSessionSelected(session: Session) {
        state = State.SESSION
        switchContent(SessionFragment.newInstance(session))
    }

    private enum class State {
        ADVENTURE,
        PLAYERS,
        SESSION,
        SESSION_CREATE,
        SESSION_EDIT
    }
}
