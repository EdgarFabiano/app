package br.unb.cic.igor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainToolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)
    }
}
