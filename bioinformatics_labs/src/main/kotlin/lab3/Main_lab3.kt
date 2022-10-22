package lab3

import MutableMatrix
import NeedlemanWunsch
import loadMatrix

fun main(args: Array<String>) {
    val gap: Int = -5
    val matchFile: String = "src/main/kotlin/lab1/" + args[2]

    val seqA = "ACAGT"
    val seqB = "ACGT"

    val result = Hirschberg(seqA, seqB, gap, matchFile)
    println(result)
}

fun Hirschberg(
    seqA: String,
    seqB: String,
    gap: Int,
    matchFile: String
): Pair<Int, Pair<String, String>> {
    val scoreMatrixAndDict: Pair<MutableMatrix<Int>, MutableMap<String, Int>> = loadMatrix(matchFile)
    val scoreMatrix: MutableMatrix<Int> = scoreMatrixAndDict.first
    val dict: MutableMap<String, Int> = scoreMatrixAndDict.second

    val s1 = "-${seqA}"
    val s2 = "-${seqB}"

    val ans = alignRecursive(s1, s2, gap, matchFile, scoreMatrix, dict)
    return Pair(if (ans.first > 0) ans.first + gap else ans.first, Pair(ans.second.first.substring(1), ans.second.second.substring(1)))
}

fun lastRow(s1: String, s2: String, gap: Int, scoreMatrix: MutableMatrix<Int>, dict: MutableMap<String, Int>) : MutableList<Int> {
    val len1 = s1.length
    val len2 = s2.length

    var preRow = mutableListOf<Int>()
    for (i in 0 until len2 + 1) {
        preRow.add(0)
    }

    var curRow = mutableListOf<Int>()
    for (i in 0 until len2 + 1) {
        curRow.add(0)
    }

    for (j in 1 until len2 + 1) {
        preRow[j] = preRow[j - 1] + gap
    }

    for (i in 1 until len1 + 1) {
        curRow[0] = gap + preRow[0]
        for (j in 1 until len2 + 1) {
            val scoreSub = preRow[j - 1] + scoreMatrix[
                    dict.getValue(s1[i - 1].toString()),
                    dict.getValue(s2[j - 1].toString())
            ]
            val scoreDel = preRow[j] + gap
            val scoreIns = curRow[j - 1] + gap
            curRow[j] = maxOf(scoreSub, scoreDel, scoreIns)
        }

        preRow = curRow
        curRow = mutableListOf()
        for (index in 0 until len2 + 1) {
            curRow.add(0)
        }
    }

    return preRow
}

fun alignRecursive(
    s1: String,
    s2: String,
    gap: Int,
    matchFile: String,
    scoreMatrix: MutableMatrix<Int>,
    dict: MutableMap<String, Int>
): Pair<Int, Pair<String, String>> {
    var aligned1 = ""
    var aligned2 = ""

    val len1 = s1.length
    val len2 = s2.length
    var ansScore = 0

    if (len1 == 0) {
        for (i in 0 until len2) {
            aligned1 = "${aligned1}-"
            aligned2 += s2[i]
            ansScore += gap
        }
    } else if (len2 == 0) {
        for (i in 0 until len1) {
            aligned1 += s1[i]
            aligned2 = "${aligned2}-"
            ansScore += gap
        }
    } else if (s1.length == 1 || s2.length == 1) {
        val needleman = NeedlemanWunsch(s1, s2, gap, matchFile)
        ansScore = needleman.first
        aligned1 = needleman.second!!.first
        aligned2 = needleman.second!!.second
    } else {
        val mid1 = (len1 / 2)
        val rowLeft = lastRow(s1.slice(0 until mid1), s2, gap, scoreMatrix, dict)
        val rowRight = lastRow(s1.slice(mid1 until s1.length).reversed(), s2.reversed(), gap, scoreMatrix, dict)

        rowRight.reverse()

        val row = mutableListOf<Int>()
        for (i in (rowLeft zip rowRight)) {
            row.add(i.first + i.second)
        }

        val maxIdx = row.indexOf(row.max())

        val mid2 = maxIdx

        var pair = alignRecursive(s1.slice(0 until mid1), s2.slice(0 until mid2), gap, matchFile, scoreMatrix, dict)
        val scoreLeft = pair.first
        val aligned1Left = pair.second.first
        val aligned2Left = pair.second.second

        pair = alignRecursive(s1.slice(mid1 until s1.length), s2.slice(mid2 until s2.length), gap, matchFile, scoreMatrix, dict)
        val scoreRight = pair.first
        val aligned1Right = pair.second.first
        val aligned2Right = pair.second.second

        aligned1 = aligned1Left + aligned1Right
        aligned2 = aligned2Left + aligned2Right

        ansScore = scoreLeft + scoreRight
    }

    return Pair(ansScore, Pair(aligned1, aligned2))
}
