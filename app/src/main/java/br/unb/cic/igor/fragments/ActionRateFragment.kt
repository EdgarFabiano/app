package br.unb.cic.igor.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.unb.cic.igor.MainActivity

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Combat
import br.unb.cic.igor.classes.PlayerAction
import br.unb.cic.igor.classes.User
import br.unb.cic.igor.extensions.toList
import kotlinx.android.synthetic.main.fragment_action_rate.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val COMBAT_PARAM = "combat_param"
private const val ADV_PARAM = "adv_param"
private const val ACTIONS_PARAM = "actions_param"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ActionRateFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ActionRateFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ActionRateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var adventure: Adventure? = null
    private var combat: Combat? = null
    private var listener: OnActionRatesDoneListener? = null
    private var playerActions: ArrayList<PlayerAction> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adventure = it.getSerializable(ADV_PARAM) as Adventure
            combat = it.getSerializable(COMBAT_PARAM) as Combat
            playerActions = it.getSerializable(ACTIONS_PARAM) as ArrayList<PlayerAction>
        }
    }

    fun updateUI() {

        val pagerAdapter = ScreenSlidePagerAdapter(fragmentManager!!, ArrayList(playerActions))
        actionsPager.adapter = pagerAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_action_rate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratesReady.setOnClickListener {

            listener!!.onActionRatesDone((actionsPager.adapter as ScreenSlidePagerAdapter).actions)
        }

        updateUI()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val currentFragment = (context as MainActivity).currentFragment
        if (currentFragment is OnActionRatesDoneListener) {
            listener = currentFragment
        } else {
            throw RuntimeException(context.toString() + " must implement OnActionRatesDoneListener")
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
    interface OnActionRatesDoneListener {
        fun onActionRatesDone(ratedActions: ArrayList<PlayerAction>)
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fm: FragmentManager, val actions: ArrayList<PlayerAction>) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = actions.size

        override fun getItem(position: Int): Fragment = ActionRatePageFragment.newInstance(actions[position])
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ActionRateFragment.
         */
        @JvmStatic
        fun newInstance(adventure: Adventure, combat: Combat, playerActions: ArrayList<PlayerAction>) =
                ActionRateFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ADV_PARAM, adventure)
                        putSerializable(COMBAT_PARAM, combat)
                        putSerializable(ACTIONS_PARAM, playerActions)
                    }
                }
    }
}
