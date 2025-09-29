package ir.miare.androidcodechallenge.domain.usecase

import androidx.paging.PagingData
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.model.Team
import ir.miare.androidcodechallenge.domain.repository.PlayerRepository
import ir.miare.androidcodechallenge.util.SortOption
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetPlayersUseCaseTest {

    @Mock
    private lateinit var mockRepository: PlayerRepository

    private lateinit var useCase: GetPlayersUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetPlayersUseCase(mockRepository)
    }

    @Test
    fun `invoke should call repository with correct sort option`() = runTest {
        val sortOption = SortOption.NAME_ASC
        val mockPagingData = PagingData.from(emptyList<Player>())
        whenever(mockRepository.getAllPlayers(sortOption)).thenReturn(flowOf(mockPagingData))

        val result = useCase.invoke(sortOption)
        result.collect {} // Consume the flow

        verify(mockRepository).getAllPlayers(sortOption)
    }

    @Test
    fun `invoke should return flow from repository`() = runTest {
        val sortOption = SortOption.GOALS_DESC
        val players = listOf(
            Player(
                id = "1",
                name = "Test Player",
                team = Team(name = "Test Team", rank = 1),
                totalGoal = 10,
                isFollowed = false,
                league = "test_league"
            )
        )
        val mockPagingData = PagingData.from(players)
        whenever(mockRepository.getAllPlayers(sortOption)).thenReturn(flowOf(mockPagingData))

        val result = useCase.invoke(sortOption)

        val resultList = result.toList()
        assert(resultList.isNotEmpty())
    }
}
