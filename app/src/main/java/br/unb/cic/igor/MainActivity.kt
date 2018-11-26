package br.unb.cic.igor

import android.support.v4.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import br.unb.cic.igor.adapters.MenuAdapter
import br.unb.cic.igor.classes.*
import br.unb.cic.igor.fragments.AdventureTabsFragment
import br.unb.cic.igor.fragments.AdventuresFragment
import br.unb.cic.igor.fragments.CombatFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*


class MainActivity : AppCompatActivity(), AdventuresFragment.OnAdventureSelected, AdventureTabsFragment.OnCombatStarted, CombatFragment.OnCombatFinished {
    private var state: State = State.ADVENTURES

    var currentFragment: Fragment = AdventuresFragment.newInstance()

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDb: FirebaseFirestore

    private var doubleBackToExitPressedOnce: Boolean = false;

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            toast("Pressione VOLTAR novamente para sair")

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }

        if (currentFragment is AdventureTabsFragment) {
            (currentFragment as AdventureTabsFragment).onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure action bar
        setSupportActionBar(main_toolbar)

        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseFirestore.getInstance()

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
        menu_list_view.setOnItemClickListener {parent, _, position, id ->
            val adapter = parent.adapter as MenuAdapter

            drawer_layout.closeDrawer(GravityCompat.START)

            when(adapter.menuOptions[id.toInt()]) {
                State.ADVENTURES.description -> toast("Aventuras")
                State.BOOKS.description -> toast("Livros")
                State.ACCOUNT.description -> toast("Conta")
                State.NOTIFICATIONS.description -> toast("Notificações")
                State.SETTINGS.description -> toast("Configurações")
                State.LOGOUT.description -> Logout()
            }
            state = state.from(adapter.menuOptions[id.toInt()])
            changeColor(adapter, position)

        }

//        var master = Master("8v08uGYQYEbeor5IqUhq4fWAfxi2", "Fernando", "FUFINUSTES", "Description")
//
//        var adventure = Adventure("", "Aventura", "Eh hora da Aventura!", master)
//        var players = PlayerContent.PLAYERS
//
//        var adId = Adventure.Insert(adventure)
//
//        var combat = Combat().apply {
//            this.currentTurn = Turn(0)
//        }z
//
//        Combat.Insert("RN3XRKtJVRHzpe5zF9n2", "QJhEO7yg5ON9ThL7PE5y", combat)

//        val playerAction = PlayerAction("", 0, "EeEuM4KTyYa3crUbxTnQ69OTsNC2", "Vou te atirar")
//
//        PlayerAction.Insert("QJhEO7yg5ON9ThL7PE5y", "RN3XRKtJVRHzpe5zF9n2", "inbVwVE4S3O7F0RvK0Jm", playerAction)

//        Adventure.Get(adId, mDb).addOnSuccessListener{
//            task ->
//            val u = task.toObject(Adventure::class.java)
//            if(u == null){
//                Toast.makeText(this, "Error on registration.", Toast.LENGTH_SHORT).show()
//            } else{
//                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
//
//                var pId0 = Adventure.AddPlayer(adId, players[0], mDb)
//                var pId1 = Adventure.AddPlayer(adId, players[1], mDb)
//            }
//        }

//        var list = Adventure.List().addOnSuccessListener{ task ->
//            val list = task.documents
//            if(list == null){
//                Toast.makeText(this, "No documents", Toast.LENGTH_SHORT).show()
//            } else{
//                for(doc in list){
//                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
//                    var realDoc = doc.toObject(Adventure::class.java)
//                    val a = 1;
//                }
//            }
//        }

//        var adId = "tSthabRpUZcXgdryAiqM"
//        var session = Session(adventureId = adId, name = "session1", date = Date(), summary = "session summary")
//        Session.Insert(session, adId)
//
//        session.summary = "ODEIO ISSO AQUI TUDO"
//
//        Session.Update(session, adId, mDb)


    }

    override fun onStart() {
        super.onStart()

        currentFragment = currentFragment
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.mainContent, currentFragment)
        ft.commit()
    }

    override fun onCombatFinished(adventureId: String) {
        switchContent(AdventureTabsFragment.newInstance(adventureId))
    }

    fun switchContent(fragment: Fragment) {
//        AdventureTabsFragment.newInstance("tSthabRpUZcXgdryAiqM")
        currentFragment = fragment
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.mainContent, currentFragment)
        ft.commit()
    }

//    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
//        val view = super.onCreateView(name, context, attrs)
//
//
//        return view
//    }

    private fun Logout(){
        toast("Logout")
        mAuth.signOut()
        User.SetInstance(null)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun changeColor(adapter: MenuAdapter, position: Int) {

        for (i in 0.until(adapter.count)) {
            val menuView = menu_list_view.getChildAt(i)
            var textView = menuView.findViewById<TextView>(R.id.title_menu)
            if (textView.text != state.description) {
                textView.setTextColor(resources.getColor(R.color.colorRed))
                menuView.findViewById<ImageView>(R.id.image_menu).setImageResource(adapter.images[i])
                menuView.findViewById<View>(R.id.indicator).visibility = View.GONE
            } else {
                menuView.findViewById<TextView>(R.id.title_menu).setTextColor(resources.getColor(R.color.colorAccent))
                menuView.findViewById<ImageView>(R.id.image_menu).setImageResource(adapter.images[position + adapter.images.size / 2])
                menuView.findViewById<View>(R.id.indicator).visibility = View.VISIBLE
            }
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

    override fun onAdventureSelected(adventureId: String) {
        switchContent(AdventureTabsFragment.newInstance("QJhEO7yg5ON9ThL7PE5y"))
    }

    override fun onCombatStarted(adventure: Adventure) {
        switchContent(CombatFragment.newInstance(adventure))
    }

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