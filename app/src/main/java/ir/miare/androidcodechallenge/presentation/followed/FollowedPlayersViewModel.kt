package ir.miare.androidcodechallenge.presentation.followed

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.usecase.GetFollowedPlayersUseCase
import ir.miare.androidcodechallenge.domain.usecase.ToggleFollowPlayerUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FollowedPlayersViewModel @Inject constructor(
    private val application: Application,
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
                    is UnknownHostException -> application.getString(R.string.error_no_internet)
                    is SocketTimeoutException -> application.getString(R.string.error_connection_timeout)
                    is IOException -> application.getString(R.string.error_network_error)
                    else -> application.getString(
                        R.string.error_failed_to_update_player_status,
                        e.localizedMessage ?: application.getString(R.string.error_unknown)
                    )
                }
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
