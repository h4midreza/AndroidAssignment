package ir.miare.androidcodechallenge.presentation.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.usecase.GetPlayersUseCase
import ir.miare.androidcodechallenge.domain.usecase.ToggleFollowPlayerUseCase
import ir.miare.androidcodechallenge.domain.util.SortOption
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
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
                //loading delay
                delay(1000)

                val playersFlow = getPlayersUseCase(sort).cachedIn(viewModelScope)
                _uiState.value = PlayersUiState.Success(playersFlow)
                playersFlow
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Please check your network."
                    is java.net.SocketTimeoutException -> "Connection timeout. Please try again."
                    is java.io.IOException -> "Network error occurred. Please try again."
                    else -> "Failed to load players: ${e.localizedMessage ?: "Unknown error"}"
                }
                _uiState.value = PlayersUiState.Error(errorMessage)
                flowOf(PagingData.empty())
            }
        }
        .cachedIn(viewModelScope)

    init {
        // Initialize with loading state
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
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Please check your network."
                    is java.net.SocketTimeoutException -> "Connection timeout. Please try again."
                    is java.io.IOException -> "Network error occurred. Please try again."
                    else -> "Failed to update player status: ${e.localizedMessage ?: "Unknown error"}"
                }
                _errorMessage.value = errorMessage
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun retryLoading() {
        _uiState.value = PlayersUiState.Loading
        // Trigger reload by updating sort option
        val currentSort = _sortOption.value
        _sortOption.value = currentSort
    }
}
