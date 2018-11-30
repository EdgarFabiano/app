package br.unb.cic.igor.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import br.unb.cic.igor.MainActivity
import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Session
import kotlinx.android.synthetic.main.fragment_session.*
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 * to handle interaction events.
 * Use the [SessionFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SessionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val SESSION_ARG_KEY = "session_arg_key"
    private var session: Session? = null

    private var listener: onCombatsClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = arguments?.getSerializable(SESSION_ARG_KEY) as Session?
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = (activity as MainActivity).currentFragment
        if (fragment is SessionFragment.onCombatsClickListener) {
            listener = fragment
        } else {
            throw RuntimeException(fragment.toString() + " must implement onCombatsClickListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_session, container, false)

        var combates = view.findViewById<Button>(R.id.combatesButton)
        combates.setOnClickListener {
            listener!!.onCombatsClick(session!!)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val summary = session!!.summary
        sessionInfoText.text = if (summary != "") summary else "Sua sessão ainda não posssui um resumo!"
        sessionNameEdit.text = session!!.name
        sessionDateText.text = SimpleDateFormat("dd/MM").format(session!!.date)
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SessionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(session: Session) =
                SessionFragment().apply {
                    val bundle = Bundle()
                    bundle.putSerializable(SESSION_ARG_KEY, session)
                    arguments = bundle
                }
    }

    interface onCombatsClickListener {
        fun onCombatsClick(session: Session)
    }
}
