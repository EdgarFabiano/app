package br.unb.cic.igor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Master
import br.unb.cic.igor.classes.User


class CreateAdventureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_adventure)

        var form_create = findViewById<View>(R.id.form_create)

        form_create.findViewById<TextView>(R.id.close).setOnClickListener { onBackPressed() }

        val bg = findViewById<RadioGroup>(R.id.bgs)

        form_create.findViewById<Button>(R.id.create_adventure).setOnClickListener {
            val currentUser = User.GetInstance()!!
            val master = Master(currentUser.id, currentUser.username!!, "Master")
            Adventure.Insert(Adventure(
                    master = master,
                    name = form_create.findViewById<EditText>(R.id.nome_aventura).text.toString(),
                    bg = bg.checkedRadioButtonId.minus(1) % 5))
            onBackPressed()
        }

    }
}
