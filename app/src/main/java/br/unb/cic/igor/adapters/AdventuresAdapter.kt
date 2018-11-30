package br.unb.cic.igor.adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.unb.cic.igor.R
import br.unb.cic.igor.adapters.view.holder.AdventuresRecyclerViewHolder

import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Session
import br.unb.cic.igor.extensions.toList
import br.unb.cic.igor.fragments.AdventuresFragment
import br.unb.cic.igor.util.FormatterUtil
import java.util.*
import android.content.ClipData.Item



class AdventuresAdapter(var adventures: List<Adventure>, private val context: Context?, private val mListener: AdventuresFragment.OnAdventureSelected?) : RecyclerView.Adapter<AdventuresRecyclerViewHolder>() {
    private val mOnClickListener: View.OnClickListener

    companion object {
        val images = intArrayOf(
                R.drawable.miniatura_coast,
                R.drawable.miniatura_corvali,
                R.drawable.miniatura_heartlands,
                R.drawable.miniatura_imagem,
                R.drawable.miniatura_krevast)
    }

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Adventure
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onAdventureSelected(item.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdventuresRecyclerViewHolder {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.adventure_card_view, parent, false) as CardView

        return AdventuresRecyclerViewHolder(layout, parent)
    }

    override fun getItemCount(): Int {
        return adventures.size
    }

    override fun onBindViewHolder(holder: AdventuresRecyclerViewHolder, position: Int) {
        var adventure = adventures[position]

        var lastSession = Session()
        Session.ListByAdventure(adventure.id).addOnSuccessListener {
            if (!it.isEmpty) {
                lastSession = it.toList(Session::class.java).last()
            }
        }

        holder.mTitle.text = adventure.name
        holder.mNextSession.text = "Próxima sessão " + FormatterUtil.formatDate(lastSession.date)
        holder.mSeekbar.progress = 1 + Random().nextInt(100)
        holder.mSeekbar.isEnabled = false
        holder.foreground.background = context!!.resources.getDrawable(images[adventure.bg])

        with(holder.view) {
            tag = adventure
            setOnClickListener(mOnClickListener)
        }
    }

    fun removeItem(position: Int) {
        adventures.minus(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: Adventure, position: Int) {
        adventures.plus(item)
        notifyItemInserted(position)
    }

}
