package ir.miare.androidcodechallenge.domain.usecase

import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.model.Team
import ir.miare.androidcodechallenge.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetFollowedPlayersUseCaseTest {

    @Mock
    private lateinit var mockRepository: PlayerRepository

    private lateinit var useCase: GetFollowedPlayersUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetFollowedPlayersUseCase(mockRepository)
    }

    @Test
    fun `invoke should call repository to get followed players`() = runTest {
        val followedPlayers = listOf(
            Player(
                id = "1",
                name = "Test Player 1",
                team = Team(name = "Test Team", rank = 1),
                totalGoal = 10,
                isFollowed = true,
                league = "test_league"
            )
        )
        whenever(mockRepository.getFollowedPlayers()).thenReturn(flowOf(followedPlayers))

        val result = useCase.invoke()

        verify(mockRepository).getFollowedPlayers()
        val resultList = result.toList()
        assert(resultList.isNotEmpty())
        assert(resultList.first().size == 1)
        assert(resultList.first().first().isFollowed)
    }

    @Test
    fun `invoke should return empty list when no followed players`() = runTest {
        whenever(mockRepository.getFollowedPlayers()).thenReturn(flowOf(emptyList()))

        val result = useCase.invoke()

        val resultList = result.toList()
        assert(resultList.first().isEmpty())
    }
}
