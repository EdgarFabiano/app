package br.unb.cic.igor.classes

import java.io.Serializable

data class Master(var userId: String = "", var name: String = "", var character: String = "", var description: String = "") : Serializable