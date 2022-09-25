fun main(args: Array<String>) {
    val dataFile: String = "src/main/kotlin/lab1/" + args[0]
    val gap: Int = args[1].toInt()
    val matchFile: String = "src/main/kotlin/lab1/" + args[2]

    val data: ArrayList<Data> = prepareDataSet(dataFile)
    val seqA: String = data[0].stringToCompare
    val seqB: String = data[1].stringToCompare

    val result = NeedlemanWunsch(seqA, seqB, gap, matchFile)
    output(result)
}

fun output(result: Pair<Int, Pair<String, String>?>) {
    println(result.first)
    println(result.second?.first)
    println(result.second?.second)
}

fun NeedlemanWunsch(
    seqA: String,
    seqB: String,
    gap: Int,
    matchFile: String
): Pair<Int, Pair<String, String>?> {
    val scoreMatrixAndDict: Pair<MutableMatrix<Int>, MutableMap<String, Int>> = loadMatrix(matchFile)
    val scoreMatrix: MutableMatrix<Int> = scoreMatrixAndDict.first
    val dict: MutableMap<String, Int> = scoreMatrixAndDict.second

    val s1 = "_$seqA"
    val s2 = "_$seqB"

    val n = s1.length
    val m = s2.length

    val matrix: MutableList<MutableList<Int>> = getStartMatrix(n, m, gap)
    val ptrMatrix: MutableList<MutableList<Pair<Int, Int>>> = getPtrMatrix(n, m)

    var score: Int
    var anc: Pair<Int, Int>
    for (i in 1 until n) {
        for (j in 1 until m) {
            val DIAG = matrix[i - 1][j - 1] + scoreMatrix[dict.getValue(s1[i].toString()), dict.getValue(s2[j].toString())]
            val LEFT = matrix[i][j - 1] + gap
            val TOP = matrix[i - 1][j] + gap

            if (DIAG > LEFT) {
                score = DIAG
                anc = Pair(i - 1, j - 1)
            } else {
                score = LEFT
                anc = Pair(i, j - 1)
            }

            if (TOP > score) {
                score = TOP
                anc = Pair(i - 1, j)
            }

            matrix[i][j] = score
            ptrMatrix[i][j] = anc
        }
    }
    return Pair(matrix[seqA.length][seqB.length], align(s1, s2, ptrMatrix, "-"))
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