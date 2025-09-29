package ir.miare.androidcodechallenge.presentation.followed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.usecase.GetFollowedPlayersUseCase
import ir.miare.androidcodechallenge.domain.usecase.ToggleFollowPlayerUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowedPlayersViewModel @Inject constructor(
    private val getFollowedPlayersUseCase: GetFollowedPlayersUseCase,
    private val toggleFollowPlayerUseCase: ToggleFollowPlayerUseCase,
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    val followedPlayers: StateFlow<List<Player>> = getFollowedPlayersUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFollowPlayer(playerId: String) {
        viewModelScope.launch {
            try {
                toggleFollowPlayerUseCase(playerId)
            } catch (e: Exception) {
                _errorMessage.value = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Please check your network."
                    is java.net.SocketTimeoutException -> "Connection timeout. Please try again."
                    is java.io.IOException -> "Network error occurred. Please try again."
                    else -> "Failed to update player status: ${e.localizedMessage ?: "Unknown error"}"
                }
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
