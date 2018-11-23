package br.unb.cic.igor.view_models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel;
import br.unb.cic.igor.classes.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class AdventureViewModel : ViewModel() {
    var adventures: MutableLiveData<List<Adventure>> = MutableLiveData()

    fun getAdventures(): LiveData<List<Adventure>> {
        if (adventures == null) {
            adventures = MutableLiveData<List<Adventure>>()
            loadAdventures()
        }
        return adventures
    }

    private fun loadAdventures() {
        User.Adventures()
    }
}
