package br.unb.cic.igor.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import br.unb.cic.igor.R
import br.unb.cic.igor.adapters.PlayerSelectionAdapter
import br.unb.cic.igor.classes.*
import kotlinx.android.synthetic.main.fragment_action_rate.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_start_turn.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ADV = "ARG_ADV"
private const val ARG_CBT = "ARG_CBT"
private const val ARG_MASTER = "ARG_MASTER"
private const val ARG_ACTIONS = "ARG_ACTIONS"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [StartTurnFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [StartTurnFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class StartTurnFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var adventure: Adventure
    private lateinit var combat: Combat
    private lateinit var playerActions: ArrayList<PlayerAction>
    private var isMaster: Boolean = false
    private var listener: OnTurnStarted? = null
    private var players: ArrayList<Player> = ArrayList()
    private var turnUsers: ArrayList<String> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: PlayerSelectionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adventure = it.getSerializable(ARG_ADV) as Adventure
            combat = it.getSerializable(ARG_CBT) as Combat
            playerActions = it.getSerializable(ARG_ACTIONS) as ArrayList<PlayerAction>
            isMaster = it.getBoolean(ARG_MASTER)
        }

        Adventure.ListPlayers(adventure.id).addOnSuccessListener{ task ->
            val list = task.documents

            players = ArrayList()
            for(doc in list){
                Toast.makeText(activity, "Success!", Toast.LENGTH_SHORT).show()
                val player = doc.toObject(Player::class.java) as Player
                players.add(player)
                turnUsers.add(player.userId)
            }

            linearLayoutManager = LinearLayoutManager(activity)
            recyclerView.layoutManager = linearLayoutManager

            adapter = PlayerSelectionAdapter(players) {
                it -> {
                    if(turnUsers.contains(it)){
                        turnUsers.remove(it)
                    } else{
                        turnUsers.add(it)
                    }
                }
            }
            recyclerView.adapter = adapter

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_turn, container, false)
    }

    fun updateUI() {
        val pagerAdapter = ScreenSlidePagerAdapter(fragmentManager!!, ArrayList(playerActions))
        reviewActionPager.adapter = pagerAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchFinish.setOnCheckedChangeListener { btn, checked ->
            if (checked) {
                recyclerView.visibility = View.GONE
                start_turn_title.text = "Finalizar Combate"
            } else {
                recyclerView.visibility = View.VISIBLE
                start_turn_title.text = "Início de turno"
            }
        }

        if (combat.currentTurn.id > 0) {
            reviewActionPager.visibility = View.VISIBLE
            updateUI()
        } else {
            reviewActionPager.visibility = View.GONE
        }

        start_turn_btn.setOnClickListener {
            startTurn()
        }
    }

    private fun startTurn(){
        if(!validateForm()){
            return
        }
        start_turn_btn.isClickable = false
        val desc = turn_description.text.toString()
        if (switchFinish.isChecked) {
            combat.currentTurn.status = TurnState.ENDING_COMBAT
            combat.currentTurn.availablePlayers = ArrayList(players.map { it -> it.userId })
            Combat.Update(adventure.id, adventure.combatInfo.sessionId, combat).addOnSuccessListener {
                listener!!.OnTurnStarted(adventure, combat)
            }
        } else {

            combat.currentTurn = Turn().apply {
                id = combat.turns.size
                description = desc
                availablePlayers = turnUsers
                status = TurnState.WAITING_ACTIONS
            }

            Combat.Update(adventure.id, adventure.combatInfo.sessionId, combat)

            listener!!.OnTurnStarted(adventure, combat)

            //Remove itself

            fragmentManager!!.beginTransaction().remove(this).commit()
        }
    }


    private fun validateForm(): Boolean {
        var valid = true
        val myIcon = resources.getDrawable(R.drawable.igor_attention)
        myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth()/2, myIcon.getIntrinsicHeight()/2)

        val description = turn_description.text.toString()
        if (TextUtils.isEmpty(description)) {
            turn_description.setError("Required", myIcon)
            valid = false
        } else {
            turn_description.error = null
        }

        return valid
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTurnStarted) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnTurnStarted")
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
    interface OnTurnStarted {
        // TODO: Update argument type and name
        fun OnTurnStarted(adventure: Adventure, combat: Combat)
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager, val actions: ArrayList<PlayerAction>) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = actions.size

        override fun getItem(position: Int): Fragment = ActionReviewFragment.newInstance(actions[position])
    }

    companion object {

        @JvmStatic
        fun newInstance(adventure: Adventure, combat: Combat, isMaster: Boolean, actions: ArrayList<PlayerAction>) =
                StartTurnFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_ADV, adventure)
                        putSerializable(ARG_CBT, combat)
                        putSerializable(ARG_ACTIONS, actions)
                        putBoolean(ARG_MASTER, isMaster)
                    }
                }
    }
}
