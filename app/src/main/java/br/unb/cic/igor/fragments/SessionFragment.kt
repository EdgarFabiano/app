package br.unb.cic.igor.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Combat
import br.unb.cic.igor.classes.Session
import br.unb.cic.igor.extensions.toList
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = arguments?.getSerializable(SESSION_ARG_KEY) as Session?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val summary = session!!.summary
        sessionInfoText.text = if (summary != "") summary else "Sua sessão ainda não posssui um resumo!"
        sessionNameEdit.text = session!!.name
        sessionDateText.text = SimpleDateFormat("dd/MM").format(session!!.date)

        Combat.List(sessionId = session!!.id, adventureId = session!!.adventureId).addOnSuccessListener {
            if (it != null) {
                var combats = it.toList(Combat::class.java)
                combatsList.adapter  = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, combats)
                combatsList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
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
}
