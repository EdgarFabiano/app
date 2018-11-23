package br.unb.cic.igor.fragments


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.unb.cic.igor.MainActivity

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Session
import kotlinx.android.synthetic.main.fragment_add_session.*
import kotlinx.android.synthetic.main.fragment_add_session.view.*
import kotlinx.android.synthetic.main.fragment_adventure_edit.*
import kotlinx.android.synthetic.main.fragment_adventure_edit.view.*
import kotlinx.android.synthetic.main.fragment_session_edit.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SessionEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SessionEditFragment : Fragment() {
    private val SESSION_ARG_KEY = "session_arg_key"
    private var session: Session? = null
    var calendar = Calendar.getInstance()
    val dateListener : DatePickerDialog.OnDateSetListener
    private var listener: SessionEditListener? = null
    var saving : Boolean = false

    init {
        dateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
    }

    private fun updateLabel() {
        val myFormat = "MM/dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        dateEditText.text = sdf.format(calendar.time)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = arguments?.getSerializable(SESSION_ARG_KEY) as Session?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_session_edit, container, false)

        view.readySessionEditBtn.setOnClickListener {
            if (!saving) {
                saving = true
                val name = sessionNameEdit.text.toString()
                val summary = sesssionSummary.text.toString()
                if (name == "" || summary == "") {
                    toast("Por favor preencha todos os campos")
                } else {
                    session!!.summary = summary
                    session!!.name = name
                    session!!.date = calendar.time
                    Session.Update(session!!, session!!.adventureId)
                    listener!!.sessionChanged(session!!)
                }
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dateEdit.setOnClickListener {
            DatePickerDialog(context, dateListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        sessionNameEdit.setText(session!!.name)
        sesssionSummary.setText(session!!.summary)
        calendar.time = session!!.date

        updateLabel()
    }

    private fun toast(message: String) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = (activity as MainActivity).currentFragment
        if (fragment is SessionEditListener) {
            listener = fragment
        } else {
            throw RuntimeException(fragment.toString() + " must implement OnSessionSelectedListener")
        }
    }

    interface SessionEditListener {
        fun sessionChanged(session: Session)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SessionEditFragment.
         */
        @JvmStatic
        fun newInstance(session: Session) =
                SessionEditFragment().apply {
                    val bundle = Bundle()
                    bundle.putSerializable(SESSION_ARG_KEY, session)
                    arguments = bundle
                }
    }
}
