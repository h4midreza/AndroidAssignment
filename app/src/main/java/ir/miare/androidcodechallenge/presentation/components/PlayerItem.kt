package ir.miare.androidcodechallenge.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.miare.androidcodechallenge.domain.model.Player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerItem(
    player: Player,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFollowLoading: Boolean = false
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { showBottomSheet = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = player.team.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row {
                    Text(
                        text = "Goals: ${player.totalGoal}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Team Rank: ${player.team.rank}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (isFollowLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(
                    onClick = onFollowClick
                ) {
                    Icon(
                        imageVector = if (player.isFollowed) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (player.isFollowed) "Unfollow" else "Follow",
                        tint = if (player.isFollowed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        PlayerDetailsBottomSheet(
            player = player,
            onDismiss = { showBottomSheet = false },
            onFollowClick = onFollowClick
        )
    }
}
