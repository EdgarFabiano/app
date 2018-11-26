package br.unb.cic.igor.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Combat
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_start_turn.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ADV = "ARG_ADV"
private const val ARG_CBT = "ARG_CBT"
private const val ARG_MASTER = "ARG_MASTER"

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
    private var isMaster: Boolean = false
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adventure = it.getSerializable(ARG_ADV) as Adventure
            combat = it.getSerializable(ARG_CBT) as Combat
            isMaster = it.getBoolean(ARG_MASTER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_turn, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
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
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(adventure: Adventure, combat: Combat, isMaster: Boolean) =
                StartTurnFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_ADV, adventure)
                        putSerializable(ARG_CBT, combat)
                        putBoolean(ARG_MASTER, isMaster)
                    }
                }
    }
}
