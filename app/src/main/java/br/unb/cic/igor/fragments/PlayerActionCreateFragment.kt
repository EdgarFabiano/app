package br.unb.cic.igor.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Combat
import br.unb.cic.igor.classes.PlayerAction
import br.unb.cic.igor.classes.User
import kotlinx.android.synthetic.main.fragment_player_action_create.*
import kotlinx.android.synthetic.main.fragment_start_turn.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ADV = "ARG_ADV"
private const val ARG_CBT = "ARG_CBT"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PlayerActionCreateFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PlayerActionCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PlayerActionCreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var adventure: Adventure
    private lateinit var combat: Combat
    private var action: PlayerAction? = null
    private var listener: OnPlayerActionCreated? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adventure = it.getSerializable(ARG_ADV) as Adventure
            combat = it.getSerializable(ARG_CBT) as Combat
        }

        PlayerAction.GetByTurn(adventure.id, adventure.combatInfo.sessionId, combat.id, combat.currentTurn.id, User.GetInstance()!!.id).addOnSuccessListener {
            val docs = it.documents

            if(docs.size > 0){
                action = docs.get(0).toObject(PlayerAction::class.java)
                if(action != null){
                    player_action.text.clear()
                    player_action.text.append(action!!.description)
                }

            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_action_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        player_action_submit.setOnClickListener {
            saveAction()
        }

        description.text = combat.currentTurn.description
    }

    private fun saveAction(){
        if(!validateForm()){
            return
        }

        val action_text = player_action.text.toString()

        if(action != null){
            action!!.description = action_text

            PlayerAction.Update(adventure.id, adventure.combatInfo.sessionId, combat.id, action!!)

        } else{
            action = PlayerAction().apply {
                description = action_text
                turnId = combat.currentTurn.id
                userId = User.GetInstance()!!.id
            }

            PlayerAction.Insert(adventure.id, adventure.combatInfo.sessionId, combat.id, action!!)
        }

        //Remove itself

        Toast.makeText(context, "Ação salva com sucesso!", Toast.LENGTH_LONG).show()

        listener!!.OnPlayerActionCreated()

        //Remove itself

        fragmentManager!!.beginTransaction().remove(this).commit()

    }

    private fun validateForm(): Boolean {
        var valid = true
        val myIcon = resources.getDrawable(R.drawable.igor_attention)
        myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth()/2, myIcon.getIntrinsicHeight()/2)

        val description = player_action.text.toString()
        if (TextUtils.isEmpty(description)) {
            player_action.setError("Required", myIcon)
            valid = false
        } else {
            player_action.error = null
        }

        return valid
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPlayerActionCreated) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnPlayerActionCreated")
        }
    }

    interface OnPlayerActionCreated{
        fun OnPlayerActionCreated()
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
        @JvmStatic
        fun newInstance(adventure: Adventure, combat: Combat) =
                PlayerActionCreateFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_ADV, adventure)
                        putSerializable(ARG_CBT, combat)
                    }
                }
    }
}
