package br.unb.cic.igor.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import br.unb.cic.igor.MainActivity
import br.unb.cic.igor.R
import br.unb.cic.igor.classes.*
import br.unb.cic.igor.adapters.AdventuresAdapter
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Player
import br.unb.cic.igor.classes.Session
import br.unb.cic.igor.extensions.toList
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
class AdventureTabsFragment : Fragment(), AdventureFragment.OnSessionSelectedListener,
        PlayersFragment.OnPlayersFragmentInteractionListener,
        PlayerDetailsFragment.OnShowMessagesListener,
        AddSessionFragment.AddSessionListener,
        AdventureEditFragment.EditAdventureListener,
        SessionEditFragment.SessionEditListener,
        AddPlayerFragment.AddPlayerListener,
        PlayerEditFragment.PlayerEditListener,
        SessionFragment.onCombatsClickListener {

    private val ADVENTURE_ID_ARG : String = "session_arg_key"

    private var listener: OnAdventureTabsFragmentInteractionListener? = null

    private var state: State = State.ADVENTURE
    private var adventureFragment: Fragment = AdventureFragment.newInstance()
    private var playersFragment: Fragment = PlayersFragment.newInstance()
    private var currentFragment: Fragment = adventureFragment
    private var selectedSession: Session? = null
    private lateinit var adventureId: String
    private var adventure : Adventure? = null
    private var sessions: List<Session> = ArrayList()
    private var players: List<Player> = ArrayList()
    private var combatListener: OnCombatStarted? = null
    private var lastSelectedPlayer: Player?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        adventureId = arguments!!.getString(ADVENTURE_ID_ARG)!!

        loadAdventure()

        loadSessions()

        loadPlayers()
    }

    fun loadPlayers() {
        Player.ListByAdventure(adventureId).addOnSuccessListener {
            if (it != null) {
                players = it.toList(Player::class.java)
                (playersFragment as PlayersFragment).updatePlayers(players)
            }
        }
    }

    fun loadAdventure() {
        Adventure.Get(adventureId).addOnSuccessListener {adv ->
            if (adv != null) {
                adventure = adv.toObject(Adventure::class.java)
                if (adventure!!.combatInfo.inCombat) {
                    combatListener!!.onCombatStarted(adventure!!)
                } else {

                    (adventureFragment as AdventureFragment).updateAdventure(adventure!!)
                    adventureBg.setBackgroundResource(AdventuresAdapter.images[adventure!!.bg])
                    adventureTitle.text = adventure!!.name
                }
            }
        }
    }

    fun loadSessions() {
        Session.ListByAdventure(adventureId).addOnSuccessListener {
            if (it != null) {
                sessions = it.toList(Session::class.java)
                (adventureFragment as AdventureFragment).updateSessions(sessions)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onDetach() {
        super.onDetach()
        combatListener = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handles action bar item clicks here.
        when (item.itemId) {
            R.id.action_editar -> {
                when (state) {
                    State.SESSION ->
                        stateTransition(State.SESSION_EDIT, SessionEditFragment.newInstance(selectedSession!!))
                    State.ADVENTURE ->
                        stateTransition(State.ADV_EDIT, AdventureEditFragment.newInstance(adventure!!))
                    State.PLAYER_DETAILS ->
                        if(adventure!!.master.userId == User.GetInstance()!!.id ||
                                lastSelectedPlayer!!.userId == User.GetInstance()!!.id){
                            stateTransition(State.PLAYER_EDIT, PlayerEditFragment.newInstance(lastSelectedPlayer, adventureId))
                        } else{
                            toast("You cannot edit this player, since you are not the master nor the clicked player.")
                        }
                    else ->
                        toast("invalid state")
                }

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
                stateTransition(State.PLAYERS, playersFragment)
            }
        }

        view.adventureTab.setOnClickListener {
            if (state != State.ADVENTURE) {
                stateTransition(State.ADVENTURE, adventureFragment)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnCombatStarted) {
            combatListener = context
        }

        if (activity is OnAdventureTabsFragmentInteractionListener) {
            listener = activity as MainActivity
        } else {
            throw RuntimeException(activity.toString() + " must implement OnAdventureTabsFragmentInteractionListener")
        }
    }

    private fun onAddButtonPressed() {
        when (state) {
            State.ADVENTURE -> {
                stateTransition(State.SESSION_CREATE, AddSessionFragment.newInstance(adventureId))
            }
            State.PLAYERS -> {
                stateTransition(State.PLAYER_ADD, AddPlayerFragment.newInstance(adventureId))
            }
            State.COMBATS -> {
                toast("Create combat")
            }
            State.SESSION -> {
                var newCombat = Combat()
                Combat.Insert(adventureId, selectedSession!!.id, newCombat).addOnSuccessListener {
                    adventure!!.combatInfo = CombatInfo(true, selectedSession!!.id, newCombat.id)
                    Adventure.Update(adventure!!).addOnSuccessListener {
                        combatListener!!.onCombatStarted(adventure!!)
                    }
                }
            }
            else -> toast("wrong state")
        }
    }

    fun stateTransition(nextState : State, fragment : Fragment) {
        switchContent(fragment)
        when (nextState) {
            State.SESSION_CREATE, State.SESSION_EDIT, State.PLAYER_ADD, State.MESSAGES_LIST, State.PLAYER_EDIT -> {
                addButton.visibility = View.INVISIBLE
                setHasOptionsMenu(false)
            }
            State.ADVENTURE -> {
                contentView.setImageResource(R.drawable.adventure_progress_tab)
                addButton.visibility = View.VISIBLE
                setHasOptionsMenu(true)
                addButton.setImageResource(R.drawable.add_sesssion)
                val cast = fragment as AdventureFragment
                cast.updateAdventure(adventure!!)
                cast.updateSessions(sessions)
            }
            State.PLAYERS -> {
                contentView.setImageResource(R.drawable.players_tab)
                addButton.visibility = View.VISIBLE
                setHasOptionsMenu(true)
                if(adventure!!.master.userId == User.GetInstance()!!.id){
                    addButton.setImageResource(R.drawable.add_player)
                } else{
                    addButton.visibility = View.INVISIBLE
                }
            }
            State.SESSION -> {
                addButton.visibility = View.VISIBLE
                addButton.setImageResource(R.drawable.start_combat)
            }
            State.PLAYER_DETAILS -> {
                addButton.visibility = View.INVISIBLE
                setHasOptionsMenu(true)
            }
            State.ADV_EDIT -> {
                addButton.visibility = View.INVISIBLE
                setHasOptionsMenu(false)
            }
            State.COMBATS, State.COMBAT_VIEW -> {
                addButton.visibility = View.VISIBLE
                setHasOptionsMenu(false)
            }
        }

        state = nextState
    }

    private fun switchContent(contentFragment: Fragment) {
        currentFragment = contentFragment

        val ft = fragmentManager?.beginTransaction()

        ft?.replace(R.id.contentFrame, contentFragment)

        ft?.commit()
    }

    fun onBackPressed() {
        when (state) {
            State.SESSION, State.SESSION_CREATE, State.SESSION_EDIT -> {
                stateTransition(State.ADVENTURE, adventureFragment)
            }
            State.PLAYER_DETAILS, State.PLAYER_ADD -> {
                stateTransition(State.PLAYERS, playersFragment)
            }
            State.MESSAGES_LIST -> {
                stateTransition(State.PLAYER_DETAILS, PlayerDetailsFragment.newInstance(lastSelectedPlayer, adventureId))
            }
            State.ADVENTURE -> {
                listener?.adventureTabsFragmentWantsToGoBack()
            }
            State.COMBATS, State.COMBAT_VIEW -> {
                stateTransition(State.ADVENTURE, adventureFragment)
            }
            else -> {
                stateTransition(State.ADVENTURE, adventureFragment)
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
    interface OnAdventureTabsFragmentInteractionListener {
        fun adventureTabsFragmentWantsToGoBack()
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
        fun newInstance(adventureId: String) =
                AdventureTabsFragment().apply {
                    val bundle = Bundle()
                    bundle.putString(ADVENTURE_ID_ARG, adventureId)
                    arguments = bundle
                }
    }

    override fun onSessionSelected(session: Session) {
        selectedSession = session
        stateTransition(State.SESSION, SessionFragment.newInstance(session))
    }

    override fun onPlayersFragmentInteraction(item: Player?){
        lastSelectedPlayer = item
        stateTransition(State.PLAYER_DETAILS, PlayerDetailsFragment.newInstance(item, adventureId))
    }

    override fun onShowMessagesClick(player: Player?, adventureId: String) {
        stateTransition(State.MESSAGES_LIST, MessagesFragment.newInstance(lastSelectedPlayer, adventureId))
    }

    override fun sessionCreated() {
        loadSessions()
        stateTransition(State.ADVENTURE, adventureFragment)
    }

    override fun playerCreated(){
        loadPlayers()
        stateTransition(State.PLAYERS, playersFragment)
    }

    override fun playerChanged(){
        loadPlayers()
        stateTransition(State.PLAYER_DETAILS, PlayerDetailsFragment.newInstance(lastSelectedPlayer, adventureId))
    }

    override fun adventureChanged() {
        loadAdventure()
        stateTransition(State.ADVENTURE, adventureFragment)
    }

    override fun sessionChanged(session: Session) {
        loadSessions()
        stateTransition(State.SESSION, SessionFragment.newInstance(session))
    }

    interface OnCombatStarted {
        fun onCombatStarted(adventure: Adventure)
    }

    override fun onCombatsClick(session: Session) {
        stateTransition(State.COMBATS, CombatsFragment.newInstance(session))
    }

    enum class State {
        ADVENTURE,
        ADV_EDIT,
        MESSAGES_LIST,
        PLAYERS,
        PLAYER_DETAILS,
        PLAYER_ADD,
        PLAYER_EDIT,
        SESSION,
        SESSION_CREATE,
        SESSION_EDIT,
        COMBATS,
        COMBAT_VIEW
    }
}
