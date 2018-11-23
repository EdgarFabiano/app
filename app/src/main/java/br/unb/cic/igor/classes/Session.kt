package br.unb.cic.igor.classes

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.io.Serializable
import java.util.*

data class Session(var id: String = "", var adventureId: String = "", var name: String = "", var date: Date = Date(), var summary: String = "") : Serializable {

    companion object {
        fun Insert(session: Session, adventureId: String): Session{
            session.adventureId = adventureId
            var ref = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document()
            session.id = ref.id
            ref.set(session)
            return session
        }

        fun Update(session: Session, adventureId: String){
            var ref = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document(session.id).update(
                     "name", session.name,
                     "date", session.date,
                            "summary", session.summary
                    )
        }

        fun Get(id: String, adventureId: String): Task<DocumentSnapshot> {
            var docRef = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions").document(id)
            return docRef.get()
        }

        fun ListByAdventure(adventureId: String): Task<QuerySnapshot>{
            var colRef = FirebaseFirestore.getInstance().collection("adventure").document(adventureId)
                    .collection("sessions")

            return colRef.get()
        }

    }
}

