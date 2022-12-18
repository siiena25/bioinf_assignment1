
package lab7.models

import kotlin.math.abs

class BasicScoringScheme @JvmOverloads constructor(
    private var matchReward: Int,
    mismatchPenalty: Int,
    gapCost: Int,
    caseSensitive: Boolean = true
) : ScoringScheme(caseSensitive) {

    private var mismatchPenalty: Int
    private var gapCost: Int
    private var maxAbsoluteScore = 0

    init {
        this.mismatchPenalty = mismatchPenalty
        this.gapCost = gapCost

        maxAbsoluteScore =
            when {
                abs(matchReward) >= abs(mismatchPenalty) -> {
                    if (abs(matchReward) >= abs(gapCost)) {
                        abs(matchReward)
                    } else {
                        abs(gapCost)
                    }
                }
                abs(mismatchPenalty) >= abs(gapCost) -> {
                    abs(mismatchPenalty)
                }
                else -> {
                    abs(gapCost)
                }
            }
    }

    override fun scoreSubstitution(a: Char, b: Char): Int {
        return if (isCaseSensitive) {
            if (a == b) {
                matchReward
            } else {
                mismatchPenalty
            }
        } else if (a.lowercaseChar() == b.lowercaseChar()) {
            matchReward
        } else {
            mismatchPenalty
        }
    }

    override fun scoreInsertion(a: Char): Int {
        return gapCost
    }

    override fun scoreDeletion(a: Char): Int {
        return gapCost
    }

    override fun maxAbsoluteScore(): Int {
        return maxAbsoluteScore
    }

    override val isPartialMatchSupported: Boolean
        get() = false

    override fun toString(): String {
        return "Basic scoring scheme: match reward = " + matchReward +
                ", mismatch penalty = " + mismatchPenalty + ", gap cost = " + gapCost
    }
}