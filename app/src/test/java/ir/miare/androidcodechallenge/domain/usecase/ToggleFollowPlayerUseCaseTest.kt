package ir.miare.androidcodechallenge.domain.usecase

import ir.miare.androidcodechallenge.domain.repository.PlayerRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ToggleFollowPlayerUseCaseTest {

    @Mock
    private lateinit var mockRepository: PlayerRepository

    private lateinit var useCase: ToggleFollowPlayerUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = ToggleFollowPlayerUseCase(mockRepository)
    }

    @Test
    fun `invoke should call repository to toggle follow status`() = runTest {
        val playerId = "test_player_123"
        whenever(mockRepository.toggleFollowPlayer(playerId)).thenReturn(Unit)

        useCase.invoke(playerId)

        verify(mockRepository).toggleFollowPlayer(playerId)
    }

    @Test
    fun `invoke should handle repository exceptions`() = runTest {
        val playerId = "test_player_123"
        val exception = RuntimeException("Database error")
        whenever(mockRepository.toggleFollowPlayer(playerId)).thenThrow(exception)

        try {
            useCase.invoke(playerId)
            assert(false) { "Expected exception to be thrown" }
        } catch (e: RuntimeException) {
            assert(e.message == "Database error")
        }
    }
}
