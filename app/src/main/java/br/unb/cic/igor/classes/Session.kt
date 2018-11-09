package br.unb.cic.igor.classes

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable
import java.util.*

data class Session(var id: String = "", var adventureId: String = "", var name: String = "", var date: Date = Date(), var summary: String = "") : Serializable {

    companion object {
        fun Insert(session: Session, adventureId: String, db: FirebaseFirestore){
            session.adventureId = adventureId
            var ref = db.collection("adventure").document(adventureId)
                    .collection("sessions").document()
            session.id = ref.id
            ref.set(session)
        }

        fun Get(id: String, adventureId: String, db: FirebaseFirestore): Task<DocumentSnapshot> {
            var docRef = db.collection("adventure").document(adventureId)
                    .collection("sessions").document(id)
            return docRef.get()
        }
    }
}

