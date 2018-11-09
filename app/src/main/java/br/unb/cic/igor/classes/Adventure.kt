package br.unb.cic.igor.classes

data class Adventure(var summary : String, var master: Master, var players : ArrayList<Player>, var sessions : ArrayList<Session>) {

    companion object {
        
    }
}