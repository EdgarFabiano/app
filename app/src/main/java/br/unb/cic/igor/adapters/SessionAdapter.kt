package br.unb.cic.igor.adapters

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Session
import br.unb.cic.igor.fragments.AdventureFragment
import br.unb.cic.igor.fragments.dummy.DummyContent
import kotlinx.android.synthetic.main.session_view.view.*
import java.text.SimpleDateFormat

public class SessionAdapter(private val sessionDataset: Array<Session>, private val mListener: AdventureFragment.OnSessionSelectedListener?) :
RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Session
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onSessionSelected(item)
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class SessionViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): SessionAdapter.SessionViewHolder {
        // create a new view
        val layout = LayoutInflater.from(parent.context)
                .inflate(R.layout.session_view, parent, false) as ConstraintLayout
        // set the view's size, margins, paddings and layout parameters

        return SessionViewHolder(layout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val item = sessionDataset[position]
        holder.layout.dateText.text = SimpleDateFormat("dd/MM").format(item.date)
        holder.layout.nameText.text = item.name

        with(holder.layout) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = sessionDataset.size
}