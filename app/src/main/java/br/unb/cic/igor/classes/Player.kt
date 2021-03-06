package br.unb.cic.igor.classes

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.io.Serializable

data class Player(var id: String = "", var userId: String = "", var name: String? = "", var character: String = "", var description: String = "", var attrs: String = "", var info: String = "", var messages: ArrayList<String> = ArrayList()) : Serializable {


    companion object {
        fun Insert(player: Player, user: User, adventureId: String): Player{
            val db = FirebaseFirestore.getInstance()
            val batch = db.batch()
            val ref = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("players").document()
            player.id = ref.id
            batch.set(ref, player)

            user.adventureRefs.add(adventureId)

            val userRef = FirebaseFirestore.getInstance().collection("users").document(user.id)

            batch.update(userRef, "adventureRefs", user.adventureRefs)

            batch.commit()

            return player
        }

        fun Update(player: Player, adventureId: String){
            FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("players").document(player.id).update(
                            "name", player.name,
                            "character", player.character,
                            "description", player.description,
                            "attrs", player.attrs,
                            "info", player.info
                    )
        }

        fun AddMessage(message: String, player: Player, adventureId: String): Player{
            player.messages.add(message)
            var ref = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("players").document(player.id).update(
                            "messages", player.messages
                    )
            return player
        }

        fun Get(id: String, adventureId: String): Task<DocumentSnapshot> {
            var docRef = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("players").document(id)
            return docRef.get()
        }

        fun ListByAdventure(adventureId: String): Task<QuerySnapshot>{
            var colRef = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("players")

            return colRef.get()
        }
    }
}

object PlayerContent{
    val PLAYERS: List<Player> = listOf(
            Player("a","8v08uGYQYEbeor5IqUhq4fWAfxi2","Fernando","Lixo Extradordinário", "Lixo mesmo", "Low testosterone", "Fufinustes"),
            Player("b","FDSdPFONaiUMsIne3uUV83aO5iX2","Fábio","Dazor", "Destruidor de planetas", "High strengh", "DAZÓR")
    )
}