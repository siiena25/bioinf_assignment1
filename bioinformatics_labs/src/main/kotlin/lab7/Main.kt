package lab7

import lab7.models.CharSequence
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import kotlin.system.exitProcess

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                if (args.size != 3) {
                    println("Usage: java -jar fasta1 fasta2 match_file")
                    exitProcess(1)
                }
                val queryFile = args[0]     // "src/main/kotlin/lab7/data/file1.fasta"
                val referenceFile = args[1] // "src/main/kotlin/lab7/data/file2.fasta"
                val matchFilePath = args[2] // "src/main/kotlin/lab7/data/BLOSUM62.txt"

                val seq1 = CharSequence(BufferedReader(FileReader(queryFile)))
                val seq2 = CharSequence(BufferedReader(FileReader(referenceFile)))

                val fasta = Fasta(matchFilePath).setup(5, 16, seq1.toString(), seq2.toString())
                fasta.calc()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}