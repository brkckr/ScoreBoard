package com.brkckr.scoreboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // manage scores and digit count
                var homeScore by remember { mutableIntStateOf(0) }
                var awayScore by remember { mutableIntStateOf(0) }
                var digitCount by remember { mutableStateOf(DigitCount.SINGLE) }
                Column {
                    // display scoreboard with white on and purple off leds
                    ScoreBoard(
                        homeScore = homeScore,
                        awayScore = awayScore,
                        teamNames = Pair("Galatasaray", "Galatasaray"),
                        digitCount = digitCount
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // buttons to test scores and digit count
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            homeScore = (homeScore + 1).coerceIn(0, digitCount.maxScore)
                        }) {
                            Text("Home +1")
                        }
                        Button(onClick = {
                            awayScore = (awayScore + 1).coerceIn(0, digitCount.maxScore)
                        }) {
                            Text("Away +1")
                        }
                        Button(onClick = {
                            digitCount = when (digitCount) {
                                DigitCount.SINGLE -> DigitCount.DOUBLE
                                DigitCount.DOUBLE -> DigitCount.TRIPLE
                                DigitCount.TRIPLE -> DigitCount.SINGLE
                            }
                        }) {
                            Text("Toggle Digits")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    ScoreBoard(
                        homeScore = 77,
                        awayScore = 66,
                        teamNames = Pair("Real Madrid", "Barcelona"),
                        digitCount = DigitCount.DOUBLE,
                        ledOffColor = Color.White,
                        ledOnColor = Color.Magenta
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ScoreBoard(
                        homeScore = 122,
                        awayScore = 111,
                        teamNames = Pair("Los Angeles Lakers", "Boston Celtics"),
                        digitCount = DigitCount.TRIPLE
                    )

                }
            }
        }
    }
}

@Composable
fun ScoreBoard(
    homeScore: Int,
    awayScore: Int,
    teamNames: Pair<String, String>,
    digitCount: DigitCount = DigitCount.DOUBLE,
    ledOnColor: Color = Color.Red, // default red for led on
    ledOffColor: Color = Color.DarkGray, // default dark gray for led off
    modifier: Modifier = Modifier
) {
    // format scores based on digit count
    val homeScoreStr = homeScore.coerceIn(0, digitCount.maxScore)
        .toString()
        .padStart(digitCount.value, '0')
    val awayScoreStr = awayScore.coerceIn(0, digitCount.maxScore)
        .toString()
        .padStart(digitCount.value, '0')

    Column(
        modifier = modifier
            .background(Color.Black) // black background for led effect
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // display team names
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = teamNames.first,
                color = Color.White,
                fontSize = 20.sp
            )
            Text(
                text = teamNames.second,
                color = Color.White,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // display scores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ScoreDisplay(
                scoreStr = homeScoreStr,
                digitCount = digitCount,
                ledOnColor = ledOnColor,
                ledOffColor = ledOffColor
            )
            ScoreDisplay(
                scoreStr = awayScoreStr,
                digitCount = digitCount,
                ledOnColor = ledOnColor,
                ledOffColor = ledOffColor
            )
        }
    }
}

@Composable
fun ScoreDisplay(
    scoreStr: String,
    digitCount: DigitCount,
    ledOnColor: Color,
    ledOffColor: Color
) {
    // display digits based on digit count
    Row(
        horizontalArrangement = Arrangement.spacedBy(LedConfig.digitSpacing)
    ) {
        for (i in 0 until digitCount.value) {
            DigitDisplay(
                pattern = DigitPatterns.patterns[scoreStr[i]] ?: DigitPatterns.patterns['0']!!,
                ledOnColor = ledOnColor,
                ledOffColor = ledOffColor
            )
        }
    }
}

@Composable
fun DigitDisplay(
    pattern: List<String>,
    ledOnColor: Color,
    ledOffColor: Color
) {
    // get density to convert dp to px
    val density = LocalDensity.current
    // create glow effect for leds
    val onGradient = ShaderBrush(
        RadialGradientShader(
            colors = listOf(ledOnColor, ledOnColor.copy(alpha = 0.3f)),
            center = with(density) { Offset(5.dp.toPx(), 5.dp.toPx()) },
            radius = with(density) { 5.dp.toPx() }
        )
    )
    val offGradient = ShaderBrush(
        RadialGradientShader(
            colors = listOf(ledOffColor, ledOffColor.copy(alpha = 0.1f)),
            center = with(density) { Offset(5.dp.toPx(), 5.dp.toPx()) },
            radius = with(density) { 5.dp.toPx() }
        )
    )

    Column {
        pattern.forEach { row ->
            Row {
                row.forEach { pixel ->
                    // animate led state change
                    Crossfade(
                        targetState = pixel,
                        animationSpec = tween(durationMillis = 300), // 300ms animation
                        modifier = Modifier
                            .size(LedConfig.ledSize)
                            .padding(LedConfig.ledPadding)
                            .clip(CircleShape)
                    ) { pixelState ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .drawBehind {
                                    // draw glowing led
                                    drawCircle(
                                        brush = if (pixelState == '1') onGradient else offGradient,
                                        radius = with(density) { 5.dp.toPx() }
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScoreBoardPreview() {
    ScoreBoard(
        homeScore = 123,
        awayScore = 45,
        teamNames = Pair("Fenerbahçe", "Galatasaray"),
        digitCount = DigitCount.TRIPLE,
        ledOnColor = Color.White, // white for led on
        ledOffColor = Color.Magenta // purple for led off
    )
}

// configuration for led display
object LedConfig {
    val ledSize = 10.dp // size of each led
    val ledPadding = 1.dp // padding between leds
    val digitSpacing = 8.dp // spacing between digits
}

// enum for digit count
enum class DigitCount(val value: Int, val maxScore: Int) {
    SINGLE(1, 9),
    DOUBLE(2, 99),
    TRIPLE(3, 999)
}