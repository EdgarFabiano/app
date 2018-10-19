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
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Session
import kotlinx.android.synthetic.main.adventure_fragment.*
import kotlinx.android.synthetic.main.adventure_fragment.view.*

class AdventureFragment : Fragment() {
    private var listener: OnSessionSelectedListener? = null
    private var adventure: Adventure? = null;

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
        adventure = viewModel.mockAdventure
        adventureInfo.text = adventure!!.summary
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = fragmentManager?.findFragmentById(R.id.tabsFragment)
        if (fragment is OnSessionSelectedListener) {
            listener = fragment
        } else {
            throw RuntimeException(fragment.toString() + " must implement OnSessionSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSessionSelectedListener {
        fun onSessionSelected(session: Session)
    }

}
