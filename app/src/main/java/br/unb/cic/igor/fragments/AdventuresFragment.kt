package br.unb.cic.igor.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.unb.cic.igor.R
import br.unb.cic.igor.adapters.AdventuresAdapter
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.view_models.AdventureViewModel
import java.util.*

class AdventuresFragment : Fragment() {

    companion object {
        fun newInstance() = AdventuresFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_adventures, container, false)

        var recyclerView = view.findViewById<RecyclerView>(R.id.adventures_recycler_view)
        var adventures : List<Adventure> = ArrayList()
        var adventureViewModel = AdventureViewModel()
        adventures = adventures.plus(adventureViewModel.mockAdventure1)
        adventures = adventures.plus(adventureViewModel.mockAdventure2)
        adventures = adventures.plus(adventureViewModel.mockAdventure3)
        adventures = adventures.plus(adventureViewModel.mockAdventure4)
        adventures = adventures.plus(adventureViewModel.mockAdventure5)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = AdventuresAdapter(adventures, context)

        return view
    }


}
