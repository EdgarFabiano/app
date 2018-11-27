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
class CombatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var adventure: Adventure? = null
    private var listener: OnCombatFinished? = null
    private var combat: Combat? = null
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
                updateState()
            }
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
                toast("REVIEWING ACTIONS!")
            TurnState.WAITING_ROLLS ->
                toast("WAITING ROLLS!")
            TurnState.ENDING_COMBAT ->
                toast("FINISHING COMBAT!")
        }

    }

    private fun startingTurn(){
        if(isMaster!!){
            waiting_text.visibility = View.GONE
            loadStartTurn()
        } else{
            waiting_text.visibility = View.VISIBLE
        }
    }

    private fun waitingActions(){
        if(isMaster!!){
            waiting_text.visibility = View.VISIBLE
        } else{
            waiting_text.visibility = View.GONE
            loadPlayerActionCreation()
        }
    }

    fun checkAllPlayerActions(){
        PlayerAction.List(adventure!!.id, adventure!!.combatInfo.sessionId, combat!!.id).addOnSuccessListener {
            val docs = it.documents
            val actionList = ArrayList<PlayerAction>()
            for (doc in docs){
                val value = doc.toObject(PlayerAction::class.java)
                if(value != null){
                    actionList.add(value)
                }
            }

            if(actionList.size == combat!!.currentTurn.availablePlayers.size){
                for(userId in combat!!.currentTurn.availablePlayers){
                    if(actionList.filter {it.userId == userId}.size != 1){
                        return@addOnSuccessListener
                    }
                }

                combat!!.currentTurn.status = TurnState.REVIEWING_ACTIONS
                Combat.Update(adventure!!.id, adventure!!.combatInfo.sessionId, combat!!)

                updateState()
            }
        }
    }

    private fun loadStartTurn(){
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.combat_inner_fragment, StartTurnFragment.newInstance(adventure!!, combat!!, isMaster!!), "StartTurnFragment Transaction")
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
