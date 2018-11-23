package br.unb.cic.igor.classes

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

data class Adventure(var id: String = "", var name: String = "", var summary : String = "", var master: Master = Master()) {

    companion object {
        fun Insert(adventure: Adventure): String{
            var ref = FirebaseFirestore.getInstance().collection("adventure").document()
            adventure.id = ref.id
            ref.set(adventure)

            return adventure.id
        }

        fun Get(id: String): Task<DocumentSnapshot> {
            val docRef = FirebaseFirestore.getInstance().collection("adventure").document(id)
            return docRef.get()
        }

        fun Update(summary: String, adventure: Adventure): Adventure{
            FirebaseFirestore.getInstance().collection("adventure").document(adventure.id).update(
                            "summary", adventure.summary
                    )
            adventure.summary = summary
            return adventure
        }

        fun AddPlayer(id: String, player: Player): String{
            val ref = FirebaseFirestore.getInstance().collection("adventure").document(id)
                    .collection("players").document()
            player.id = ref.id
            ref.set(player)

            return player.id
        }

        fun List(): Task<QuerySnapshot> {
            val docRef = FirebaseFirestore.getInstance().collection("adventure")
            return docRef.get()
        }
    }
}