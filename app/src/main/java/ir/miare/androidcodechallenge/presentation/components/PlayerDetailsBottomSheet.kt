package ir.miare.androidcodechallenge.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.domain.model.Player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDetailsBottomSheet(
    player: Player,
    onDismiss: () -> Unit,
    onFollowClick: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            //Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.bottom_sheet_title_overview),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close_button_description)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Player information
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = player.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    DetailRow(label = stringResource(id = R.string.label_team), value = player.team.name)
                    Spacer(modifier = Modifier.height(4.dp))

                    DetailRow(label = stringResource(id = R.string.label_league), value = player.league)
                    Spacer(modifier = Modifier.height(4.dp))

                    DetailRow(label = stringResource(id = R.string.label_team_rank), value = player.team.rank.toString())
                    Spacer(modifier = Modifier.height(4.dp))

                    DetailRow(label = stringResource(id = R.string.label_total_goals), value = player.totalGoal.toString())
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Follow button
            Button(
                onClick = {
                    onFollowClick()
                    // onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (player.isFollowed) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (player.isFollowed) stringResource(id = R.string.unfollow_player) else stringResource(id = R.string.follow_player)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
