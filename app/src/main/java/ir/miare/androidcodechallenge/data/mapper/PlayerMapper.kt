package ir.miare.androidcodechallenge.data.mapper

import ir.miare.androidcodechallenge.data.model.FakeData
import ir.miare.androidcodechallenge.domain.model.League
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.model.Team

fun List<FakeData>.toPlayers(): List<Player> {
    return flatMap { fakeData ->
        fakeData.players.map { player ->
            Player(
                id = "${fakeData.league.name}_${player.name}_${player.team.name}".replace(" ", "_").lowercase(),
                name = player.name,
                team = Team(
                    name = player.team.name,
                    rank = player.team.rank
                ),
                totalGoal = player.totalGoal,
                league = fakeData.league.name,
                isFollowed = false
            )
        }
    }
}

fun List<FakeData>.toLeagues(): List<League> {
    return map { fakeData ->
        League(
            id = fakeData.league.name.replace(" ", "_").lowercase(),
            name = fakeData.league.name,
            country = fakeData.league.country,
            rank = fakeData.league.rank,
            totalMatches = fakeData.league.totalMatches
        )
    }
}
