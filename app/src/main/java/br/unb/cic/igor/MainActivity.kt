package br.unb.cic.igor

import android.content.Context
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import br.unb.cic.igor.adapters.MenuAdapter
import br.unb.cic.igor.fragments.AdventureTabsFragment
import br.unb.cic.igor.fragments.PlayersFragment
import br.unb.cic.igor.fragments.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AdventureTabsFragment.OnTabSelectionListener, PlayersFragment.OnPlayersFragmentInteractionListener {
//    private var contentFragment : AdventureTabsFragment = AdventureTabsFragment.newInstance()

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure action bar
        setSupportActionBar(main_toolbar)

        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                this,
                drawer_layout,
                main_toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                //toast("Drawer opened")
            }
        }

        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        menu_list_view.adapter = MenuAdapter(this)

        // Set navigation view navigation item selected listener
        menu_list_view.setOnItemClickListener {parent, view, position, id ->
            changeColor(parent, view, position)

            when(id){
                
            }
        }

    }

    private fun changeColor(parent: AdapterView<*>, view: View, position: Int) {
        val adapter = parent.adapter as MenuAdapter

        for (i in 0.until(adapter.count)) {
            val menuView = menu_list_view.getChildAt(i)
            menuView.findViewById<TextView>(R.id.title_menu).setTextColor(resources.getColor(R.color.colorRed))
            menuView.findViewById<ImageView>(R.id.image_menu).setImageResource(adapter.images[i])

        }

        view.findViewById<TextView>(R.id.title_menu).setTextColor(resources.getColor(R.color.colorAccent))
        view.findViewById<ImageView>(R.id.image_menu).setImageResource(adapter.images[position + adapter.images.size / 2])
    }

    // Extension function to show toast message easily
    private fun Context.toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu, menu)
//        return true
//    }

    override fun onFragmentInteraction(selection: String) {

    }

    override fun onPlayersFragmentInteraction(item: DummyContent.DummyItem?){

    }
}