package ir.miare.androidcodechallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import ir.miare.androidcodechallenge.data.local.LocalPlayerDataSource
import ir.miare.androidcodechallenge.data.mapper.toPlayers
import ir.miare.androidcodechallenge.data.paging.PlayerPagingSource
import ir.miare.androidcodechallenge.data.remote.api.PlayerApiService
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.repository.PlayerRepository
import ir.miare.androidcodechallenge.domain.util.SortOption
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    private val localDataSource: LocalPlayerDataSource,
    private val apiService: PlayerApiService
) : PlayerRepository {

    private var isDataLoaded = false
    private val activePagingSources = mutableSetOf<PlayerPagingSource>()

    override fun getAllPlayers(sortBy: SortOption): Flow<PagingData<Player>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PlayerPagingSource(localDataSource, sortBy) { loadDataIfNeeded() }.also { pagingSource ->
                    synchronized(activePagingSources) {
                        activePagingSources.removeAll { it.invalid }
                        activePagingSources.add(pagingSource)
                    }
                }
            }
        ).flow
    }

    override fun getFollowedPlayers(): Flow<List<Player>> {
        return localDataSource.allPlayers.map { players ->
            players.filter { it.isFollowed }
        }
    }

    override suspend fun toggleFollowPlayer(playerId: String) {
        localDataSource.toggleFollowPlayer(playerId)
        synchronized(activePagingSources) {
            activePagingSources.forEach { it.invalidate() }
            activePagingSources.clear()
        }
    }

    private suspend fun loadDataIfNeeded() {
        if (!isDataLoaded) {
            try {
                val remoteData = apiService.getData()
                val players = remoteData.toPlayers()
                localDataSource.updatePlayers(players)
                isDataLoaded = true
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
