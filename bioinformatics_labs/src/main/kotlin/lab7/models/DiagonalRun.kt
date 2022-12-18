package lab7.models

import java.util.*

class DiagonalRun(i: Int, j: Int) {
    private val id: Int
    val i: Int
    val j: Int
    var score: Int
    var hotSpostList: MutableList<HotSpot>?

    init {
        id = i - j
        this.i = i
        this.j = j
        score = 0
        hotSpostList = ArrayList()
    }

    fun finalize() {
        hotSpostList!!.clear()
        hotSpostList = null
    }

    override fun toString(): String {
        return ("DiagonalRun [id=" + id + ", i=" + i + ", j=" + j
                + ", hotSpostList=" + hotSpostList + "]")
    }

    private fun sort() {
        hotSpostList?.sort()
    }

    fun addHotSpot(hs: HotSpot) {
        sort()
        hotSpostList!!.add(hs)
    }

    fun getHotSpotAt(i: Int): HotSpot {
        return hotSpostList!![i]
    }

    val lastHotSpot: HotSpot?
        get() = if (hotSpostList!!.size > 0) hotSpostList!![hotSpostList!!.size - 1] else null

    val firstHotSpot: HotSpot?
        get() = if (hotSpostList!!.size > 0) hotSpostList!![0] else null

    fun extractStringFromReference(ref: String): String {
        val last = lastHotSpot
        val lastIndex = j + (last!!.j - j + last.k)
        return ref.substring(j, lastIndex)
    }

    fun extractStringFromQuery(qry: String): String {
        val last = lastHotSpot
        val lastIndex = i + (last!!.i - i + last.k)
        return qry.substring(i, lastIndex)
    }

    fun increaseScore(score: Int) {
        this.score += score
    }
}