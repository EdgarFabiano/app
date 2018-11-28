package br.unb.cic.igor.classes

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.io.Serializable

data class Player(var id: String = "", var userId: String = "", var name: String? = "", var character: String = "", var description: String = "", var attrs: String = "", var info: String = "", var messages: ArrayList<String> = ArrayList()) : Serializable {


    companion object {
        fun Insert(player: Player, adventureId: String): Player{
            var ref = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("players").document()
            player.id = ref.id
            ref.set(player)

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
            Player("a","brunin","Bruno","D4Rk 4vEnGeR", "Destruidor de lares", "High testosterone", ""),
            Player("b","avent","","OuTroAvenT", "Destruidor de joaninhas", "High strengh", "")
    )
}