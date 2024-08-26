# Chess Game with GUI

## Project Overview

This is a Java-based chess game featuring a graphical user interface (GUI) that allows two players to play a standard game of chess. The game supports all the standard rules of chess, including pawn promotion, en passant, and castling. The GUI is designed using Swing, with the chessboard and pieces displayed on a `JFrame`.

## Features

- **Interactive GUI**: The game is played on a GUI that displays the chessboard and pieces.
- **Pawn Promotion**: When a pawn reaches the opposite end of the board, the player is prompted to promote it to a Rook, Knight, Bishop, or Queen. The options are presented as clickable icons.
- **Game Logic**: The game enforces all standard chess rules, including check, checkmate, and stalemate.
- **Move Validation**: Only legal moves are allowed.
- **Immediate Board Update**: The board updates immediately after a pawn promotion, ensuring smooth gameplay.

## Setup Instructions

To run this project locally:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/DalfoDev/chess.git
   cd chess-game
   ```
2. **Compile the Code**:
   Make sure you have Java installed on your machine. Compile the code using:
   ```bash
   javac -d bin src/*.java
   ```
3. **Run the Game**:
   After compiling, run the game using:
   ```bash
   java -cp bin Chessboard
   ```

## Usage

- **Starting the Game**: Launch the game by running the `Chessboard` class.
- **Playing**: Use the mouse to click and drag pieces to make moves. The game will only allow legal moves.
- **Pawn Promotion**: When a pawn reaches the last rank, a GUI will appear with icons for each piece (Queen, Rook, Bishop, Knight). Click on the desired piece to promote the pawn.

## Implementation Details

The project is organized into several key classes:

- **Chessboard.java**: This class handles the main game loop and the GUI components.
- **ChessPieces.java**: This class represents the different pieces on the chessboard and their behaviors.
- **BoardState.java**: This class manages the state of the chessboard, tracking piece positions and handling move validation.
- **ChessPieceFactory.java**: Implements the factory design pattern to create instances of different chess pieces.
- **PawnPromotionDialog.java**: Manages the user interface for handling pawn promotions, allowing players to select the piece to which the pawn will be promoted.
- **Pieces.java**: Serves as an abstract class that defines the common behavior and attributes for all chess pieces.

### Naming Conventions

- Chess piece image files are named using the format 'w' or 'b' followed by the first letter of the piece name in lowercase (e.g., 'wq.png' for a white queen, 'bp.png' for a black pawn).

### Pawn Promotion

- The promotion feature is implemented using a popup window that appears when a pawn reaches the last rank. This window shows buttons with icons representing the possible promotion pieces.

### Castling

Castling is a special move in chess that involves the king and one of the rooks. It's the only move in chess that allows a player to move two pieces at once. The move serves two purposes: it helps to protect the king by moving it to a safer position, and it helps to develop the rook by bringing it into the game.

In this game:

- **Conditions for Castling**:
  - Neither the king nor the chosen rook must have moved previously in the game.
  - There must be no pieces between the king and the rook.
  - The king must not be in check, nor can the king pass through or land on a square that is attacked by an opponent's piece.
- **How to Perform Castling**:
  - To castle, move the king two squares towards the rook you wish to castle with. The game will automatically move the rook to the square next to the king.
- **Implementation Details**:
  - The castling logic is implemented in the move validation function. The game checks if all conditions for castling are met before allowing the move.
