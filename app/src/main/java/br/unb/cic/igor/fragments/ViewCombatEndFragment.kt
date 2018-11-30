package br.unb.cic.igor.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.unb.cic.igor.MainActivity

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Combat
import kotlinx.android.synthetic.main.fragment_view_combat_end.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val COMBAT_PARAM = "combat_param"
private const val ADV_PARAM = "adv_param"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ViewCombatEndFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ViewCombatEndFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ViewCombatEndFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var adventure: Adventure? = null
    private var combat: Combat? = null
    private var listener: OnCombatEndViewedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adventure = it.getSerializable(ADV_PARAM) as Adventure
            combat = it.getSerializable(COMBAT_PARAM) as Combat
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_combat_end, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finishTurnText.text = combat!!.currentTurn.description

        okBtn.setOnClickListener {
            okBtn.isClickable = false
            listener!!.onCombatEndViewed()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val currentFragment = (context as MainActivity).currentFragment
        if (currentFragment is OnCombatEndViewedListener) {
            listener = currentFragment
        } else {
            throw RuntimeException(context.toString() + " must implement OnCombatEndViewedListener")
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
    interface OnCombatEndViewedListener {
        fun onCombatEndViewed()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ViewCombatEndFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(adventure: Adventure, combat: Combat) =
                ViewCombatEndFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ADV_PARAM, adventure)
                        putSerializable(COMBAT_PARAM, combat)
                    }
                }
    }
}
