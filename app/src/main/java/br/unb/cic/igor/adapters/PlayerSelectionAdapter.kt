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




public interface MyPlayerSelectedListener{
    fun playerSelected(checked: Boolean, userId: String): Boolean
}

class PlayerSelectionAdapter(private val players: ArrayList<Player>): RecyclerView.Adapter<PlayerSelectionAdapter.PlayerSelectionHolder>() {
    var checkedPlayers: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerSelectionHolder {
        val inflatedView = parent.inflate(R.layout.player_selection_row, false)
        return PlayerSelectionHolder(inflatedView)
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: PlayerSelectionHolder, position: Int) {
        val itemPlayer = players[position]
        checkedPlayers.add(itemPlayer.userId)
        holder.bindPlayer(itemPlayer)
    }

    class PlayerSelectionHolder(v: View) : RecyclerView.ViewHolder(v) {
        //2
        private var view: View = v
        var player: Player? = null
        var checked: Boolean = true

        //3
        init {
            view.switch_selection.isChecked = true

            view.setOnClickListener {
                view.switch_selection.isChecked = !checked
                checked = !checked
            }
        }

        fun bindPlayer(player: Player){
            this.player = player
            val str = "${player!!.character} (${player!!.name})"
            view.player_name.text = str
        }

        companion object {
            //5
            private val PLAYER_SEL_KEY = "PLAYER_SEL"
        }
    }

}