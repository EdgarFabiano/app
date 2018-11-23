package br.unb.cic.igor.classes

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

data class Adventure(var id: String = "", var name: String = "", var summary : String = "", var master: Master = Master()) {

    companion object {
        fun Insert(adventure: Adventure, db: FirebaseFirestore): String{
            var ref = db.collection("adventure").document()
            adventure.id = ref.id
            ref.set(adventure)

            return adventure.id
        }

        fun Get(id: String, mDatabase: FirebaseFirestore): Task<DocumentSnapshot> {
            val docRef = mDatabase.collection("adventure").document(id)
            return docRef.get()
        }

        fun Update(summary: String, adventure: Adventure, db: FirebaseFirestore): Adventure{
            db.collection("adventure").document(adventure.id).update(
                            "summary", adventure.summary
                    )
            adventure.summary = summary
            return adventure
        }

        fun AddPlayer(id: String, player: Player, db: FirebaseFirestore): String{
            val ref = db.collection("adventure").document(id)
                    .collection("players").document()
            player.id = ref.id
            ref.set(player)

            return player.id
        }

        fun List(mDatabase: FirebaseFirestore): Task<QuerySnapshot> {
            val docRef = mDatabase.collection("adventure")
            return docRef.get()
        }
    }
}