package br.unb.cic.igor.fragments


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
import kotlinx.android.synthetic.main.fragment_adventure_edit.*
import kotlinx.android.synthetic.main.fragment_adventure_edit.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [AdventureEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AdventureEditFragment : Fragment() {
    private val ADV_ARG_KEY = "adv_arg_key"
    private var adventure: Adventure? = null
    private var listener: EditAdventureListener? = null
    var saving : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adventure = arguments?.getSerializable(ADV_ARG_KEY) as Adventure?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_adventure_edit, container, false)

        view.advReadyEditBtn.setOnClickListener {
            if (!saving) {
                saving = true
                val name = advNameEdit.text.toString()
                val summary = advInfoEdit.text.toString()
                if (name == "" || summary == "") {
                    toast("Por favor preencha todos os campos")
                } else {
                    adventure!!.summary = summary
                    adventure!!.name = name
                    Adventure.Update(adventure!!)
                    listener!!.adventureChanged()
                }
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        advNameEdit.setText(adventure!!.name)
        advInfoEdit.setText(adventure!!.summary)
    }

    private fun toast(message: String) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = (activity as MainActivity).currentFragment
        if (fragment is EditAdventureListener) {
            listener = fragment
        } else {
            throw RuntimeException(fragment.toString() + " must implement OnSessionSelectedListener")
        }
    }

    interface EditAdventureListener {
        fun adventureChanged()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AdventureEditFragment.
         */
        @JvmStatic
        fun newInstance(adventure: Adventure) = AdventureEditFragment().apply {
            val bundle = Bundle()
            bundle.putSerializable(ADV_ARG_KEY, adventure)
            arguments = bundle
        }
    }
}
