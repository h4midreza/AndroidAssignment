package ir.miare.androidcodechallenge.domain.usecase

import androidx.paging.PagingData
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.repository.PlayerRepository
import ir.miare.androidcodechallenge.domain.util.SortOption
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlayersUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke(sortBy: SortOption): Flow<PagingData<Player>> {
        return repository.getAllPlayers(sortBy)
    }
}
