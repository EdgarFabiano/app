package br.unb.cic.igor.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.PlayerAction
import kotlinx.android.synthetic.main.fragment_action_review.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ACT_REVIEW_PARAM = "act_review_param"

/**
 * A simple [Fragment] subclass.
 * Use the [ActionReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ActionReviewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var playerAction: PlayerAction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playerAction = it.getSerializable(ACT_REVIEW_PARAM) as PlayerAction
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_action_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviewDescription.text = playerAction!!.description
        minimunText.text = (100 - playerAction!!.successRate!!).toString()
        resultText.text = playerAction!!.actionResult!!.toString()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ActionReviewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(playerAction: PlayerAction) =
                ActionReviewFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ACT_REVIEW_PARAM, playerAction)
                    }
                }
    }
}
