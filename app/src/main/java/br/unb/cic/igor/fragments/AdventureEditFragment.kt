package br.unb.cic.igor.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.view_models.AdventureViewModel
import kotlinx.android.synthetic.main.fragment_adventure_edit.*


/**
 * A simple [Fragment] subclass.
 * Use the [AdventureEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AdventureEditFragment : Fragment() {
    private var adventure: Adventure? = null

    private lateinit var viewModel: AdventureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adventure_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AdventureViewModel::class.java)
        adventure = viewModel.mockAdventure

        advNameEdit.setText(adventure!!.name)
        advInfoEdit.setText(adventure!!.summary)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AdventureEditFragment.
         */
        @JvmStatic
        fun newInstance() = AdventureEditFragment()
    }
}
