package com.classwork.vangtichai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.classwork.vangtichai.ui.theme.VangtiChaiTheme
import androidx.compose.ui.platform.LocalConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VangtiChaiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VangtiChaiApp()
                }
            }
        }
    }
}



@Composable
fun VangtiChaiApp() {
    // Remember amount upon screen rotation
    var amount by rememberSaveable { mutableStateOf("0") } // Ensure "0" is the default value
    val changeNotes: Map<Int, Int> = calculateChange(amount.toIntOrNull() ?: 0)

    // Get screen orientation using LocalConfiguration
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    // Main layout using BoxWithConstraints for responsive design
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Vangtichai",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Display the current Taka amount
            Text(
                text = "Taka: $amount",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (isLandscape) {
                // Landscape Mode: Two-column layout for change notes with the keypad on the right
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Change notes taking 50% of the width
                    Row(
                        modifier = Modifier.weight(0.5f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Split changeNotes into two lists for two columns
                        val halfSize = (changeNotes.size + 1) / 2
                        val leftColumnNotes = changeNotes.toList().take(halfSize)
                        val rightColumnNotes = changeNotes.toList().drop(halfSize)

                        // Left sub-column for change notes
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            leftColumnNotes.forEach { (note, count) ->
                                Text(
                                    text = "$note Taka: $count",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        // Right sub-column for change notes
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rightColumnNotes.forEach { (note, count) ->
                                Text(
                                    text = "$note Taka: $count",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }

                    // Keypad taking 50% of the width
                    Keypad(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight(),
                        onDigitClick = { digit ->
                            amount = when {
                                digit == "Clear" -> "0"
                                amount == "0" -> digit
                                else -> (amount + digit).take(9)
                            }
                        }
                    )
                }
            } else {
                // Portrait Mode: Original layout with the change notes on the left and keypad on the right
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Change notes taking 30% of the width
                    Column(
                        modifier = Modifier.weight(0.3f),
                        verticalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
                        changeNotes.forEach { (note, count) ->
                            Text(
                                text = "$note Taka: $count",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    // Keypad taking 70% of the width
                    Keypad(
                        modifier = Modifier.weight(0.7f),
                        onDigitClick = { digit ->
                            amount = when {
                                digit == "Clear" -> "0"
                                amount == "0" -> digit
                                else -> (amount + digit).take(9)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Keypad(modifier: Modifier = Modifier, onDigitClick: (String) -> Unit) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("0", "Clear")
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in keys) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (key in row) {
                    Button(
                        onClick = { onDigitClick(key) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = key,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
                        )
                    }
                }
            }
        }
    }
}




fun calculateChange(amount: Int): Map<Int, Int> {
    val notes = listOf(500, 100, 50, 20, 10, 5, 2, 1)
    val change = mutableMapOf<Int, Int>()

    var remainingAmount = amount
    for (note in notes) {
        val count = remainingAmount / note
        change[note] = count
        remainingAmount %= note
    }

    return change
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VangtiChaiAppPreview() {
    VangtiChaiTheme {
        VangtiChaiApp()
    }
}

