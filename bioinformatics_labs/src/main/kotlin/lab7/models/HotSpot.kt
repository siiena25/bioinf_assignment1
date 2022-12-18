package lab7.models

import lab7.Fasta

class HotSpot(i: Int, j: Int) : Comparable<HotSpot> {
    val i: Int
    val j: Int
    val k: Int

    init {
        this.i = i
        this.j = j
        k = Fasta.instance.kTup
    }

    fun contains(i: Int, j: Int): Boolean {
        return this.i == i && this.j == j
    }

    val lastPoint: IntArray
        get() = intArrayOf(i + k - 1, j + k - 1)

    fun getKTupleFromReference(ref: String): String {
        return ref.substring(j, j + k)
    }

    fun getKTupleFromQuery(qry: String): String {
        return qry.substring(i, i + k)
    }

    override fun toString(): String {
        return "HotSpot [i=$i, j=$j, k=$k]"
    }

    override fun compareTo(other: HotSpot): Int {
        if (i < other.i) return -1
        if (i > other.i) return +1
        if (j < other.j) return -1
        return if (j > other.j) +1 else 0
    }
}