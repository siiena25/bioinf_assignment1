package lab4

import java.lang.Float.NEGATIVE_INFINITY

fun main(args: Array<String>) {
    val seqA: String = ""
    val seqB: String = "ACGT"

    val match = 5
    val mismatch = -4
    val open = -10
    val extend = -1

    val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)
    output(result)
}

fun output(result: Pair<Int, Pair<String, String>?>) {
    println(result.first)
    println(result.second?.first)
    println(result.second?.second)
}

fun NeedlemanWunschModified(
    seqA: String,
    seqB: String,
    open: Int,
    extend: Int,
    match: Int,
    mismatch: Int
): Pair<Int, Pair<String, String>?> {
    val s1 = "_$seqA"
    val s2 = "_$seqB"

    val n = s1.length
    val m = s2.length

    val matrixM: MutableList<MutableList<Int>> = initMatrixM(n, m, open, extend)
    val matrixI: MutableList<MutableList<Int>> = initMatrixI(n, m, open, extend)
    val matrixD: MutableList<MutableList<Int>> = initMatrixD(n, m, open, extend)

    val result = getEmptyMatrix(n, m)
    val ptrMatrix: MutableList<MutableList<Pair<Int, Int>>> = getPtrMatrix(n, m)

    for (i in 1 until n) {
        for (j in 1 until m) {
            var DIAG = matrixM[i - 1][j - 1] + (if (s1[i] == s2[j]) match else mismatch)
            var LEFT = matrixI[i - 1][j - 1] + (if (s1[i] == s2[j]) match else mismatch)
            val TOP  = matrixD[i - 1][j - 1] + (if (s1[i] == s2[j]) match else mismatch)
            matrixM[i][j] = maxOf(DIAG, LEFT, TOP)

            DIAG = matrixI[i][j - 1] + extend
            LEFT = matrixM[i][j - 1] + open
            matrixI[i][j] = maxOf(DIAG, LEFT)

            DIAG = matrixD[i - 1][j] + extend
            LEFT = matrixM[i - 1][j] + open
            matrixD[i][j] = maxOf(DIAG, LEFT)

            result[i][j] = maxOf(matrixM[i][j], matrixI[i][j], matrixD[i][j])

            if (result[i][j] == matrixM[i][j]) {
                ptrMatrix[i][j] = Pair(i - 1, j - 1)
            } else if (result[i][j] == matrixI[i][j]) {
                ptrMatrix[i][j] = Pair(i, j - 1)
            } else if (result[i][j] == matrixD[i][j]) {
                ptrMatrix[i][j] = Pair(i - 1, j)
            }
        }
    }
    return Pair(result[n - 1][m - 1], align(s1, s2, ptrMatrix, "-"))
}

fun initMatrixD(n: Int, m: Int, open: Int, extend: Int): MutableList<MutableList<Int>> {
    val matrixD = getEmptyMatrix(n, m)
    matrixD[0][0] = getInf(n, m, open, extend)
    for (i in 1 until n) {
        matrixD[i][0] = open + (i - 1) * extend
    }
    for (j in 1 until m) {
        matrixD[0][j] = open + (j - 1) * extend
    }
    return matrixD
}

fun initMatrixI(n: Int, m: Int, open: Int, extend: Int): MutableList<MutableList<Int>> {
    val matrixI = initMatrixM(n, m, open, extend)
    matrixI[0][0] = getInf(n, m, open, extend)
    return matrixI
}

fun initMatrixM(n: Int, m: Int, open: Int, extend: Int): MutableList<MutableList<Int>> {
    val matrixM = getEmptyMatrix(n, m)
    val infinity = getInf(n, m, open, extend)

    for (i in 1 until n) {
        matrixM[i][0] = infinity
    }
    for (j in 1 until m) {
        matrixM[0][j] = infinity
    }
    return matrixM
}

fun getInf(n: Int, m: Int, open: Int, extend: Int): Int {
    return 2 * open + (n + m) * extend + 1
}

fun getEmptyMatrix(n: Int, m: Int): MutableList<MutableList<Int>> {
    val matrix: MutableList<MutableList<Int>> = mutableListOf(mutableListOf())
    for (i in 0 until n) {
        val list = mutableListOf<Int>()
        for (j in 0 until m) {
            list.add(j, 0)
        }
        matrix.add(i, list)
    }
    return matrix
}

fun getPtrMatrix(n: Int, m: Int): MutableList<MutableList<Pair<Int, Int>>> {
    val ptrMatrix: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf(mutableListOf())
    for (i in 0 until n) {
        val list = mutableListOf<Pair<Int, Int>>()
        for (j in 0 until m) {
            list.add(j, Pair(0, 0))
        }
        ptrMatrix.add(i, list)
    }
    for (i in 1 until n) {
        ptrMatrix[i][0] = Pair(i - 1, 0)
    }
    for (j in 1 until m) {
        ptrMatrix[0][j] = Pair(0, j - 1)
    }
    return ptrMatrix
}

fun align(
    s1: String,
    s2: String,
    ptrMatrix: MutableList<MutableList<Pair<Int, Int>>>,
    gapSymbol: String
): Pair<String, String>? {
    val path = mutableListOf<Pair<Int, Int>>()
    var i = s1.length - 1
    var j = s2.length - 1

    while (i > 0 || j > 0) {
        path.add(Pair(i, j))
        val elem = ptrMatrix[i][j]
        i = elem.first
        j = elem.second
    }
    path.reverse()

    var res1 = ""
    var res2 = ""
    var iPrev = 0
    var jPrev = 0

    for (p in path) {
        if (p.first - iPrev == p.second - jPrev && p.second - jPrev == 1) {
            res1 += s1[p.first]
            res2 += s2[p.second]
        } else if (p.first - iPrev == 1 && p.second == jPrev) {
            res1 += s1[p.first]
            res2 += gapSymbol
        } else if (p.first == iPrev && p.second - jPrev == 1) {
            res1 += gapSymbol
            res2 += s2[p.second]
        } else {
            return null
        }
        iPrev = p.first
        jPrev = p.second
    }
    return Pair(res1, res2)
}