package lab2

import Data
import prepareDataSet
import kotlin.math.max
import kotlin.math.min


fun main(args: Array<String>) {
    val dataFile: String = "src/main/kotlin/lab1/test1.fasta"
    val gap: Int = -5

    val match = 5
    val mismatch = -4

    val data: ArrayList<Data> = prepareDataSet(dataFile)
    val seqA: String = data[0].stringToCompare
    val seqB: String = data[1].stringToCompare

    val k = max(seqA.length - seqB.length + 1, 3)

    println(NeedlemanWunsch(seqA, seqB, gap, match, mismatch, k))
}

fun NeedlemanWunsch(
    seqA: String,
    seqB: String,
    gap: Int,
    match: Int,
    mismatch: Int,
    k: Int
): Float {
    val s1 = "_$seqA"
    val s2 = "_$seqB"

    val n = s1.length + 1
    val m = s2.length + 1

    val d1 = max(n - m, 0)
    val d2 = max(m - n, 0)

    val matrix: MutableList<MutableList<Float>> = startMatrix(n, m)

    matrix[0][0] = 0F

    for (i in 1 until min(k + d1, n)) {
        matrix[i][0] = matrix[i - 1][0] + gap
    }
    for (j in 1 until min(k + d2, m)) {
        matrix[0][j] = matrix[0][j - 1] + gap
    }

    for (i in 1 until n) {
        for (j in max(1, i - k - d1) until min(m, i + k + d2)) {
            val DIAG = matrix[i - 1][j - 1] + if (s1[i - 1] == s2[j - 1]) match else mismatch
            val LEFT = matrix[i][j - 1] + gap
            val TOP = matrix[i - 1][j] + gap

            matrix[i][j] = maxOf(DIAG, LEFT, TOP)
        }
    }
    return matrix[seqA.length][seqB.length]
}

fun startMatrix(n: Int, m: Int): MutableList<MutableList<Float>> {
    val matrix: MutableList<MutableList<Float>> = mutableListOf(mutableListOf())
    for (i in 0 until n) {
        val list = mutableListOf<Float>()
        for (j in 0 until m) {
            list.add(j, Float.NEGATIVE_INFINITY)
        }
        matrix.add(i, list)
    }
    return matrix
}