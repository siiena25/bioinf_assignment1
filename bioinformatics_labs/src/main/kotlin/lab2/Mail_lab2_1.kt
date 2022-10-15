package lab2

import Data
import MutableMatrix
import loadMatrix
import prepareDataSet

fun main(args: Array<String>) {
    val dataFile: String = "src/main/kotlin/lab1/" + args[0]
    val gap: Int = args[1].toInt()
    val matchFile: String = "src/main/kotlin/lab1/" + args[2]

    val data: ArrayList<Data> = prepareDataSet(dataFile)
    val seqA: String = data[0].stringToCompare
    val seqB: String = data[1].stringToCompare

    val score = NeedlemanWunschWithoutAlign(seqA, seqB, gap, matchFile)
    println("Score: $score")
}

fun NeedlemanWunschWithoutAlign(
    seqA: String,
    seqB: String,
    gap: Int,
    matchFile: String
): Int {
    val scoreMatrixAndDict: Pair<MutableMatrix<Int>, MutableMap<String, Int>> = loadMatrix(matchFile)
    val scoreMatrix: MutableMatrix<Int> = scoreMatrixAndDict.first
    val dict: MutableMap<String, Int> = scoreMatrixAndDict.second

    val s1 = "_$seqA"
    val s2 = "_$seqB"

    val n = s1.length
    val m = s2.length

    val matrix: MutableList<MutableList<Int>> = getStartMatrix(n, m, gap)

    var score: Int
    for (i in 1 until n) {
        for (j in 1 until m) {
            val DIAG = matrix[i - 1][j - 1] + scoreMatrix[dict.getValue(s1[i].toString()), dict.getValue(s2[j].toString())]
            val LEFT = matrix[i][j - 1] + gap
            val TOP = matrix[i - 1][j] + gap

            if (DIAG > LEFT) {
                score = DIAG
            } else {
                score = LEFT
            }

            if (TOP > score) {
                score = TOP
            }
            matrix[i][j] = score
        }
    }
    return matrix[seqA.length][seqB.length]
}

fun getStartMatrix(n: Int, m: Int, gap: Int): MutableList<MutableList<Int>> {
    val matrix: MutableList<MutableList<Int>> = mutableListOf(mutableListOf())
    for (i in 0 until n) {
        val list = mutableListOf<Int>()
        for (j in 0 until m) {
            list.add(j, 0)
        }
        matrix.add(i, list)
    }
    for (j in 0 until n) {
        matrix[j][0] = j * gap
    }
    for (j in 0 until m) {
        matrix[0][j] = j * gap
    }
    return matrix
}