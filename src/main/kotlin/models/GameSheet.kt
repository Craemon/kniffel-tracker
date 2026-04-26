package models

data class GameSheet(
    val id: Int? = null,
    val gameId: Int,
    val playerId: Int,
    var ones: Int? = null,
    var twos: Int? = null,
    var threes: Int? = null,
    var fours: Int? = null,
    var fives: Int? = null,
    var sixes: Int? = null,
    var threeOfAKind: Int? = null,
    var fourOfAKind: Int? = null,
    var fullHouse: Int? = null,
    var smallStraight: Int? = null,
    var largeStraight: Int? = null,
    var kniffel: Int? = null,
    var chance: Int? = null,
    var kniffelBonus: Int? = null
)
