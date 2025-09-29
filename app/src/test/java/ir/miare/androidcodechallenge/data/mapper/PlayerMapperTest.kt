package ir.miare.androidcodechallenge.data.mapper

import ir.miare.androidcodechallenge.data.model.FakeData
import ir.miare.androidcodechallenge.data.model.League
import ir.miare.androidcodechallenge.data.model.Player
import ir.miare.androidcodechallenge.data.model.Team
import org.junit.Test

class PlayerMapperTest {

    @Test
    fun `toPlayers should convert FakeData to domain Player list correctly`() {
        val fakeDataList = listOf(
            FakeData(
                league = League(
                    name = "Premier League",
                    country = "England",
                    rank = 1,
                    totalMatches = 380
                ),
                players = listOf(
                    Player(
                        name = "Lionel Messi",
                        team = Team(name = "Barcelona", rank = 1),
                        totalGoal = 25
                    ),
                    Player(
                        name = "Cristiano Ronaldo",
                        team = Team(name = "Real Madrid", rank = 2),
                        totalGoal = 30
                    )
                )
            ),
            FakeData(
                league = League(
                    name = "La Liga",
                    country = "Spain",
                    rank = 2,
                    totalMatches = 340
                ),
                players = listOf(
                    Player(
                        name = "Kylian Mbappe",
                        team = Team(name = "PSG", rank = 1),
                        totalGoal = 28
                    )
                )
            )
        )

        val result = fakeDataList.toPlayers()

        assert(result.size == 3)

        val firstPlayer = result[0]
        assert(firstPlayer.name == "Lionel Messi")
        assert(firstPlayer.team.name == "Barcelona")
        assert(firstPlayer.team.rank == 1)
        assert(firstPlayer.totalGoal == 25)
        assert(firstPlayer.league == "Premier League")
        assert(firstPlayer.id == "premier_league_lionel_messi_barcelona")
        assert(!firstPlayer.isFollowed)

        val secondPlayer = result[1]
        assert(secondPlayer.name == "Cristiano Ronaldo")
        assert(secondPlayer.team.name == "Real Madrid")
        assert(secondPlayer.totalGoal == 30)
        assert(secondPlayer.league == "Premier League")

        val thirdPlayer = result[2]
        assert(thirdPlayer.name == "Kylian Mbappe")
        assert(thirdPlayer.league == "La Liga")
    }

    @Test
    fun `toLeagues should convert FakeData to domain League list correctly`() {
        val fakeDataList = listOf(
            FakeData(
                league = League(
                    name = "Premier League",
                    country = "England",
                    rank = 1,
                    totalMatches = 380
                ),
                players = emptyList()
            ),
            FakeData(
                league = League(
                    name = "La Liga",
                    country = "Spain",
                    rank = 2,
                    totalMatches = 340
                ),
                players = emptyList()
            )
        )

        val result = fakeDataList.toLeagues()

        assert(result.size == 2)

        val premierLeague = result[0]
        assert(premierLeague.id == "premier_league")
        assert(premierLeague.name == "Premier League")
        assert(premierLeague.country == "England")
        assert(premierLeague.rank == 1)
        assert(premierLeague.totalMatches == 380)

        val laLiga = result[1]
        assert(laLiga.id == "la_liga")
        assert(laLiga.name == "La Liga")
        assert(laLiga.country == "Spain")
        assert(laLiga.rank == 2)
        assert(laLiga.totalMatches == 340)
    }

    @Test
    fun `toPlayers should handle empty list`() {
        val emptyList = emptyList<FakeData>()
        val result = emptyList.toPlayers()
        assert(result.isEmpty())
    }

    @Test
    fun `toLeagues should handle empty list`() {
        val emptyList = emptyList<FakeData>()
        val result = emptyList.toLeagues()
        assert(result.isEmpty())
    }
}
