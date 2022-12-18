package lab7.models


abstract class ScoringScheme @JvmOverloads constructor(
    var isCaseSensitive: Boolean = true
) {
    @Throws(Exception::class)
    abstract fun scoreSubstitution(a: Char, b: Char): Int

    @Throws(Exception::class)
    abstract fun scoreInsertion(a: Char): Int
    
    @Throws(Exception::class)
    abstract fun scoreDeletion(a: Char): Int
    
    abstract fun maxAbsoluteScore(): Int
    
    abstract val isPartialMatchSupported: Boolean
}
