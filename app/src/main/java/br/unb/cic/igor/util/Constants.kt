package br.unb.cic.igor.util

class Constants private constructor() {

    init {
        throw RuntimeException("No " + Constants::class.java.simpleName + " instances for you!")
    }

    companion object {
        var DEV_MODE: Boolean? = true
    }
}
