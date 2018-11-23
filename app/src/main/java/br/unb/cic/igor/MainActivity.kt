package br.unb.cic.igor

import android.support.v4.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import br.unb.cic.igor.adapters.MenuAdapter
import br.unb.cic.igor.classes.User
import br.unb.cic.igor.fragments.AdventureTabsFragment
import br.unb.cic.igor.fragments.AdventuresFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var state: State = State.ADVENTURES

    private var currentFragment: Fragment = AdventuresFragment.newInstance()

    private lateinit var mAuth: FirebaseAuth

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        (tabsFragment as AdventureTabsFragment).onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure action bar
        setSupportActionBar(main_toolbar)

        mAuth = FirebaseAuth.getInstance()

        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                this,
                drawer_layout,
                main_toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) { }

        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        menu_list_view.adapter = MenuAdapter(this)

        // Set navigation view navigation item selected listener
        menu_list_view.setOnItemClickListener {parent, view, position, id ->
            val adapter = parent.adapter as MenuAdapter
            changeColor(adapter, view, position)

            drawer_layout.closeDrawer(GravityCompat.START)

            when(adapter.menuOptions[id.toInt()]){
                State.ADVENTURES.description -> toast("Aventuras")
                State.BOOKS.description -> toast("Livros")
                State.ACCOUNT.description -> toast("Conta")
                State.NOTIFICATIONS.description -> toast("Notificações")
                State.SETTINGS.description -> toast("Configurações")
                State.LOGOUT.description -> Logout()
            }
            state = state.from(adapter.menuOptions[id.toInt()])
        }

    }

    private fun Logout(){
        toast("Logout")
        mAuth.signOut()
        User.SetInstance(null)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun changeColor(adapter: MenuAdapter, view: View, position: Int) {

        for (i in 0.until(adapter.count)) {
            val menuView = menu_list_view.getChildAt(i)
            menuView.findViewById<TextView>(R.id.title_menu).setTextColor(resources.getColor(R.color.colorRed))
            menuView.findViewById<ImageView>(R.id.image_menu).setImageResource(adapter.images[i])
            menuView.findViewById<View>(R.id.indicator).visibility = View.GONE

        }

        view.findViewById<TextView>(R.id.title_menu).setTextColor(resources.getColor(R.color.colorAccent))
        view.findViewById<ImageView>(R.id.image_menu).setImageResource(adapter.images[position + adapter.images.size / 2])
        view.findViewById<View>(R.id.indicator).visibility = View.VISIBLE
    }

    // Extension function to show toast message easily
    private fun Context.toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu, menu)
//        return true
//    }

    enum class State(val description: String) {
        ADVENTURES("Aventuras"),
        BOOKS("Livros"),
        ACCOUNT("Conta"),
        NOTIFICATIONS("Notificações"),
        SETTINGS("Configurações"),
        LOGOUT("Logout");

        fun from(description: String): State {
            for (value in values()) {
                if (value.description == description) {
                    return value
                }
            }

            return ADVENTURES
        }


    }
}