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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

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
    val changeNotes = calculateChange(amount.toIntOrNull() ?: 0)

    // Enable Scrolling
    val scrollState = rememberScrollState()

    // Column with background color filling the whole screen and scrollability
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Set background color for the entire screen
            .padding(16.dp)
            .verticalScroll(scrollState), // Enable vertical scrolling
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Banner at the top with app title and background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary) // Background color for the title
                .padding(8.dp) // Padding inside the Box for spacing
        ) {
            Text(
                text = "Vangtichai",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                color = MaterialTheme.colorScheme.onPrimary, // Text color contrasting the background
                modifier = Modifier.align(Alignment.TopStart)
            )
        }

        // Display the current Taka amount
        Text(
            text = "Taka: $amount",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Space between the Taka display and the rest

        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically // Align components to the top
        ) {
            // Display with results on the left
            Column(
                modifier = Modifier.weight(0.3f),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Display the result for Taka notes
                changeNotes.forEach { (note, count) ->
                    Text(text = "$note Taka: $count", style = MaterialTheme.typography.bodyLarge)
                }
            }

            // Numeric keypad on the right
            Keypad(
                modifier = Modifier.weight(0.7f),
                onDigitClick = { digit ->
                    amount = when {
                        digit == "Clear" -> "0" // Reset to "0" when cleared
                        amount == "0" -> digit // Replace "0" with the first digit
                        else -> (amount + digit).take(9) // Limit to 9 digits
                    }
                }
            )
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in keys) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for ((index, key) in row.withIndex()) {
                    Button(
                        onClick = { onDigitClick(key) },
                        modifier = if (row == keys.last() && key == "Clear") {
                            Modifier
                                .weight(2f) // Double the width for the "Clear" button
                                .height(64.dp) // Fixed height for all buttons
                                .padding(4.dp)
                        } else {
                            Modifier
                                .weight(1f) // Normal width for other buttons
                                .height(64.dp) // Fixed height for all buttons
                                .padding(4.dp)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary, // Background color
                            contentColor = MaterialTheme.colorScheme.onPrimary // Text color
                        ),
                        shape = MaterialTheme.shapes.medium // Rounded corners
                    ) {
                        Text(
                            text = key,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp) // Custom text style
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
