package lab7

import java.lang.Integer.max
import java.util.*

class SmithWaterman(one: String, two: String) {
    private val one: String
    private val two: String
    private var matrix: Array<IntArray>?
    private var gap = 0
    private val match: Int
    private val o: Int
    private var l: Int
    private val e: Int

    init {
        this.one = "-" + one.lowercase(Locale.getDefault())
        this.two = "-" + two.lowercase(Locale.getDefault())
        match = 2

        o = -2
        l = 0
        e = -1

        matrix = Array(one.length + 1) { IntArray(two.length + 1) }
        for (i in one.indices) {
            for (j in two.indices) {
                matrix!![i][j] = 0
            }
        }
    }

    fun finalize() {
        matrix = null
    }


    fun computeSmithWaterman(): Int {
        for (i in one.indices) {
            for (j in two.indices) {
                gap = o + (l - 1) * e
                if (i != 0 && j != 0) {
                    if (one[i] == two[j]) {
                        l = 0
                        matrix!![i][j] = max(
                            0,
                            max(
                                matrix!![i - 1][j - 1] + match,
                                max(
                                    matrix!![i - 1][j] + gap,
                                    matrix!![i][j - 1] + gap
                                )
                            )
                        )
                    } else {
                        l++
                        matrix!![i][j] = max(
                            0,
                            max(
                                matrix!![i - 1][j - 1] + gap,
                                max(
                                    matrix!![i - 1][j] + gap,
                                    matrix!![i][j - 1] + gap
                                )
                            )
                        )
                    }
                }
            }
        }

        var longest = 0
        var iL = 0
        var jL = 0

        for (i in one.indices) {
            for (j in two.indices) {
                if (matrix!![i][j] > longest) {
                    longest = matrix!![i][j]
                    iL = i
                    jL = j
                }
            }
        }

        var i = iL
        var j = jL

        val actions = Stack<String>()
        while (i != 0 && j != 0) {
            if (max(matrix!![i - 1][j - 1], max(matrix!![i - 1][j], matrix!![i][j - 1])) == matrix!![i - 1][j - 1]) {
                actions.push("align")
                i -= 1
                j -= 1
            } else if (max(matrix!![i - 1][j - 1], max(matrix!![i - 1][j], matrix!![i][j - 1])) == matrix!![i][j - 1]) {
                actions.push("insert")
                j -= 1
            } else {
                actions.push("delete")
                i -= 1
            }
        }

        var alignOne = String()
        var alignTwo = String()
        val backActions = actions.clone() as Stack<*>

        for (element in one) {
            alignOne += element
            if (!actions.empty()) {
                val curAction = actions.pop()
                if (curAction == "insert") {
                    alignOne = "$alignOne-"
                    while (!actions.empty() && actions.peek() == "insert") {
                        alignOne = "$alignOne-"
                        actions.pop()
                    }
                }
            }
        }
        for (element in two) {
            alignTwo += element
            if (!backActions.empty()) {
                val curAction = backActions.pop()
                if (curAction == "delete") {
                    alignTwo = "$alignTwo-"
                    while (!backActions.empty() && backActions.peek() == "delete") {
                        alignTwo = "$alignTwo-"
                        backActions.pop()
                    }
                }
            }
        }

        println("$alignOne\n$alignTwo")
        backActions.clear()
        return longest
    }

    fun printMatrix() {
        for (i in one.indices) {
            if (i == 0) {
                for (z in two.indices) {
                    if (z == 0) print("   ")
                    print(two[z].toString() + "  ")
                    if (z == two.length - 1) println()
                }
            }
            for (j in two.indices) {
                if (j == 0) {
                    print(one[i].toString() + "  ")
                }
                print(matrix!![i][j].toString() + "  ")
            }
            println()
        }
        println()
    }
}