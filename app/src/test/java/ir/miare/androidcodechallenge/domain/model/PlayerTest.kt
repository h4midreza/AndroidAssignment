package ir.miare.androidcodechallenge.domain.model

import org.junit.Test

class PlayerTest {

    @Test
    fun `player creation should set correct properties`() {
        val team = Team(name = "Barcelona", rank = 1)
        val player = Player(
            id = "test_id",
            name = "Lionel Messi",
            team = team,
            totalGoal = 30,
            league = "la_liga",
            isFollowed = false
        )

        assert(player.id == "test_id")
        assert(player.name == "Lionel Messi")
        assert(player.team.name == "Barcelona")
        assert(player.team.rank == 1)
        assert(player.totalGoal == 30)
        assert(player.league == "la_liga")
        assert(!player.isFollowed)
    }

    @Test
    fun `player equality should work correctly`() {
        val team = Team(name = "Real Madrid", rank = 1)
        val player1 = Player(
            id = "1",
            name = "Cristiano Ronaldo",
            team = team,
            totalGoal = 25,
            league = "la_liga",
            isFollowed = false
        )
        val player2 = Player(
            id = "1",
            name = "Cristiano Ronaldo",
            team = team,
            totalGoal = 25,
            league = "la_liga",
            isFollowed = false
        )
        val player3 = Player(
            id = "2",
            name = "Karim Benzema",
            team = team,
            totalGoal = 20,
            league = "la_liga",
            isFollowed = false
        )
        assert(player1 == player2)
        assert(player1 != player3)
        assert(player1.hashCode() == player2.hashCode())
    }

    @Test
    fun `team creation should set correct properties`() {
        val team = Team(name = "Manchester City", rank = 1)

        assert(team.name == "Manchester City")
        assert(team.rank == 1)
    }
}
