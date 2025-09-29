package ir.miare.androidcodechallenge.data.local

import android.content.SharedPreferences
import ir.miare.androidcodechallenge.domain.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class LocalPlayerDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    private val followedPlayerIds = MutableStateFlow(getFollowedPlayerIds())

    private val _allPlayers = MutableStateFlow<List<Player>>(emptyList())
    val allPlayers: Flow<List<Player>> = _allPlayers.asStateFlow()

    fun updatePlayers(players: List<Player>) {
        val followedIds = followedPlayerIds.value
        val updatedPlayers = players.map { player ->
            player.copy(isFollowed = followedIds.contains(player.id))
        }
        _allPlayers.value = updatedPlayers
    }

    fun toggleFollowPlayer(playerId: String) {
        val currentFollowed = followedPlayerIds.value.toMutableSet()
        if (currentFollowed.contains(playerId)) {
            currentFollowed.remove(playerId)
        } else {
            currentFollowed.add(playerId)
        }
        saveFollowedPlayerIds(currentFollowed)
        followedPlayerIds.value = currentFollowed

        val updatedPlayers = _allPlayers.value.map { player ->
            if (player.id == playerId) {
                player.copy(isFollowed = !player.isFollowed)
            } else {
                player
            }
        }
        _allPlayers.value = updatedPlayers
    }

    private fun getFollowedPlayerIds(): Set<String> {
        return sharedPreferences.getStringSet(FOLLOWED_PLAYERS_KEY, emptySet()) ?: emptySet()
    }

    private fun saveFollowedPlayerIds(playerIds: Set<String>) {
        sharedPreferences.edit {
            putStringSet(FOLLOWED_PLAYERS_KEY, playerIds)
        }
    }

    companion object {
        private const val FOLLOWED_PLAYERS_KEY = "followed_players"
    }
}
