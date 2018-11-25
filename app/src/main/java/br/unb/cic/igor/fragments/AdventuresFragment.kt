package br.unb.cic.igor.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import br.unb.cic.igor.MainActivity
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
        recyclerView.layoutManager = LinearLayoutManager(activity)

        runAnimation(recyclerView, adventures)


        return view
    }

    private fun runAnimation(recyclerView: RecyclerView, adventures: List<Adventure>) {
        var animationController : LayoutAnimationController = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_fall)

        recyclerView.adapter = AdventuresAdapter(adventures, context, activity as MainActivity)

        recyclerView.layoutAnimation = animationController
        (recyclerView.adapter as AdventuresAdapter).notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()

    }

    interface OnAdventureSelected {
        fun onAdventureSelected(adventureId: String)
    }

}
