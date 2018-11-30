package br.unb.cic.igor.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import br.unb.cic.igor.CreateAdventureActivity
import br.unb.cic.igor.R
import br.unb.cic.igor.adapters.AdventuresAdapter
import br.unb.cic.igor.adapters.view.holder.AdventuresRecyclerViewHolder
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.User
import br.unb.cic.igor.extensions.toList
import br.unb.cic.igor.util.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_adventures.*


class AdventuresFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, SwipeToDeleteCallback.RecyclerItemTouchHelperListener {

    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    lateinit var rv : RecyclerView
    var adventures: ArrayList<Adventure> = ArrayList()
    lateinit var adapter: AdventuresAdapter

    override fun onRefresh() {
        User.Get(User.GetInstance()!!.id).addOnSuccessListener {
            val obj = it.toObject(User::class.java)
            User.SetInstance(obj)
        }

        Adventure.List().addOnSuccessListener {
            adventures.clear()
            if (it != null) {
                val allAdv = it.toList(Adventure::class.java)
                val userId = User.GetInstance()!!.id
                val userAdvs = User.GetInstance()!!.adventureRefs
                for(adv in allAdv){
                    if(adv.master.userId == userId || userAdvs.any { a -> a == adv.id }){
                        adventures.add(adv)
                    }
                }
                runAnimation(rv, adventures)
            }
        }
        mSwipeRefreshLayout.setRefreshing(false)
    }

    fun refresh(){
        onRefresh()
    }

    companion object {
        fun newInstance() =  AdventuresFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_adventures, container, false)

        var recyclerView: RecyclerView?
        recyclerView = view.findViewById(R.id.adventures_recycler_view)
        rv = recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        Adventure.List().addOnSuccessListener {
            if (it != null) {
                adventures.clear()
                var allAdv = it.toList(Adventure::class.java)
                var userId = User.GetInstance()!!.id
                var userAdvs = User.GetInstance()!!.adventureRefs
                for(adv in allAdv){
                    if(adv.master.userId == userId || userAdvs.any { a -> a == adv.id }){
                        adventures.add(adv)
                    }
                }
                runAnimation(recyclerView, adventures)
            }
        }

        var swipeToDeleteCallback = SwipeToDeleteCallback(this)

        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(recyclerView)

        // SwipeRefreshLayout
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)

        val fab: View = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(context, CreateAdventureActivity::class.java))
        }

        return view
    }

    private fun runAnimation(recyclerView: RecyclerView, adventures: List<Adventure>) {
        var animationController : LayoutAnimationController = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_fall)

        adapter = AdventuresAdapter(adventures, context, activity as OnAdventureSelected)
        recyclerView.adapter = adapter

        recyclerView.layoutAnimation = animationController
        (recyclerView.adapter as AdventuresAdapter).notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()

    }

    interface OnAdventureSelected {
        fun onAdventureSelected(adventureId: String)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is AdventuresRecyclerViewHolder) {
            // get the removed item name to display it in snack bar
            val name = adventures.get(viewHolder.getAdapterPosition()).name

            // backup of removed item for undo purpose
            val deletedItem = adventures.get(viewHolder.getAdapterPosition())
            val deletedIndex = viewHolder.getAdapterPosition()

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition())

            // showing snack bar with Undo option
            val snackbar = Snackbar.make(main_layout, name + " removida", Snackbar.LENGTH_LONG)
            snackbar.setAction("desfazer")  {
                adapter.restoreItem(deletedItem, deletedIndex)
            }
            snackbar.setActionTextColor(Color.YELLOW)
            snackbar.show()
        }
    }

}
