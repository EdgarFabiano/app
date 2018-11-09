package br.unb.cic.igor.classes

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

data class Adventure(var id: String = "", var summary : String = "", var master: Master = Master(), var players : ArrayList<Player> = ArrayList(), var sessions : ArrayList<Session> = ArrayList()) {

    companion object {
        fun Insert(adventure: Adventure, db: FirebaseFirestore){
            var ref = db.collection("adventure").document()
            adventure.id = ref.id
            ref.set(adventure)
        }

        fun Get(id: String, mDatabase: FirebaseFirestore): Task<DocumentSnapshot> {
            var docRef = mDatabase.collection("adventure").document(id)
            return docRef.get()
        }
    }
}