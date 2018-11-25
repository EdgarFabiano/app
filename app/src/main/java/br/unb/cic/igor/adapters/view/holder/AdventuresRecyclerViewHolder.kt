package br.unb.cic.igor.adapters.view.holder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import br.unb.cic.igor.R

class AdventuresRecyclerViewHolder: RecyclerView.ViewHolder {

    var mlayout : LinearLayout = itemView.findViewById(R.id.main_layout)
    var mCardView : CardView = itemView.findViewById(R.id.adventure_card_view)
    var mTitle : TextView = itemView.findViewById(R.id.title)
    var mNextSession : TextView = itemView.findViewById(R.id.next_session)
    var mSeekbar : SeekBar = itemView.findViewById(R.id.seekBar)
    lateinit var view: CardView

    public constructor(view: View) : super(view)

    public constructor(view: CardView, container: ViewGroup?) : super(view) {
        this.view = view
    }

}
