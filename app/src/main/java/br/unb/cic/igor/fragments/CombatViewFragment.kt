package br.unb.cic.igor.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Combat
import kotlinx.android.synthetic.main.fragment_combat_view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val COMBAT_ARG = "combat_arg"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CombatViewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CombatViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CombatViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var combat: Combat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            combat = it.getSerializable(COMBAT_ARG) as Combat
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_combat_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        combatDescriptions.text = combat!!.turns.map { it.description }.joinToString(separator = "\n\n")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CombatViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(combat: Combat) =
                CombatViewFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(COMBAT_ARG, combat)
                    }
                }
    }
}
