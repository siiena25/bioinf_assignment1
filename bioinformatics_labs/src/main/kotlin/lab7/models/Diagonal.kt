package lab7.models

import lab7.Fasta

class Diagonal(id: Int) : Comparable<Diagonal> {
    override fun toString(): String {
        return ("Diagonal [diagonalId=$diagonalId, score=$score, runs=$diagonalRuns]")
    }

    private val diagonalId: Int
    private var totalDiagonalRunScore: Int

    var score: Int
    val diagonalRuns: ArrayList<DiagonalRun>

    init {
        diagonalId = id
        score = 0
        totalDiagonalRunScore = Int.MIN_VALUE
        diagonalRuns = ArrayList()
    }

    private fun increaseScore() {
        score++
    }

    fun increaseScore(quantity: Int) {
        score += quantity
    }

    fun addDiagonalRun(run: DiagonalRun) {
        diagonalRuns.add(run)
    }

    fun addDiagonalRun(i: Int, j: Int) {
        diagonalRuns.add(DiagonalRun(i, j))
    }

    override fun compareTo(other: Diagonal): Int {
        return other.score - score
    }

    fun getTotalDiagonalRunScore(): Int {
        if (totalDiagonalRunScore == Int.MIN_VALUE) {
            totalDiagonalRunScore = 0
            for (dr in diagonalRuns) {
                totalDiagonalRunScore += dr.score
            }
        }
        return totalDiagonalRunScore
    }

    fun addHotSpot(hs: HotSpot) {
        if (diagonalRuns.size == 0) {
            val run = DiagonalRun(hs.i, hs.j)
            run.addHotSpot(hs)
            diagonalRuns.add(run)
        } else {
            var fallback = true
            for (r in diagonalRuns) {
                if (canExtend(hs, r)) {
                    diagonalRuns[diagonalRuns.size - 1].addHotSpot(hs)
                    fallback = false
                    break
                }
            }
            if (fallback) {
                val run = DiagonalRun(hs.i, hs.j)
                run.addHotSpot(hs)
                diagonalRuns.add(run)
            }
        }
        increaseScore()
    }

    private fun canExtend(hs: HotSpot, run: DiagonalRun): Boolean {
        val first = run.firstHotSpot
        val last = run.lastHotSpot
        return (hs.contains(last!!.i + 1, last.j + 1)
                || first!!.contains(hs.i + 1, hs.j + 1))
    }
    
    val diagonalBounds: IntArray
        get() = intArrayOf(
            diagonalRuns[0].i,
            diagonalRuns[0].j,
            (diagonalRuns[diagonalRuns.size - 1].lastHotSpot?.i ?: 0) + Fasta.instance.kTup - 1,
            (diagonalRuns[diagonalRuns.size - 1].lastHotSpot?.j ?: 0) + Fasta.instance.kTup - 1
        )
}