package br.unb.cic.igor.extensions

import com.google.firebase.firestore.QuerySnapshot

fun <T>QuerySnapshot.toList(valueType : Class<T>) : List<T> {
    var result: ArrayList<T> = ArrayList()
    return this.map {
        it.toObject(valueType)
    }
}