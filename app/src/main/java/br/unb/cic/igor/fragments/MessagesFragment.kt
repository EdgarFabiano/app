package br.unb.cic.igor.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.unb.cic.igor.R
import br.unb.cic.igor.classes.Player

import kotlinx.android.synthetic.main.fragment_messages_list.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [MessagesFragment.OnMessagesFragmentInteractionListener] interface.
 */
class MessagesFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnMessagesFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_messages_list, container, false)

        // Set the adapter
        if (view.messages_list is RecyclerView) {
            with(view.messages_list) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MessagesAdapter(listOf("Você encontrou um Dragão Branco dos Olhos Azuis furioso e faminto",
                        "Você precisa rolar os dados pra saber se vai morrer pelo Dragão Branco dos Olhos Azuis, ou se vai sair com vida", "Valeu fera, tu fugiu com sucesso"), listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val fragment = fragmentManager?.findFragmentById(R.id.tabsFragment)
        if (fragment is OnMessagesFragmentInteractionListener) {
            listener = fragment
        }
//        else {
//            throw RuntimeException(fragment.toString() + " must implement OnMessagesFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnMessagesFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onMessagesFragmentInteraction(item: String?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                MessagesFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
