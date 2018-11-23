package br.unb.cic.igor.util

import java.text.SimpleDateFormat
import java.util.Date

class FormatterUtil private constructor() {

    init {
        throw RuntimeException("No " + FormatterUtil::class.java.simpleName + " instances for you!")
    }

    companion object {
        fun formatDate(date: Date): String {
            val simpleDateFormat = SimpleDateFormat("dd/MM")
            return simpleDateFormat.format(date)
        }
    }
}
