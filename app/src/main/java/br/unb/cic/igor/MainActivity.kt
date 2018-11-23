package br.unb.cic.igor

import android.content.Context
import android.opengl.Visibility
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
import br.unb.cic.igor.classes.*
import br.unb.cic.igor.fragments.AdventureTabsFragment
import br.unb.cic.igor.fragments.PlayersFragment
import br.unb.cic.igor.fragments.dummy.DummyContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*


class MainActivity : AppCompatActivity() {
//    private var contentFragment : AdventureTabsFragment = AdventureTabsFragment.newInstance()

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDb: FirebaseFirestore

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

//        var master = Master("0W98WyWPqOZCGCzHvMQ487lQxSH3", "Fabio", "Dazor", "Um maluco no pedaço")
//
//        var adventure = Adventure("", "Nova1", "Resumo!", master)
//        var players = PlayerContent.PLAYERS
//
//        var adId = Adventure.Insert(adventure, mDb)
//
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

//        var list = Adventure.List(mDb).addOnSuccessListener{ task ->
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

//        var adId = "X1qYRauOUTDTj5k9ChOs"
//        var session = Session(adventureId = adId,name = "session1", date = Date(), summary = "session summary")
//        session = Session.Insert(session, adId, mDb)
//
//        session.summary = "ODEIO ISSO AQUI TUDO"
//
//        Session.Update(session, adId, mDb)


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
}