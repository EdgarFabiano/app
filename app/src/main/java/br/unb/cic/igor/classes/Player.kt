package br.unb.cic.igor.classes

public class Player(var userId: String, var name: String, var character: String, var description: String, var attrs: String, var info: String) {
    var messages: ArrayList<String> = ArrayList()
}

object PlayerContent{
    val PLAYERS: List<Player> = listOf(
            Player("brunin","Bruno","D4Rk 4vEnGeR", "Destruidor de lares", "High testosterone", ""),
            Player("avent","","OuTroAvenT", "Destruidor de joaninhas", "High strengh", "")
    )
}