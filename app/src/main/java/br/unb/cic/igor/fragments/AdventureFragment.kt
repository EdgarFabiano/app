package br.unb.cic.igor.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.unb.cic.igor.AdventureViewModel
import br.unb.cic.igor.R

class AdventureFragment : Fragment() {
    private var listener: OnSessionSelectedListener? = null

    companion object {
        fun newInstance() = AdventureFragment()
    }

    private lateinit var viewModel: AdventureViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.adventure_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AdventureViewModel::class.java)
        // TODO: Use the ViewModel

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSessionSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnTabSelectionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSessionSelectedListener {
        // TODO: Update argument type and name
        fun onSessionSelected(selection: String)
    }

}
