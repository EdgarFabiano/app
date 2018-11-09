package br.unb.cic.igor

import android.content.Context
import android.content.Intent
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
import br.unb.cic.igor.classes.User
import br.unb.cic.igor.fragments.AdventureTabsFragment
import br.unb.cic.igor.fragments.PlayersFragment
import br.unb.cic.igor.fragments.dummy.DummyContent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), PlayersFragment.OnPlayersFragmentInteractionListener {
//    private var contentFragment : AdventureTabsFragment = AdventureTabsFragment.newInstance()

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
            changeColor(adapter, parent, view, position)

            drawer_layout.closeDrawer(GravityCompat.START)

            when(adapter.menuOptions[id.toInt()]){
                "Aventuras" -> toast("Aventuras")
                "Livros" -> toast("Livros")
                "Conta" -> toast("Conta")
                "Notificações" -> toast("Notificações")
                "Configurações" -> toast("Configurações")
                "Logout" -> Logout()
            }
        }

    }

    private fun Logout(){
        toast("Logout")
        mAuth.signOut()
        User.SetInstance(null)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun changeColor(adapter: MenuAdapter, parent: AdapterView<*>, view: View, position: Int) {

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

    override fun onPlayersFragmentInteraction(item: DummyContent.DummyItem?){

    }
}