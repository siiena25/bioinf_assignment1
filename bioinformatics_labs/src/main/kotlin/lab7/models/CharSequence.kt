package lab7.models

import java.io.BufferedReader
import java.io.Reader
import java.lang.Exception

class CharSequence(reader: Reader) {
    private var sequence: CharArray
    
    init {
        var ch: Int
        var c: Char
        val input = BufferedReader(reader)
        val buf = StringBuffer()


        while (input.read().also { ch = it } != -1) {
            c = ch.toChar()

            if (c == '>') {
                input.readLine()
            } else if (Character.isLetter(c)) {
                buf.append(c)
            } else if (!Character.isWhitespace(c)) {
                throw Exception("Sequences can contain letters only.")
            }
        }
        sequence = if (buf.isNotEmpty()) {
            CharArray(buf.length)
        } else {
            throw Exception("Empty sequence.")
        }
        buf.getChars(0, buf.length, sequence, 0)
    }

    fun length(): Int {
        return sequence.size
    }

    fun charAt(pos: Int): Char {
        return sequence[pos - 1]
    }
    
    override fun toString(): String {
        return String(sequence)
    }
}