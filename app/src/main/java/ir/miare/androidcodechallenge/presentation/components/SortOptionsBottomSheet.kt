package ir.miare.androidcodechallenge.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.util.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortOptionsBottomSheet(
    currentSortOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.sort_players),
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

            // Sort options
            val sortOptions = listOf(
                SortOption.NAME_ASC to stringResource(id = R.string.sort_name_asc),
                SortOption.NAME_DESC to stringResource(id = R.string.sort_name_desc),
                SortOption.GOALS_ASC to stringResource(id = R.string.sort_goals_asc),
                SortOption.GOALS_DESC to stringResource(id = R.string.sort_goals_desc),
                SortOption.TEAM_RANK_ASC to stringResource(id = R.string.sort_rank_asc),
                SortOption.TEAM_RANK_DESC to stringResource(id = R.string.sort_rank_desc)
            )

            sortOptions.forEach { (option, displayName) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = currentSortOption == option,
                            onClick = { onSortOptionSelected(option) }
                        )
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentSortOption == option,
                        onClick = { onSortOptionSelected(option) }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
