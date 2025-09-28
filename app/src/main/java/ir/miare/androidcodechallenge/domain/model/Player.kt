package ir.miare.androidcodechallenge.domain.model

data class Player(
    val id: String,
    val name: String,
    val team: Team,
    val totalGoal: Int,
    val league: String,
    val isFollowed: Boolean = false
)

data class Team(
    val name: String,
    val rank: Int
)

data class League(
    val id: String,
    val name: String,
    val country: String,
    val rank: Int,
    val totalMatches: Int
)
