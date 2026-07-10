package service

import models.GameSheet

class ScoreCalculatorService {
    fun calculateGrandTotal(gameSheet: GameSheet): Int {
        val upperSum = (gameSheet.ones ?: 0) +
                (gameSheet.twos ?: 0) +
                (gameSheet.threes ?: 0) +
                (gameSheet.fours ?: 0) +
                (gameSheet.fives ?: 0) +
                (gameSheet.sixes ?: 0)

        val upperBonus = if (upperSum >= 63) 35 else 0

        val lowerSum = (gameSheet.threeOfAKind ?: 0) +
                (gameSheet.fourOfAKind ?: 0) +
                (gameSheet.fullHouse ?: 0) +
                (gameSheet.smallStraight ?: 0) +
                (gameSheet.largeStraight ?: 0) +
                (gameSheet.kniffel ?: 0) +
                (gameSheet.chance ?: 0)

        val kniffelBonus = gameSheet.kniffelBonus ?: 0

        return upperSum + upperBonus + lowerSum + kniffelBonus
    }
}