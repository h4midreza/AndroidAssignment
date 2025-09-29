package ir.miare.androidcodechallenge.presentation

import androidx.paging.PagingData
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.di.ResourceProvider
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.usecase.GetPlayersUseCase
import ir.miare.androidcodechallenge.domain.usecase.ToggleFollowPlayerUseCase
import ir.miare.androidcodechallenge.presentation.players.PlayersViewModel
import ir.miare.androidcodechallenge.util.SortOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PlayersViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var mockGetPlayersUseCase: GetPlayersUseCase
    @Mock
    private lateinit var mockToggleFollowPlayerUseCase: ToggleFollowPlayerUseCase
    @Mock
    private lateinit var resourceProvider: ResourceProvider

    private lateinit var viewModel: PlayersViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        whenever(resourceProvider.getString(R.string.error_no_internet)).thenReturn("No internet")
        whenever(resourceProvider.getString(R.string.error_connection_timeout)).thenReturn("Connection timeout")
        whenever(resourceProvider.getString(R.string.error_network_error)).thenReturn("Network error")
        whenever(resourceProvider.getString(R.string.error_unknown)).thenReturn("Unknown error")
        whenever(
            resourceProvider.getString(
                R.string.error_failed_to_load_players, "Network error"
            )
        ).thenReturn("Failed to load players: Network error")
        whenever(
            resourceProvider.getString(
                R.string.error_failed_to_update_player_status, "Network error"
            )
        ).thenReturn("Failed to update player status: Network error")

        val mockPagingData = PagingData.Companion.from(emptyList<Player>())
        whenever(mockGetPlayersUseCase.invoke(SortOption.NAME_ASC)).thenReturn(flowOf(mockPagingData))

        viewModel = PlayersViewModel(
            resourceProvider,
            mockGetPlayersUseCase,
            mockToggleFollowPlayerUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial sort option should be NAME_ASC`() {
        assert(viewModel.sortOption.value == SortOption.NAME_ASC)
    }

    @Test
    fun `toggleFollowPlayer should call use case successfully`() = runTest {
        val playerId = "test123"
        whenever(mockToggleFollowPlayerUseCase.invoke(playerId)).thenReturn(Unit)

        viewModel.toggleFollowPlayer(playerId)
        advanceUntilIdle()

        verify(mockToggleFollowPlayerUseCase).invoke(playerId)
        assert(viewModel.errorMessage.value == null)
    }
}