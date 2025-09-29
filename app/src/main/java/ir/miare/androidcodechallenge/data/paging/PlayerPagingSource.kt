package ir.miare.androidcodechallenge.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ir.miare.androidcodechallenge.data.local.LocalPlayerDataSource
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.util.SortOption
import kotlinx.coroutines.flow.first

class PlayerPagingSource(
    private val localDataSource: LocalPlayerDataSource,
    private val sortOption: SortOption,
    private val loadData: suspend () -> Unit
) : PagingSource<Int, Player>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Player> {
        return try {
            loadData()
            val currentPage = params.key ?: 0
            val players = localDataSource.allPlayers.first()

            val sortedPlayers = when (sortOption) {
                SortOption.NAME_ASC -> players.sortedBy { it.name }
                SortOption.NAME_DESC -> players.sortedByDescending { it.name }
                SortOption.GOALS_ASC -> players.sortedBy { it.totalGoal }
                SortOption.GOALS_DESC -> players.sortedByDescending { it.totalGoal }
                SortOption.TEAM_RANK_ASC -> players.sortedBy { it.team.rank }
                SortOption.TEAM_RANK_DESC -> players.sortedByDescending { it.team.rank }
            }

            val startIndex = currentPage * params.loadSize
            val endIndex = minOf(startIndex + params.loadSize, sortedPlayers.size)
            val pageData = if (startIndex <= sortedPlayers.lastIndex) {
                sortedPlayers.subList(startIndex, endIndex)
            } else {
                emptyList()
            }

            LoadResult.Page(
                data = pageData,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (endIndex >= sortedPlayers.size) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Player>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
