package br.unb.cic.igor.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import br.unb.cic.igor.view_models.AdventureViewModel
import br.unb.cic.igor.R
import br.unb.cic.igor.adapters.SessionAdapter
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Session
import kotlinx.android.synthetic.main.adventure_fragment.*

class AdventureFragment : Fragment() {
    private var listener: OnSessionSelectedListener? = null
    private var adventure: Adventure? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object {
        fun newInstance() = AdventureFragment()
    }

    private lateinit var viewModel: AdventureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.adventure_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AdventureViewModel::class.java)
        adventure = viewModel.mockAdventure
        adventureInfo.text = adventure!!.summary

        viewManager = LinearLayoutManager(activity)
        viewAdapter = SessionAdapter(adventure?.sessions!!.toTypedArray(), listener)

        recyclerView = sessionList.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
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
