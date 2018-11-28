package br.unb.cic.igor.fragments

import android.content.Context
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.unb.cic.igor.MainActivity

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.*
import br.unb.cic.igor.extensions.toList
import kotlinx.android.synthetic.main.fragment_combat.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_SUB_PARAM = "COMBAT_SUB_PARAM"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CombatFragment.OnCombatFinished] interface
 * to handle interaction events.
 * Use the [CombatFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CombatFragment : Fragment(), ActionRateFragment.OnActionRatesDoneListener, ActionResultFragment.OnActionResultListener, ViewCombatEndFragment.OnCombatEndViewedListener {

    private var adventure: Adventure? = null
    private var listener: OnCombatFinished? = null
    private var combat: Combat? = null
    private var playerActions: List<PlayerAction> = ArrayList()
    private var allPlayerActions: List<PlayerAction> = ArrayList()
    private var currentFragment: Fragment? = null
    private var isMaster: Boolean? = null
    private var waiting: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adventure = it.getSerializable(ARG_SUB_PARAM) as Adventure
        }

        isMaster = adventure!!.master.userId == User.GetInstance()!!.id

        loadCombat()
    }

    fun loadCombat() {
        Combat.Get(adventure!!.id, adventure!!.combatInfo.sessionId, adventure!!.combatInfo.combatId).addOnSuccessListener {
            if (it != null) {
                combat = it.toObject(Combat::class.java)
                loadPlayerActions()
            }
        }
    }

    fun loadPlayerActions() {
        PlayerAction.List(adventure!!.id, adventure!!.combatInfo.sessionId, adventure!!.combatInfo.combatId).addOnSuccessListener {
            allPlayerActions = it.toList(PlayerAction::class.java)
            playerActions = allPlayerActions.filter { pa ->
                pa.turnId == combat!!.currentTurn.id
            }
            updateState()
        }
    }

    fun updateAdventure(adventure: Adventure? = null, combat: Combat? = null){
        if(adventure == null){
            Adventure.Get(adventure!!.id).addOnSuccessListener {
                if (it != null) {
                    this.adventure = it.toObject(Adventure::class.java)
                    if(combat == null){
                        loadCombat()
                    } else{
                        this.combat = combat
                        updateState()
                    }
                }
            }
        } else{
            this.adventure = adventure
            if(combat == null){
                loadCombat()
            } else{
                this.combat = combat
                updateState()
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!adventure!!.combatInfo.inCombat) {
            listener!!.onCombatFinished(adventure!!.id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_combat, container, false)
    }

    fun updateState() {
        when (combat!!.currentTurn.status) {
            TurnState.NOT_STARTED->
                startingTurn()
            TurnState.STARTING ->
                startingTurn()
            TurnState.WAITING_ACTIONS ->
                waitingActions()
            TurnState.REVIEWING_ACTIONS ->
                if (isMaster!!) {
                    switchContent(ActionRateFragment.newInstance(adventure!!, combat!!))
                } else {
                    showWaiting()
                }
            TurnState.WAITING_ROLLS -> {
                if (!isMaster!! ) {
                    val thisPlayerAction = playerActions.firstOrNull { it.userId == User.GetInstance()!!.id }
                    if (thisPlayerAction != null) {
                        switchContent(ActionResultFragment.newInstance(adventure!!, combat!!, thisPlayerAction))
                    } else {
                        showWaiting()
                    }
                } else if (isMaster!!) {
                    val completedActions = playerActions.filter { a -> a.actionResult != null }
                    showWaiting()
                    if (completedActions.size >= combat!!.currentTurn.availablePlayers.size && combat!!.currentTurn.availablePlayers.all { completedActions.any {a -> a.userId == it} }) {
                        val finishedTurn = combat!!.currentTurn
                        finishedTurn.status = TurnState.FINISHED
                        combat!!.turns.add(finishedTurn)
                        combat!!.currentTurn = Turn(id = finishedTurn.id++)
                        Combat.Update(adventure!!.id, adventure!!.combatInfo.sessionId, combat!!).addOnSuccessListener {
                            loadCombat()
                        }
                    }
                }
            }
            TurnState.ENDING_COMBAT ->
                if (isMaster!!) {
                    showWaiting()
                    if (playerActions.size >= combat!!.currentTurn.availablePlayers.size && combat!!.currentTurn.availablePlayers.all { playerActions.any {a -> a.userId == it} }) {
                        val finishedTurn = combat!!.currentTurn
                        finishedTurn.status = TurnState.FINISHED
                        combat!!.turns.add(finishedTurn)
                        combat!!.currentTurn = Turn()
                        Combat.Update(adventure!!.id, adventure!!.combatInfo.sessionId, combat!!).addOnSuccessListener {
                            adventure!!.combatInfo = CombatInfo()
                            Adventure.Update(adventure!!).addOnSuccessListener { _ ->
                                listener!!.onCombatFinished(adventure!!.id)
                            }
                        }
                    }
                } else {
                    val thisPlayerAction = playerActions.firstOrNull { it.userId == User.GetInstance()!!.id }
                    if (thisPlayerAction != null) {
                        showWaiting()
                    } else {
                        switchContent(ViewCombatEndFragment.newInstance(adventure!!, combat!!))
                    }
                }
        }

    }

    fun showWaiting() {
        waiting_text.visibility = View.VISIBLE
        combat_inner_fragment.visibility = View.GONE
    }

    fun hideWaiting() {
        waiting_text.visibility = View.GONE
        combat_inner_fragment.visibility = View.VISIBLE
    }

    private fun startingTurn(){
        if(isMaster!!){
            hideWaiting()
            loadStartTurn()
        } else{
            showWaiting()
        }
    }

    private fun waitingActions(){
        if(isMaster!!){
            showWaiting()
            if (playerActions.size >= combat!!.currentTurn.availablePlayers.size && combat!!.currentTurn.availablePlayers.all { playerActions.any {a -> a.userId == it} }) {
                combat!!.currentTurn.status = TurnState.REVIEWING_ACTIONS
                Combat.Update(adventure!!.combatInfo.sessionId, adventure!!.id, combat!!)
                loadCombat()
            }
        } else{
            hideWaiting()
            loadPlayerActionCreation()
        }
    }

    private fun switchContent(fragment: Fragment) {
        currentFragment = fragment
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.combat_inner_fragment, fragment)
        ft.commit()
    }

    private fun loadStartTurn(){
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.combat_inner_fragment, StartTurnFragment.newInstance(adventure!!, combat!!, isMaster!!, ArrayList(allPlayerActions)), "StartTurnFragment Transaction")
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun loadPlayerActionCreation(){
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.combat_inner_fragment, PlayerActionCreateFragment.newInstance(adventure!!, combat!!), "PlayerActionCreateFragment Transaction")
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCombatFinished) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCombatFinished")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun toast(message: String) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
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
    interface OnCombatFinished {
        fun onCombatFinished(adventureId: String)
    }

    override fun onActionRatesDone(ratedActions: ArrayList<PlayerAction>) {
        PlayerAction.BatchUpdate(adventure!!.id, adventure!!.combatInfo.sessionId, adventure!!.combatInfo.combatId, ratedActions)
        loadCombat()
    }

    override fun onActionResult(playerAction: PlayerAction) {
        PlayerAction.Update(adventure!!.id, adventure!!.combatInfo.sessionId, adventure!!.combatInfo.combatId, playerAction).addOnSuccessListener {
            (currentFragment as ActionResultFragment).updatePlayerAction(playerAction)
        }
    }

    override fun onCombatEndViewed() {
        val playerAction = PlayerAction(turnId = combat!!.currentTurn.id, userId = User.GetInstance()!!.id)
        PlayerAction.Insert(adventure!!.id, adventure!!.combatInfo.sessionId, adventure!!.combatInfo.combatId, playerAction).addOnSuccessListener {
            loadCombat()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param2 Parameter 2.
         * @return A new instance of fragment CombatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(adventure: Adventure) =
                CombatFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_SUB_PARAM, adventure)
                    }
                }
    }
}
