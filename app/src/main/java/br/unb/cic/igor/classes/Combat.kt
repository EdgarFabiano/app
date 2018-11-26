package br.unb.cic.igor.classes

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.io.Serializable

// Current turns are not included in the turns array
data class Combat(var id: String = "", var currentTurn: Turn = Turn(), var turns: ArrayList<Turn> = ArrayList()) : Serializable {
    companion object {
        fun Insert(sessionId: String, adventureId: String, combat: Combat): Combat{
            val ref = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document(sessionId).collection("combats").document()
            combat.id = ref.id
            ref.set(combat)
            return combat
        }

        private fun turnToMap(turn: Turn): Map<String, Any>{
            val turnMap = HashMap<String, Any>()
            turnMap["id"] = turn.id
            turnMap["status"] = turn.status
            turnMap["description"] = turn.description
            turnMap["availablePlayers"] = turn.availablePlayers

            return turnMap

        }

        // We should pass the already updated combat object to this function
        fun Update(sessionId: String, adventureId: String, combat: Combat){
            val turnMap = turnToMap(combat.currentTurn)
            val turnMapList = ArrayList<Map<String, Any>>()
            for (t in combat.turns){
                turnMapList.add(turnToMap(t))
            }

            val combatMap = HashMap<String, Any>()
            combatMap["currentTurn"] = turnMap
            combatMap["turns"] = turnMapList

            FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document(sessionId).collection("combats").document(combat.id)
                    .update(combatMap)
        }

        fun Get(id: String, sessionId: String, adventureId: String): Task<DocumentSnapshot> {
            val docRef = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document(sessionId).collection("combats").document(id)
            return docRef.get()
        }

        fun List(sessionId: String, adventureId: String): Task<QuerySnapshot> {
            val colRef = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document(sessionId).collection("combats")
            return colRef.get()
        }
    }
}

enum class TurnState {
    NOT_STARTED,
    STARTING,
    WAITING_ACTIONS,
    REVIEWING_ACTIONS,
    WAITING_ROLLS,
    FINISHED,
    ENDING_COMBAT
}

data class Turn(var id: Int = 0, var status: TurnState = TurnState.NOT_STARTED, var description: String = "", var availablePlayers: ArrayList<String> = ArrayList()) : Serializable

data class PlayerAction(var id: String = "", var turnId: Int = 0, var userId: String = "", var description: String = "", var successRate: Int? = null, var actionResult: Int? = null) : Serializable {
    companion object {
        fun Insert(adventureId: String, sessionId: String, combatId: String, action: PlayerAction): PlayerAction{
            val ref = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document(sessionId).collection("combats").document(combatId).collection("playerActions").document()
            action.id = ref.id
            ref.set(action)
            return action
        }

        // We should pass the already updated combat object to this function
        fun Update(adventureId: String, sessionId: String, combatId: String, action: PlayerAction){
            FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document(sessionId).collection("combats").
                            document(combatId).collection("playerActions").document(action.id).update(
                            "description", action.description,
                            "successRate", action.successRate,
                            "actionResult", action.actionResult
                    )
        }

        fun Get(adventureId: String, sessionId: String, combatId: String, actionId: String): Task<DocumentSnapshot> {
            val docRef = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document(sessionId).collection("combats").document(combatId).
                            collection("playerActions").document(actionId)
            return docRef.get()
        }

        fun List(adventureId: String, sessionId: String, combatId: String): Task<QuerySnapshot> {
            val colRef = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document(sessionId).collection("combats").document(combatId).collection("playerActions")
            return colRef.get()
        }
    }
}