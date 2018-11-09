package br.unb.cic.igor.classes

import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable
import java.util.*

data class Session(var name: String = "", var date: Date = Date(), var summary: String = "") : Serializable {

    companion object {
        fun Insert(session: Session, mDatabase: FirebaseFirestore){

        }
    }
}

