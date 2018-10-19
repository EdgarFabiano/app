package br.unb.cic.igor

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TabHost
import android.widget.Toast
import br.unb.cic.igor.fragments.AdventureTabsFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AdventureTabsFragment.OnTabSelectionListener {
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


        // Set navigation view navigation item selected listener
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_aventuras -> toast(resources.getString(R.string.aventuras))
                R.id.action_livros -> toast(resources.getString(R.string.livros))
                R.id.action_conta -> toast(resources.getString(R.string.conta))
                R.id.action_notificacoes -> toast(resources.getString(R.string.notificacoes))
                R.id.action_configuracoes -> toast(resources.getString(R.string.configuracoes))
            }
            // Close the drawer
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    // Extension function to show toast message easily
    private fun Context.toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onFragmentInteraction(selection: String) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handles action bar item clicks here.
        when (item.itemId) {
            R.id.action_editar -> {
                toast(resources.getString(R.string.editar))
                return true
            }
            R.id.action_ordenar -> {
                toast(resources.getString(R.string.ordenar))
                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }
}