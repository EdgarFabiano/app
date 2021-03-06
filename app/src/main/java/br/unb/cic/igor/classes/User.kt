package br.unb.cic.igor.classes

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import br.unb.cic.igor.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


data class User(var id: String = "id", var email: String = "email", var username: String? = null, var birthdate: Date? = null, var gender: String? = null, var adventureRefs: ArrayList<String> = ArrayList()) : Serializable{

    companion object {
        private var instance: User? = null

        fun GetInstance(): User?{
            return instance
        }

        fun SetInstance(user: User?): User?{
            instance = user
            return instance
        }

        fun Insert(user: User){
            FirebaseFirestore.getInstance().collection("users").document(user.id).set(user)
        }

        fun Get(id: String): Task<DocumentSnapshot> {
            val docRef = FirebaseFirestore.getInstance().collection("users").document(id)
            return docRef.get()
        }

        fun List(): Task<QuerySnapshot> {
            val colRef = FirebaseFirestore.getInstance().collection("users")
            return colRef.get()
        }

        fun Adventures(): Task<QuerySnapshot>{
            val adRefs = this.GetInstance()!!.adventureRefs
            val docRef = FirebaseFirestore.getInstance().collection("adventure").takeIf {
                adRefs.contains(it.id)
            }
            return docRef?.get()!!
        }

        fun ManageAdventures(operation: String, adventureId: String){
            val user = GetInstance()!!

            if(operation == "add"){
                user.adventureRefs.add(adventureId)
            } else if (operation == "rm"){
                user.adventureRefs.remove(adventureId)
            }

            FirebaseFirestore.getInstance().collection("users").document(user.id).update(
                    "adventureRefs", user.adventureRefs
            )
            SetInstance(user)
        }
    }
}