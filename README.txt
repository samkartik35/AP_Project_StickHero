# Stick Hero Game

Rishima Chadha 2022404
Kartik Kumar 2022239

##Rules
1.Stick Hero is a simple JavaFX-based game where the player needs to extend a stick to cross a gap between two platforms which are the black rectangles.
2.The red dots represent the cherries and have a 1000 points each.
3.The black dots are obstacles and touching one will make the stick hero fall ending the game.
4.Crossing each platform earns the player 100 points.
5.The game further has 3 functionalities on ending:
 i.Restart: The player starts the game all over again and his cherry and point records and cleared.
 ii.Revive: If the player has one cherry, he can revive the character using those 1000 points of a cherry. His cherry points are then deducted by a 1000.
 iii.Exit: The player can simply choose to end the game and exit.

## Prerequisites

- Java 8 or later
- JavaFX

## How to Run

1. Compile and run the `Main` class located in the `com.example.application` package.

```bash
javac com/example/application/Main.java
java com.example.application.Main


## Gameplay Controls

- Mouse Click: Extend the stick length.
- Enter Key: Finalize the stick length and attempt to cross the gap.
- Space Key: Flips the character to avoid the black dots and collect cherries.


## Files and Structure
- Main.java: The main class to start the Stick Hero game.
- StickHero.java: The Stick Hero game implementation.
- StickHeroTest.java: JUnit test class for testing Stick Hero functionality.

## Extra features
- The black dots which act as hurdles are an added feature and increase the difficulty of the game.
- The application also takes the name of the user in the start as a more welcoming segue into the game.