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
import android.content.Context
import android.widget.Toast
import br.unb.cic.igor.MainActivity
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
    private val ADV_ID_ARG_KEY = "adv_id_arg_key"
    private var listener: AddSessionFragment.AddSessionListener? = null
    var calendar = Calendar.getInstance()
    val dateListener : OnDateSetListener
    var adventureId : String? = null
    var saving : Boolean = false

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adventureId = arguments?.getString(ADV_ID_ARG_KEY) as String?
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

        view.readySessionEditBtn.setOnClickListener {
            if (!saving) {
                saving = true
                val name = sessionNameCreate.text.toString()
                if (name == "") {
                    toast("Por favor digite um nome para a sessão")
                } else {
                    var session = Session(name = sessionNameCreate.text.toString(), date = calendar.time)
                    Session.Insert(session, adventureId!!)
                    listener!!.sessionCreated()
                }
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = (activity as MainActivity).currentFragment
        if (fragment is AddSessionListener) {
            listener = fragment
        } else {
            throw RuntimeException(fragment.toString() + " must implement OnSessionSelectedListener")
        }
    }

    interface AddSessionListener {
        fun sessionCreated()
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
        fun newInstance(adventureId: String) =
                AddSessionFragment().apply {
                    val bundle = Bundle()
                    bundle.putString(ADV_ID_ARG_KEY, adventureId)
                    arguments = bundle
                }
    }
}
