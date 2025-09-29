package ir.miare.androidcodechallenge.domain.repository

import androidx.paging.PagingData
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.util.SortOption
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun getAllPlayers(sortBy: SortOption): Flow<PagingData<Player>>
    fun getFollowedPlayers(): Flow<List<Player>>
    suspend fun toggleFollowPlayer(playerId: String)
}
