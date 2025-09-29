package ir.miare.androidcodechallenge.presentation.players

import androidx.paging.PagingData
import ir.miare.androidcodechallenge.domain.model.Player
import kotlinx.coroutines.flow.Flow

sealed class PlayersUiState {
    object Loading : PlayersUiState()
    data class Success(val players: Flow<PagingData<Player>>) : PlayersUiState()
    data class Error(val message: String) : PlayersUiState()
}
