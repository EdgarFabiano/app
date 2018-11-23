package br.unb.cic.igor.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import br.unb.cic.igor.R
import br.unb.cic.igor.adapters.view.holder.AdventuresRecyclerViewHolder

import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.util.FormatterUtil
import java.util.*

class AdventuresAdapter(private val adventures: List<Adventure>, private val context: Context?) : RecyclerView.Adapter<AdventuresRecyclerViewHolder>() {

    val images = intArrayOf(
            R.drawable.miniatura_coast,
            R.drawable.miniatura_corvali,
            R.drawable.miniatura_heartlands,
            R.drawable.miniatura_imagem,
            R.drawable.miniatura_krevast)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdventuresRecyclerViewHolder {
        val inflater = LayoutInflater.from(context)

        return AdventuresRecyclerViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return adventures.size
    }

    override fun onBindViewHolder(holder: AdventuresRecyclerViewHolder, position: Int) {
        var adventure = adventures[position]
        holder.mTitle.text = adventure.name
        holder.mNextSession.text = "Próxima sessão " + FormatterUtil.formatDate(adventure.sessions[adventure.sessions.size -1].date)
        holder.mSeekbar.progress = 1 + Random().nextInt(100)
        holder.mlayout.background = context!!.resources.getDrawable(images[adventure.bg])
    }

}
