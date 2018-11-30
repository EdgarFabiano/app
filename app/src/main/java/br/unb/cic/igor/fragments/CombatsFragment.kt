package br.unb.cic.igor.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import br.unb.cic.igor.MainActivity
import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Combat
import br.unb.cic.igor.classes.Session
import br.unb.cic.igor.extensions.toList
import kotlinx.android.synthetic.main.fragment_combats.*
import java.util.stream.Collectors


class CombatsFragment : Fragment() {
    private val SESSION_ARG_KEY = "session_arg_key"
    private var session: Session? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = arguments?.getSerializable(SESSION_ARG_KEY) as Session?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_combats, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Combat.List(sessionId = session!!.id, adventureId = session!!.adventureId).addOnSuccessListener {
            if (it != null) {
                var combats = it.toList(Combat::class.java)
                combatsList.adapter  = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, combats.map { it.id })
                combatsList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val parentFragment = (activity as MainActivity).currentFragment as AdventureTabsFragment
                    parentFragment.stateTransition(AdventureTabsFragment.State.COMBAT_VIEW, CombatViewFragment.newInstance(combats[position]))
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(session: Session) =
                CombatsFragment().apply {
                    val bundle = Bundle()
                    bundle.putSerializable(SESSION_ARG_KEY, session)
                    arguments = bundle
                }
    }
}