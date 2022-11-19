package lab4

import org.junit.jupiter.api.Test

class NeedlemanWunschTest {
    @Test
    fun test1() {
        val seqA = "ACGT"
        val seqB = "ACGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "ACGT")
        assert(result.first == 20)
    }

    @Test
    fun test2() {
        val seqA = "ACG"
        val seqB = "ACGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "ACG-")
        assert(result.second?.second == "ACGT")
        assert(result.first == 5)
    }

    @Test
    fun test3() {
        val seqA = "ACGT"
        val seqB = "ACG"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "ACGT")
        assert(result.second?.second == "ACG-")
        assert(result.first == 5)
    }

    @Test
    fun test4() {
        val seqA = "ACAGT"
        val seqB = "ACGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "ACAGT")
        assert(result.second?.second == "AC-GT")
        assert(result.first == 10)
    }

    @Test
    fun test5() {
        val seqA = "ACGT"
        val seqB = "ACAGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "AC-GT")
        assert(result.second?.second == "ACAGT")
        assert(result.first == 10)
    }

    @Test
    fun test6() {
        val seqA = "CAGT"
        val seqB = "ACAGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "-CAGT")
        assert(result.second?.second == "ACAGT")
        assert(result.first == 10)
    }

    @Test
    fun test7() {
        val seqA = "ACAGT"
        val seqB = "CAGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "ACAGT")
        assert(result.second?.second == "-CAGT")
        assert(result.first == 10)
    }

    @Test
    fun test8() {
        val seqA = "ACGT"
        val seqB = "A"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "ACGT")
        assert(result.second?.second == "A---")
        assert(result.first == -7)
    }

    @Test
    fun test9() {
        val seqA = "ACGT"
        val seqB = ""
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "ACGT")
        assert(result.second?.second == "----")
        assert(result.first == -13)
    }

    @Test
    fun test10() {
        val seqA = "A"
        val seqB = "ACGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "A---")
        assert(result.second?.second == "ACGT")
        assert(result.first == -7)
    }

    @Test
    fun test11() {
        val seqA = ""
        val seqB = "ACGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "----")
        assert(result.second?.second == "ACGT")
        assert(result.first == -13)
    }

    @Test
    fun test12() {
        val seqA = ""
        val seqB = ""
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "")
        assert(result.second?.second == "")
        assert(result.first == 0)
    }

    @Test
    fun test13() {
        val seqA = "TACGT"
        val seqB = "ATGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "TACGT")
        assert(result.second?.second == "-ATGT")
        assert(result.first == 1)
    }

    @Test
    fun test14() {
        val seqA = "TACGT"
        val seqB = "ATGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "TACGT")
        assert(result.second?.second == "-ATGT")
        assert(result.first == 1)
    }

    @Test
    fun test15() {
        val seqA = "ACGT"
        val seqB = "TAGTA"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "ACGT-")
        assert(result.second?.second == "TAGTA")
        assert(result.first == -8)
    }

    @Test
    fun test16() {
        val seqA = "TAGTA"
        val seqB = "ACGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "TAGTA")
        assert(result.second?.second == "ACGT-")
        assert(result.first == -8)
    }

    @Test
    fun test17() {
        val seqA = "ACGT"
        val seqB = "TAGT"
        val open = -1
        val extend = 0
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "-ACGT")
        assert(result.second?.second == "TA-GT")
        assert(result.first == 13)
    }

    @Test
    fun test18() {
        val seqA = "TAGT"
        val seqB = "ACGT"
        val open = 10
        val extend = 1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first?.length == 8)
        assert(result.first == 80)
    }

    @Test
    fun test19() {
        val seqA = "GGAGCCAAGGTGAAGTTGTAGCAGTGTGTCC"
        val seqB = "GACTTGTGGAACCTCTGTCCTCCGAGCTCTC"
        val open = -5
        val extend = -5
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first?.length == 36)
        assert(result.first == 8)
    }

    @Test
    fun test20() {
        val seqA = "AAAAAAATTTTTTT"
        val seqB = "TTTTTTTAAAAAAA"
        val open = -5
        val extend = -5
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first?.length == 21)
        assert(result.first == -35)
    }

    @Test
    fun test21() {
        val seqA = "ACGGCTT"
        val seqB = "ACGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "ACGGCTT")
        assert(result.second?.second == "ACG---T")
        assert(result.first == 8)
    }

    @Test
    fun test22() {
        val seqA = "ACGT"
        val seqB = "TAGT"
        val open = -10
        val extend = -1
        val match = 5
        val mismatch = -4

        val result = NeedlemanWunschModified(seqA, seqB, open, extend, match, mismatch)

        assert(result.second?.first?.length == result.second?.second?.length)
        assert(result.second?.first == "ACGT")
        assert(result.second?.second == "TAGT")
        assert(result.first == 2)
    }
}