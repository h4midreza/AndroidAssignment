package ir.miare.androidcodechallenge.presentation.followed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.di.ResourceProvider
import ir.miare.androidcodechallenge.domain.model.Player
import ir.miare.androidcodechallenge.domain.usecase.GetFollowedPlayersUseCase
import ir.miare.androidcodechallenge.domain.usecase.ToggleFollowPlayerUseCase
import ir.miare.androidcodechallenge.util.ExceptionMapper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowedPlayersViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
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
                _errorMessage.value = ExceptionMapper.map(
                    e,
                    resourceProvider,
                    R.string.error_failed_to_update_player_status
                )
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
