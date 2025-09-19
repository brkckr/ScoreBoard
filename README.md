![Scoreboard GIF](https://github.com/brkckr/ScoreBoard/blob/master/ss/ss.gif)

# LED matrix style Scoreboard

An Android app built with Jetpack Compose that displays a scoreboard using a 5x7 LED matrix style. It supports single, double, or triple digit scores with customizable LED colors and smooth animations.

## Features
- **5x7 LED Matrix**: Displays digits (0-9) in a glowing LED matrix.
- **Digit Count**: Choose single (0-9), double (0-99), or triple (0-999) digits.
- **Animations**: 300ms color transitions for score updates.
- **Custom LED Colors**: Default red(on) and dark gray(off), fully customizable.
- **Toggle Button**: Switch between single, double, and triple digit modes.
- **Team Names**: Customizable team names (e.g., "Galatasaray" vs. "Fenerbahçe").

## Requirements
- Android Studio (latest stable version)
- Kotlin 1.9.0 or higher
- Jetpack Compose
- Minimum SDK: 21

## Setup
1. Clone the repository from https://github.com/brkckr/ScoreBoard.git
2. Open the project in Android Studio.
3. Sync with Gradle (File > Sync Project with Gradle Files).
4. Build and run on an emulator or Android device.

## Kullanım (Usage)
- **Scoreboard**: Shows scores for two teams with LED matrix digits.
- **Buttons**:
  - "Home +1": Increase home team score.
  - "Away +1": Increase away team score.
  - "Toggle Digits": Cycle between single (0-9), double (0-99), and triple (0-999) digits.
- **Customization**: Modify team names, digit count, or LED colors in code:
  ```kotlin
  ScoreBoard(
      homeScore = 123,
      awayScore = 45,
      teamNames = Pair("Team A", "Team B"),
      digitCount = DigitCount.TRIPLE,
      ledOnColor = Color.Green, // custom on color
      ledOffColor = Color.Blue  // custom off color
  )

