package lab7.models

import java.io.Reader
import java.io.StreamTokenizer
import java.util.*
import kotlin.math.abs

class ScoringMatrix @JvmOverloads constructor(input: Reader?, caseSensitive: Boolean = true) :
    ScoringScheme(caseSensitive) {
     var colCodes: String? = null
     var rowCodes: String? = null
     var matrix: Array<IntArray>
     var dimension: Int
     var maxAbsoluteScore: Int


    init {
        val buf = StringBuffer()
        var row: Int
        var col: Int
        var max_abs = 0
        var c: Char

        val contains = StreamTokenizer(input)
        contains.commentChar(COMMENT_CHAR.code)
        contains.eolIsSignificant(true)
        contains.nextToken()

        while (contains.ttype == StreamTokenizer.TT_EOL) {
            contains.nextToken()
        }

        while (contains.ttype != StreamTokenizer.TT_EOF && contains.ttype != StreamTokenizer.TT_EOL) {
            when (contains.ttype) {
                StreamTokenizer.TT_WORD -> {
                    if (contains.sval.length > 1) {
                        throw Exception("Column headers must have one-character only.")
                    }
                    buf.append(contains.sval[0])
                }
                INDEL_CHAR.code -> {
                    buf.append(INDEL_CHAR)
                }
                else -> {
                    throw Exception("Column headers must be one-character codes or the special character '$INDEL_CHAR'.")
                }
            }
            contains.nextToken()
        }

        colCodes = if (caseSensitive) buf.toString() else buf.toString().uppercase(Locale.getDefault())
        dimension = colCodes!!.length

        if (colCodes!!.indexOf(INDEL_CHAR) == -1) {
            throw Exception("Matrix have no column for deletion penalties.")
        }

        if (dimension < 2) {
            throw Exception("Matrix must have at least one column with a character code.")
        }

        for (i in 0 until dimension) {
            if (colCodes!!.indexOf(colCodes!![i], i + 1) > i) {
                throw Exception("Columns must have distinct one-character codes.")
            }
        }

        matrix = Array(dimension) { IntArray(dimension) }

        buf.delete(0, dimension)

        contains.eolIsSignificant(false)
        if (contains.ttype == StreamTokenizer.TT_EOL) contains.nextToken()

        row = 0
        while (row < dimension && contains.ttype != StreamTokenizer.TT_EOF) {
            when (contains.ttype) {
                StreamTokenizer.TT_WORD -> {
                    if (contains.sval.length > 1) {
                        throw Exception("Codes must have one character only.")
                    }
                    buf.append(contains.sval[0])
                }
                INDEL_CHAR.code -> {
                    buf.append(INDEL_CHAR)
                }
                else -> {
                    throw Exception("Rows must start with an one-character code or the special character '$INDEL_CHAR'.")
                }
            }

            col = 0
            while (col < dimension) {
                if (contains.nextToken() != StreamTokenizer.TT_NUMBER) {
                    throw Exception("Invalid value at row " + (row + 1) + ", column " + (col + 1) + ".")
                }
                matrix[row][col] = contains.nval.toInt()
                if (abs(matrix[row][col]) > max_abs) {
                    max_abs = abs(matrix[row][col])
                }
                col++
            }
            contains.nextToken()
            row++
        }

        rowCodes = if (caseSensitive){
            buf.toString()
        } else {
            buf.toString().uppercase(Locale.getDefault())
        }

        if (rowCodes!!.length != dimension) {
            throw Exception("Matrix must have as many rows as columns.")
        }

        if (rowCodes!!.indexOf(INDEL_CHAR) == -1) {
            throw Exception("Matrix have no row for insertion penalties.")
        }

        for (i in 0 until dimension) {
            if (rowCodes!!.indexOf(rowCodes!![i], i + 1) > i) {
                throw Exception("Rows must have distinct one-character codes.")
            }
        }

        for (i in 0 until dimension) if (colCodes!!.indexOf(rowCodes!![i].also { c = it }) == -1) {
            throw Exception("There is no corresponding column for row character '$c'.")
        }

        maxAbsoluteScore = max_abs
    }


    @Throws(Exception::class)
    override fun scoreSubstitution(a: Char, b: Char): Int {
        val r: Int
        val c: Int

        if (super.isCaseSensitive) {
            r = rowCodes!!.indexOf(a)
            c = colCodes!!.indexOf(b)
        } else {
            r = rowCodes!!.indexOf(a.uppercaseChar())
            c = colCodes!!.indexOf(b.uppercaseChar())
        }

        if (r < 0 || c < 0) {
            throw Exception("Substitution of character $a for $b is not defined.")
        }

        return matrix[r][c]
    }


    @Throws(Exception::class)
    override fun scoreInsertion(a: Char): Int {
        return scoreSubstitution(INDEL_CHAR, a)
    }


    @Throws(Exception::class)
    override fun scoreDeletion(a: Char): Int {
        return scoreSubstitution(a, INDEL_CHAR)
    }


    override val isPartialMatchSupported: Boolean
        get() = true


    override fun maxAbsoluteScore(): Int {
        return maxAbsoluteScore
    }


    override fun toString(): String {
        var row: Int
        var col: Int
        val buf = StringBuffer()


        buf.append("Scoring matrix:\n\t")
        col = 0
        while (col < dimension) {
            buf.append("\t" + col)
            col++
        }
        buf.append("\n\t")

        col = 0
        while (col < dimension) {
            buf.append('\t')
            buf.append(colCodes!![col])
            col++
        }
        row = 0
        while (row < dimension) {
            buf.append("$row  ${rowCodes!![row]}")
            col = 0
            while (col < dimension) {
                buf.append('\t')
                buf.append(matrix[row][col])
                col++
            }
            row++
        }
        return buf.toString()
    }

    companion object {
        private const val INDEL_CHAR = '*'
        private const val COMMENT_CHAR = '#'
    }
}
