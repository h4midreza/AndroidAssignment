package ir.miare.androidcodechallenge.presentation.players

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.usecase.GetPlayersUseCase
import ir.miare.androidcodechallenge.domain.usecase.ToggleFollowPlayerUseCase
import ir.miare.androidcodechallenge.domain.util.SortOption
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val application: Application,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val toggleFollowPlayerUseCase: ToggleFollowPlayerUseCase
) : ViewModel() {

    private val _sortOption = MutableStateFlow(SortOption.NAME_ASC)
    val sortOption = _sortOption.asStateFlow()

    private val _uiState = MutableStateFlow<PlayersUiState>(PlayersUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val players: Flow<PagingData<Player>> = sortOption
        .flatMapLatest { sort ->
            _uiState.value = PlayersUiState.Loading
            try {
                delay(1000)

                val playersFlow = getPlayersUseCase(sort).cachedIn(viewModelScope)
                _uiState.value = PlayersUiState.Success(playersFlow)
                playersFlow
            } catch (e: Exception) {
                val message = when (e) {
                    is UnknownHostException -> application.getString(R.string.error_no_internet)
                    is SocketTimeoutException -> application.getString(R.string.error_connection_timeout)
                    is IOException -> application.getString(R.string.error_network_error)
                    else -> application.getString(
                        R.string.error_failed_to_load_players,
                        e.localizedMessage ?: application.getString(R.string.error_unknown)
                    )
                }
                _uiState.value = PlayersUiState.Error(message)
                flowOf(PagingData.empty())
            }
        }
        .cachedIn(viewModelScope)

    init {
        _uiState.value = PlayersUiState.Loading
    }

    fun updateSortOption(newSortOption: SortOption) {
        _sortOption.value = newSortOption
    }

    fun toggleFollowPlayer(playerId: String) {
        viewModelScope.launch {
            try {
                toggleFollowPlayerUseCase(playerId)
            } catch (e: Exception) {
                val message = when (e) {
                    is UnknownHostException -> application.getString(R.string.error_no_internet)
                    is SocketTimeoutException -> application.getString(R.string.error_connection_timeout)
                    is IOException -> application.getString(R.string.error_network_error)
                    else -> application.getString(
                        R.string.error_failed_to_update_player_status,
                        e.localizedMessage ?: application.getString(R.string.error_unknown)
                    )
                }
                _errorMessage.value = message
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun retryLoading() {
        _uiState.value = PlayersUiState.Loading
        val currentSort = _sortOption.value
        _sortOption.value = currentSort
    }
}
