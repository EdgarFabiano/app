package br.unb.cic.igor.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.PlayerAction
import kotlinx.android.synthetic.main.fragment_action_rate_page.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ActionRatePageFragment : Fragment() {
    var action: PlayerAction? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_action_rate_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionDescriptionInfo.text = action!!.description
        action!!.successRate = action!!.successRate ?: 0
        successRatingText.text = "${action!!.successRate!!} %"
        successRatingBar.progress = action!!.successRate!!
        successRatingBar.incrementProgressBy(1)
        successRatingBar.max = 100
        successRatingBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
                successRatingText.text = "${progress} %"
                action!!.successRate = progress
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    companion object {
        fun newInstance(action: PlayerAction) : ActionRatePageFragment {
            return ActionRatePageFragment().apply {
                this.action = action
            }
        }
    }
}
