# Blackjack Game

This project is a Blackjack card game developed as an assignment for a Java Programming course. Implemented using Java and the Swing GUI toolkit, it was designed to reinforce key concepts in object-oriented programming (OOP) and event-driven design.

---

## Project Goals

The main objective of this project was to implement a fully functional Blackjack game to deepen the understanding of Java GUI programming and OOP principles.

-   **Minimum Goal:** Implement a basic single-round game where a player competes against a dealer based on standard Blackjack rules.
-   **Additional Goals:**
    -   Implement a betting system with a player's money balance.
    -   Enable continuous gameplay with persistent win/loss records and a visual win rate display.
    -   (Considered but not implemented) A multiplayer mode for 2-3 players.

---

## Key Features

-   **Betting System:** Players must place a wager before each round begins. The player's money is updated based on the outcome.
-   **Win/Loss Tracking:** The game automatically records wins, losses, and ties, and calculates the player's win rate in real-time.
-   **Standard Blackjack Rules:** The game adheres to core Blackjack rules, including actions like Hit and Stay, score calculation (with Aces as 1 or 11), and bust detection.
-   **User-Friendly Interface:** The application provides visual feedback for game status, such as "Game Over" or "You Win!", and uses a `CardLayout` to manage different panels.

---

## Technologies and Concepts

-   **Language:** Java
-   **GUI Toolkit:** Swing
-   **Core Concepts:**
    -   **Object-Oriented Programming (OOP):** The project uses a modular class structure with a `Card` class to represent cards and a `BlackJack` class to manage all game logic and the GUI.
    -   **Event Handling:** `ActionListener` interfaces and anonymous inner classes are used to handle user input from buttons.
    -   **Data Structures:** `ArrayList` is used to manage the card deck and players' hands.

---

## Troubleshooting and Lessons Learned

Throughout the development process, several challenges were encountered and resolved, providing valuable experience in debugging and design.

-   **Win Rate Calculation Error:** Initially, the win rate would display as `NaN` or be inaccurate. This was fixed by adding specific exception handling for zero total games and by correctly implementing BUST and TIE rules, which were initially overlooked.
-   **Incorrect Betting Rule:** The original betting system allowed betting after the initial cards were dealt. This was corrected to align with standard Blackjack rules, where the bet is placed before the cards are drawn.
-   **Game State Reset:** A bug where the game state (money, win rate) would not reset after pressing the Quit button was identified and fixed.

---

This project was developed during the first semester of the second year (Spring 2024) for the Java Programming course at Dankook University.
