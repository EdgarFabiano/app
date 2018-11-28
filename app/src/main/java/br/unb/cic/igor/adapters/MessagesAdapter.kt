package br.unb.cic.igor.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Player


import br.unb.cic.igor.fragments.PlayersFragment.OnPlayersFragmentInteractionListener
import br.unb.cic.igor.fragments.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.message_item.view.*

import kotlinx.android.synthetic.main.player_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnPlayersFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MessagesAdapter(
        private val mValues: ArrayList<String>,
        private val mListener: MessagesFragment.OnMessagesFragmentInteractionListener?)
    : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as String
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onMessagesFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mMessageView.text = item

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mMessageView: TextView = mView.message_text

        override fun toString(): String {
            return super.toString() + " '" + mMessageView.text + "'"
        }
    }
}
