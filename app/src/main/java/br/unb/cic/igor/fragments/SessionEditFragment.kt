package br.unb.cic.igor.fragments


import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Session
import kotlinx.android.synthetic.main.fragment_add_session.*
import kotlinx.android.synthetic.main.fragment_add_session.view.*
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
        return inflater.inflate(R.layout.fragment_session_edit, container, false)
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
