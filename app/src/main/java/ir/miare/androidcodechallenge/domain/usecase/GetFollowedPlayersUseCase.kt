package ir.miare.androidcodechallenge.domain.usecase

import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFollowedPlayersUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Flow<List<Player>> {
        return repository.getFollowedPlayers()
    }
}
