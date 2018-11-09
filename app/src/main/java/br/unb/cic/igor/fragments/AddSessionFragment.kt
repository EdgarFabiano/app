package br.unb.cic.igor.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.unb.cic.igor.R
import kotlinx.android.synthetic.main.fragment_add_session.view.*
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.widget.Toast
import br.unb.cic.igor.classes.Session
import java.util.*
import kotlinx.android.synthetic.main.fragment_add_session.*
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 * Use the [AddSessionFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AddSessionFragment : Fragment() {
    var calendar = Calendar.getInstance()
    val dateListener : OnDateSetListener

    init {
        dateListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
    }

    private fun updateLabel() {
        val myFormat = "MM/dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        dateText.text = sdf.format(calendar.time)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_session, container, false)

        view.dateEditBtn.setOnClickListener {
            DatePickerDialog(context, dateListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        view.readyEditBtn.setOnClickListener {
            val session = Session(sessionNameCreate.text.toString(), calendar.time, "")
            toast(session.name)
        }

        return view
    }

    public interface AddSessionListener {
        fun addSession(session: Session)
    }

    private fun toast(message: String) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AddSessionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                AddSessionFragment()
    }
}
