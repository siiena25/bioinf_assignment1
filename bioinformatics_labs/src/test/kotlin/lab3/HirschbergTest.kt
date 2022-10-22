package lab3

import org.junit.jupiter.api.Test

class HirschbergTest {
    @Test
    fun test1() {
        val gap = -5
        val seqA = "ACGT"
        val seqB = "ACGT"
        val matchFile = "src/main/kotlin/lab1/BLOSUM62.txt"

        val result = Hirschberg(seqA, seqB, gap, matchFile)

        assert(result.second.first == "ACGT")
        assert(result.first == 20)
    }

    @Test
    fun test2() {
        val gap = -5
        val seqA = "ACG"
        val seqB = "ACGT"
        val matchFile = "src/main/kotlin/lab1/BLOSUM62.txt"

        val result = Hirschberg(seqA, seqB, gap, matchFile)

        assert(result.second.first == "ACG-")
        assert(result.second.second == "ACGT")
        assert(result.first == 10)
    }

    @Test
    fun test3() {
        val gap = -5
        val seqA = "ACGT"
        val seqB = "ACG"
        val matchFile = "src/main/kotlin/lab1/BLOSUM62.txt"

        val result = Hirschberg(seqA, seqB, gap, matchFile)

        assert(result.second.first == "ACGT")
        assert(result.second.second == "ACG-")
        assert(result.first == 10)
    }

    @Test
    fun test4() {
        val gap = -5
        val seqA = "ACAGT"
        val seqB = "ACGT"
        val matchFile = "src/main/kotlin/lab1/BLOSUM62.txt"

        val result = Hirschberg(seqA, seqB, gap, matchFile)

        assert(result.second.first == "ACAGT")
        assert(result.second.second == "AC-GT")
        assert(result.first == 15)
    }

    @Test
    fun test5() {
        val gap = -5
        val seqA = "ACGT"
        val seqB = "ACAGT"
        val matchFile = "src/main/kotlin/lab1/BLOSUM62.txt"

        val result = Hirschberg(seqA, seqB, gap, matchFile)

        assert(result.second.first == "AC-GT")
        assert(result.second.second == "ACAGT")
        assert(result.first == 15)
    }

    @Test
    fun test6() {
        val gap = -5
        val seqA = "CAGT"
        val seqB = "ACAGT"
        val matchFile = "src/main/kotlin/lab1/BLOSUM62.txt"

        val result = Hirschberg(seqA, seqB, gap, matchFile)

        assert(result.second.first == "-CAGT")
        assert(result.second.second == "ACAGT")
        assert(result.first == 15)
    }

    @Test
    fun test7() {
        val gap = -5
        val seqA = "ACAGT"
        val seqB = "CAGT"
        val matchFile = "src/main/kotlin/lab1/BLOSUM62.txt"

        val result = Hirschberg(seqA, seqB, gap, matchFile)

        assert(result.second.first == "ACAGT")
        assert(result.second.second == "-CAGT")
        assert(result.first == 15)
    }

    @Test
    fun test8() {
        val gap = -5
        val seqA = "ACGT"
        val seqB = "A"
        val matchFile = "src/main/kotlin/lab1/BLOSUM62.txt"

        val result = Hirschberg(seqA, seqB, gap, matchFile)

        assert(result.second.first == "ACGT")
        assert(result.second.second == "A---")
        assert(result.first == -10)
    }
}