package br.unb.cic.igor.classes

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import br.unb.cic.igor.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


data class User(var userId: String = "id", var email: String = "email", var username: String? = null, var birthdate: Date? = null, var gender: String? = null){

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
            mDatabase.collection("users").document(user.userId).set(user)
        }

        fun Get(id: String, mDatabase: FirebaseFirestore): Task<DocumentSnapshot> {
            var docRef = mDatabase.collection("users").document(id);
            return docRef.get()
        }
    }
}