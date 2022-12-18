package lab7

import lab7.models.*
import java.io.*
import java.util.*
import kotlin.math.abs

class Fasta(
    private val matchFilePath: String
) {
    var kTup = 0
    private var width = 0
    private var reference: String? = null
    private var query: String? = null
    private var targetReference: String? = null
    private var targetQuery: String? = null
    private var bestDiagonal: Diagonal? = null
    private var diagonals: MutableMap<Int, Diagonal>? = null

    fun setup(k: Int, w: Int, s1: String?, s2: String?): Fasta {
        reference = s1
        query = s2
        kTup = k
        width = if (w > 16) 0 else w
        if (w <= 0) {
            throw RuntimeException("Width must be between 1 and 16.")
        }
        bestDiagonal = null
        diagonals = mutableMapOf()
        return this
    }

    fun calc() {
        findKTuples()
        makeSumTable()
        applySubstitutionMatrix()
        selectBestDiagonal()
        joinDiagonals()
        performSmithWaterman()
    }

    private fun selectBestDiagonal(): Diagonal? {
        if (bestDiagonal == null) {
            var best: Diagonal? = null
            for (d in diagonals!!.values) {
                if (best == null) {
                    best = d
                } else {
                    val bestScore = best.getTotalDiagonalRunScore()
                    val currentScore = d.getTotalDiagonalRunScore()
                    if (currentScore > bestScore) {
                        best = d
                    }
                }
            }
            bestDiagonal = best
        }
        return bestDiagonal
    }

    private fun makeSumTable() {
        diagonals = sortByValue(diagonals!!).toMutableMap()
        if (diagonals!!.size > 10) {
            for ((i, key) in diagonals!!.keys.withIndex()) {
                if (i >= 10) diagonals?.remove(key)
            }
        }
    }

    private fun findKTuples() {
        val matchesInRef: HashMap<String?, ArrayList<Int>> = HashMap()
        val matchesInQuery: HashMap<String?, ArrayList<Int>> = HashMap()

        findKTuples(reference, matchesInRef)
        findKTuples(query, matchesInQuery)
        
        var hotspots: MutableMap<Int, TreeSet<HotSpot>> = mutableMapOf()
        for (str in matchesInRef.keys) {
            if (matchesInQuery.containsKey(str)) {
                val listRef = matchesInRef[str]!!
                val listQuery = matchesInQuery[str]!!
                for (j in listRef) {
                    for (i in listQuery) {
                        val diagId = i - j
                        val hs = HotSpot(i, j)
                        if (!hotspots.containsKey(diagId)) {
                            hotspots[diagId] = TreeSet<HotSpot>()
                        }
                        hotspots[diagId]?.add(hs)
                    }
                }
            }
        }
        hotspots = sortByValueSize(hotspots).toMutableMap()
        var i = 0
        for ((key, value) in hotspots) {
            if (i > 10) {
                break
            }
            val dlg = getDiagonal(key)
            for (h in value) {
                dlg!!.addHotSpot(h)
            }
            i++
        }
        hotspots.clear()
        matchesInQuery.clear()
        matchesInRef.clear()
    }

    private fun getDiagonal(id: Int): Diagonal? {
        if (!diagonals!!.containsKey(id)) {
            diagonals?.put(id, Diagonal(id))
        }
        return diagonals!![id]
    }

    private fun findKTuples(str: String?, matches: HashMap<String?, ArrayList<Int>>?) {
        val n = str!!.length
        for (i in 0 until n - kTup + 1) {
            val sub = str.substring(i, i + kTup)
            if (!matches!!.containsKey(sub)) {
                matches[sub] = ArrayList()
                matches[sub]!!.add(i)
            } else {
                matches[sub]!!.add(i)
            }
        }
    }

    private fun performSmithWaterman() {
        val sw = SmithWaterman(targetQuery!!, targetReference!!)
        println("Score: " + sw.computeSmithWaterman())
    }

    private fun joinDiagonals() {
        val bounds = bestDiagonal!!.diagonalBounds
        val halfW = width / 2
        val queryStart = bounds[0] - halfW + abs(bounds[0] - halfW)
        val refStart = bounds[1] - halfW + abs(bounds[1] - halfW)
        val queryEnd =
            bounds[2] + halfW - if (bounds[2] + halfW > query!!.length - 1) abs(bounds[2] + halfW - query!!.length - 1) else 0
        val refEnd =
            bounds[3] + halfW - if (bounds[3] + halfW > reference!!.length - 1) abs(bounds[3] + halfW - reference!!.length - 1) else 0

        targetQuery = query!!.substring(queryStart, queryEnd - 1)
        targetReference = reference!!.substring(refStart, refEnd - 1)
    }

    private fun applySubstitutionMatrix() {
        var fileReader: Reader? = null
        try {
            fileReader = FileReader(matchFilePath)
            val scoringMatrix: ScoringScheme = ScoringMatrix(BufferedReader(fileReader))
            for (key in diagonals!!.keys) {
                for (run in diagonals!![key]!!.diagonalRuns) {
                    val strRef = run.extractStringFromReference(reference!!)
                    val strQuery = run.extractStringFromQuery(query!!)
                    var score = 0
                    var i = 0
                    val l = strRef.length
                    while (i < l) {
                        score += scoringMatrix.scoreSubstitution(strRef[i], strQuery[i])
                        i++
                    }
                    run.increaseScore(score)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun <K, V : Comparable<V>?> sortByValue(map: Map<K, V>): Map<K, V> {
        val list: List<Map.Entry<K, V>> = LinkedList(map.entries)
        Collections.sort(list) { o1, o2 -> -1 * (o1.value)!!.compareTo(o2.value) }
        val result: MutableMap<K, V> = LinkedHashMap()
        for (entry: Map.Entry<K, V> in list) {
            result[entry.key] = entry.value
        }
        return result
    }

    private fun <K, T, V : Set<T>> sortByValueSize(map: Map<K, V>): Map<K, V> {
        val list: List<Map.Entry<K, V>> = LinkedList(map.entries)
        Collections.sort(list) { o1, o2 ->
            val l1 = o1.value.size
            val l2 = o2.value.size
            when {
                l1 > l2 -> -1
                l1 < l2 -> 1
                else -> 0
            }
        }
        val result: MutableMap<K, V> = LinkedHashMap()
        for (entry: Map.Entry<K, V> in list) {
            result[entry.key] = entry.value
        }
        return result
    }
}