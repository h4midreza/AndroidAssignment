package ir.miare.androidcodechallenge.domain.usecase

import ir.miare.androidcodechallenge.domain.repository.PlayerRepository
import javax.inject.Inject

class ToggleFollowPlayerUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(playerId: String) {
        repository.toggleFollowPlayer(playerId)
    }
}
