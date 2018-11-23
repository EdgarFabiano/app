package br.unb.cic.igor.classes

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import br.unb.cic.igor.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import kotlin.collections.ArrayList


data class User(var id: String = "id", var email: String = "email", var username: String? = null, var birthdate: Date? = null, var gender: String? = null, var adventureRefs: ArrayList<String> = ArrayList()){

    companion object {
        private var instance: User? = null

        fun GetInstance(): User?{
            return instance
        }

        fun SetInstance(user: User?): User?{
            instance = user
            return instance
        }

        fun Insert(user: User, mDatabase: FirebaseFirestore){
            mDatabase.collection("users").document(user.id).set(user)
        }

        fun Get(id: String, mDatabase: FirebaseFirestore): Task<DocumentSnapshot> {
            var docRef = mDatabase.collection("users").document(id)
            return docRef.get()
        }

        fun Adventures(mDb: FirebaseFirestore): Task<QuerySnapshot>{
            var adRefs = this.GetInstance()!!.adventureRefs
            var docRef = mDb.collection("adventure").takeIf {
                adRefs.contains(it.id)
            }
            return docRef?.get()!!
        }

        fun ManageAdventures(operation: String, adventureId: String, db: FirebaseFirestore){
            val user = GetInstance()!!

            if(operation == "add"){
                user.adventureRefs.add(adventureId)
            } else if (operation == "rm"){
                user.adventureRefs.remove(adventureId)
            }

            db.collection("users").document(user.id).update(
                    "adventureRefs", user.adventureRefs
            )
            SetInstance(user)
        }
    }
}