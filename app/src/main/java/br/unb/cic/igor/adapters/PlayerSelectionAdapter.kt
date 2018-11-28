package br.unb.cic.igor.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Player
import br.unb.cic.igor.extensions.inflate
import kotlinx.android.synthetic.main.player_selection_row.view.*
import android.view.MotionEvent


class PlayerSelectionAdapter(private val players: ArrayList<Player>, private val callback: (String) -> Any): RecyclerView.Adapter<PlayerSelectionAdapter.PlayerSelectionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerSelectionHolder {
        val inflatedView = parent.inflate(R.layout.player_selection_row, false)
        return PlayerSelectionHolder(inflatedView)
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: PlayerSelectionHolder, position: Int) {
        val itemPlayer = players[position]
        holder.bindPlayer(itemPlayer , callback)
    }

    class PlayerSelectionHolder(v: View) : RecyclerView.ViewHolder(v) {
        //2
        private var view: View = v
        var player: Player? = null
        var checked: Boolean = true

        //3
        init {
            view.switch_selection.isChecked = true
        }

        fun bindPlayer(player: Player, callback: (String) -> Any){
            this.player = player
            val str = "${player.character} (${player.name})"
            view.player_name.text = str
            view.setOnClickListener {
                view.switch_selection.isChecked = !checked
                checked = !checked
                callback(this.player!!.id)
            }
        }

        companion object {
            //5
            private val PLAYER_SEL_KEY = "PLAYER_SEL"
        }
    }

}