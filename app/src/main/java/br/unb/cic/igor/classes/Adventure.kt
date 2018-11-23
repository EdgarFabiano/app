package br.unb.cic.igor.classes

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.io.Serializable

data class Adventure(var id: String = "", var name: String = "", var summary : String = "", var master: Master = Master()) : Serializable {

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

        fun Update(adventure: Adventure): Adventure{
            FirebaseFirestore.getInstance().collection("adventure").document(adventure.id).update(
                            "summary", adventure.summary,
                    "name", adventure.name
                    )

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