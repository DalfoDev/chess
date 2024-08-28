import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ChessPieces {

    /**
     * The {@code Queen} class represents a Queen piece in a chess game.
     * It extends the {@link Pieces} class and includes specific behavior
     * for the Queen piece, such as valid moves and drawing the piece.
     */
    public static class Queen extends Pieces {
        private BoardState boardState;

        /**
         * Constructs a {@code Queen} piece with the specified position, color, and
         * board state.
         *
         * @param row        The row position of the Queen on the chessboard.
         * @param column     The column position of the Queen on the chessboard.
         * @param color      The color of the Queen piece ("w" for white, "b" for
         *                   black).
         * @param boardState The current state of the chessboard.
         */
        public Queen(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
        }

        /**
         * Draws the Queen piece on the chessboard.
         *
         * @param g        The {@code Graphics} context to use for drawing the piece.
         * @param x        The x-coordinate where the piece should be drawn.
         * @param y        The y-coordinate where the piece should be drawn.
         * @param tileSize The size of the tile on which the piece is drawn.
         */
        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        /**
         * Determines if the Queen can move to the specified position.
         * The Queen can move horizontally, vertically, or diagonally.
         *
         * @param newRow    The target row position.
         * @param newColumn The target column position.
         * @return {@code true} if the move is valid, {@code false} otherwise.
         */
        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            return (newRow == row || newColumn == column || Math.abs(newRow - row) == Math.abs(newColumn - column));
        }

        /**
         * Returns a list of all legal moves for the Queen from its current position.
         * The legal moves include all possible vertical, horizontal, and diagonal
         * moves.
         *
         * @return A list of legal moves in the format "e2", "d4", etc.
         */
        @Override
        public List<String> getLegalMoves() {
            List<String> legalMoves = new ArrayList<>();

            // Add all possible vertical, horizontal, and diagonal moves
            addMovesInDirection(legalMoves, 1, 0); // Down
            addMovesInDirection(legalMoves, -1, 0); // Up
            addMovesInDirection(legalMoves, 0, 1); // Right
            addMovesInDirection(legalMoves, 0, -1); // Left
            addMovesInDirection(legalMoves, 1, 1); // Down-right
            addMovesInDirection(legalMoves, 1, -1); // Down-left
            addMovesInDirection(legalMoves, -1, 1); // Up-right
            addMovesInDirection(legalMoves, -1, -1); // Up-left

            return legalMoves;
        }

        /**
         * Adds legal moves in a specific direction to the provided list.
         * This method iterates through the board in the specified direction
         * until it hits the edge of the board, a piece of the same color, or an
         * opponent's piece.
         *
         * @param legalMoves The list to which the legal moves are added.
         * @param rowDelta   The row increment (positive, negative, or zero).
         * @param colDelta   The column increment (positive, negative, or zero).
         */
        private void addMovesInDirection(List<String> legalMoves, int rowDelta, int colDelta) {
            int currentRow = row + rowDelta;
            int currentColumn = column + colDelta;

            while (currentRow >= 0 && currentRow < 8 && currentColumn >= 0 && currentColumn < 8) {
                if (!boardState.isPositionOccupiedBySameColor(currentRow, currentColumn, this.color)) {
                    legalMoves.add(convertToPosition(currentRow, currentColumn));
                    if (boardState.getPieceAt(currentRow, currentColumn) != null) {
                        break; // Stop if another piece is in the way
                    }
                } else {
                    break; // Stop if a piece of the same color is in the way
                }
                currentRow += rowDelta;
                currentColumn += colDelta;
            }
        }

        /**
         * Returns the initial letter representing the Queen piece.
         *
         * @return 'q' for Queen.
         */
        @Override
        protected char getPieceInitial() {
            return 'q'; // 'q' for queen
        }
    }

    /**
     * The {@code Pawn} class represents a Pawn piece in a chess game.
     * It extends the {@link Pieces} class and includes specific behavior
     * for the Pawn piece, such as valid moves and drawing the piece.
     */
    public static class Pawn extends Pieces {
        private BoardState boardState;

        /**
         * Constructs a {@code Pawn} piece with the specified position, color, and board
         * state.
         *
         * @param row        The row position of the Pawn on the chessboard.
         * @param column     The column position of the Pawn on the chessboard.
         * @param color      The color of the Pawn piece ("w" for white, "b" for black).
         * @param boardState The current state of the chessboard.
         */
        public Pawn(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
        }

        /**
         * Draws the Pawn piece on the chessboard.
         *
         * @param g        The {@code Graphics} context to use for drawing the piece.
         * @param x        The x-coordinate where the piece should be drawn.
         * @param y        The y-coordinate where the piece should be drawn.
         * @param tileSize The size of the tile on which the piece is drawn.
         */
        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        /**
         * Determines if the Pawn can move to the specified position.
         * The Pawn has unique movement rules: it moves forward one square,
         * but can move forward two squares from its initial position,
         * and captures diagonally.
         *
         * @param newRow    The target row position.
         * @param newColumn The target column position.
         * @return {@code true} if the move is valid, {@code false} otherwise.
         */
        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            int rowDiff = newRow - row;
            int colDiff = Math.abs(newColumn - column);

            if (color.equals("white")) {
                // Standard single forward move
                if (rowDiff == -1 && colDiff == 0 && boardState.getPieceAt(newRow, newColumn) == null) {
                    return true;
                }
                // Initial double move
                if (row == 6 && rowDiff == -2 && colDiff == 0 && boardState.getPieceAt(newRow, newColumn) == null
                        && boardState.getPieceAt(row - 1, column) == null) {
                    return true;
                }
                // Diagonal capture
                if (rowDiff == -1 && colDiff == 1 && boardState.getPieceAt(newRow, newColumn) != null
                        && !boardState.isPositionOccupiedBySameColor(newRow, newColumn, color)) {
                    return true;
                }
            } else {
                // Standard single forward move
                if (rowDiff == 1 && colDiff == 0 && boardState.getPieceAt(newRow, newColumn) == null) {
                    return true;
                }
                // Initial double move
                if (row == 1 && rowDiff == 2 && colDiff == 0 && boardState.getPieceAt(newRow, newColumn) == null
                        && boardState.getPieceAt(row + 1, column) == null) {
                    return true;
                }
                // Diagonal capture
                if (rowDiff == 1 && colDiff == 1 && boardState.getPieceAt(newRow, newColumn) != null
                        && !boardState.isPositionOccupiedBySameColor(newRow, newColumn, color)) {
                    return true;
                }
            }

            return false;
        }

        /**
         * Returns a list of all legal moves for the Pawn from its current position.
         * The legal moves include forward moves, initial double moves, and diagonal
         * captures.
         *
         * @return A list of legal moves in the format "e2", "d4", etc.
         */
        @Override
        public List<String> getLegalMoves() {
            List<String> legalMoves = new ArrayList<>();

            int direction = color.equals("white") ? -1 : 1;
            int startRow = color.equals("white") ? 6 : 1;

            // Single move forward
            if (isValidMove(row + direction, column)) {
                legalMoves.add(convertToPosition(row + direction, column));
            }

            // Double move forward from start position
            if (row == startRow && isValidMove(row + 2 * direction, column)) {
                legalMoves.add(convertToPosition(row + 2 * direction, column));
            }

            // Capture moves
            if (isValidMove(row + direction, column - 1)) {
                legalMoves.add(convertToPosition(row + direction, column - 1));
            }
            if (isValidMove(row + direction, column + 1)) {
                legalMoves.add(convertToPosition(row + direction, column + 1));
            }

            return legalMoves;
        }

        /**
         * Returns the initial letter representing the Pawn piece.
         *
         * @return 'p' for Pawn.
         */
        @Override
        protected char getPieceInitial() {
            return 'p'; // 'p' for pawn
        }
    }

    /**
     * The {@code Rook} class represents a Rook piece in a chess game.
     * It extends the {@link Pieces} class and includes specific behavior
     * for the Rook piece, such as valid moves, castling logic, and drawing the
     * piece.
     */
    public static class Rook extends Pieces {
        private BoardState boardState;
        private boolean hasMoved;

        /**
         * Constructs a {@code Rook} piece with the specified position, color, and board
         * state.
         *
         * @param row        The row position of the Rook on the chessboard.
         * @param column     The column position of the Rook on the chessboard.
         * @param color      The color of the Rook piece ("w" for white, "b" for black).
         * @param boardState The current state of the chessboard.
         */
        public Rook(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
            this.hasMoved = false; // Initialize hasMoved to false since the Rook hasn't moved yet.
        }

        /**
         * Checks if the Rook has moved from its original position.
         *
         * @return {@code true} if the Rook has moved, {@code false} otherwise.
         */
        public boolean hasMoved() {
            return this.hasMoved;
        }

        /**
         * Sets the Rook's state to indicate that it has moved.
         * This method is used to prevent the Rook from participating in castling
         * after it has moved from its initial position.
         */
        public void movedPiece() {
            this.hasMoved = true;
        }

        /**
         * Draws the Rook piece on the chessboard.
         *
         * @param g        The {@code Graphics} context to use for drawing the piece.
         * @param x        The x-coordinate where the piece should be drawn.
         * @param y        The y-coordinate where the piece should be drawn.
         * @param tileSize The size of the tile on which the piece is drawn.
         */
        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        /**
         * Determines if the Rook can move to the specified position.
         * The Rook can move horizontally or vertically any number of squares.
         *
         * @param newRow    The target row position.
         * @param newColumn The target column position.
         * @return {@code true} if the move is valid, {@code false} otherwise.
         */
        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            return newRow == row || newColumn == column;
        }

        /**
         * Returns a list of all legal moves for the Rook from its current position.
         * The legal moves include all possible vertical and horizontal moves.
         *
         * @return A list of legal moves in the format "e2", "d4", etc.
         */
        @Override
        public List<String> getLegalMoves() {
            List<String> legalMoves = new ArrayList<>();

            // Add all possible vertical and horizontal moves
            addMovesInDirection(legalMoves, 1, 0); // Down
            addMovesInDirection(legalMoves, -1, 0); // Up
            addMovesInDirection(legalMoves, 0, 1); // Right
            addMovesInDirection(legalMoves, 0, -1); // Left

            return legalMoves;
        }

        /**
         * Adds legal moves in a specific direction to the provided list.
         * This method iterates through the board in the specified direction
         * until it hits the edge of the board, a piece of the same color, or an
         * opponent's piece.
         *
         * @param legalMoves The list to which the legal moves are added.
         * @param rowDelta   The row increment (positive, negative, or zero).
         * @param colDelta   The column increment (positive, negative, or zero).
         */
        private void addMovesInDirection(List<String> legalMoves, int rowDelta, int colDelta) {
            int currentRow = row + rowDelta;
            int currentColumn = column + colDelta;

            while (currentRow >= 0 && currentRow < 8 && currentColumn >= 0 && currentColumn < 8) {
                if (!boardState.isPositionOccupiedBySameColor(currentRow, currentColumn, this.color)) {
                    legalMoves.add(convertToPosition(currentRow, currentColumn));
                    if (boardState.getPieceAt(currentRow, currentColumn) != null) {
                        break; // Stop if another piece is in the way
                    }
                } else {
                    break; // Stop if a piece of the same color is in the way
                }
                currentRow += rowDelta;
                currentColumn += colDelta;
            }
        }

        /**
         * Returns the initial letter representing the Rook piece.
         *
         * @return 'r' for Rook.
         */
        @Override
        protected char getPieceInitial() {
            return 'r'; // 'r' for rook
        }
    }

    /**
     * The {@code Knight} class represents a Knight piece in a chess game.
     * It extends the {@link Pieces} class and includes specific behavior
     * for the Knight piece, such as valid moves and drawing the piece.
     */
    public static class Knight extends Pieces {
        private BoardState boardState;

        /**
         * Constructs a {@code Knight} piece with the specified position, color, and
         * board state.
         *
         * @param row        The row position of the Knight on the chessboard.
         * @param column     The column position of the Knight on the chessboard.
         * @param color      The color of the Knight piece ("w" for white, "b" for
         *                   black).
         * @param boardState The current state of the chessboard.
         */
        public Knight(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
        }

        /**
         * Draws the Knight piece on the chessboard.
         *
         * @param g        The {@code Graphics} context to use for drawing the piece.
         * @param x        The x-coordinate where the piece should be drawn.
         * @param y        The y-coordinate where the piece should be drawn.
         * @param tileSize The size of the tile on which the piece is drawn.
         */
        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        /**
         * Determines if the Knight can move to the specified position.
         * The Knight moves in an L-shape: two squares in one direction and one square
         * perpendicular.
         *
         * @param newRow    The target row position.
         * @param newColumn The target column position.
         * @return {@code true} if the move is valid, {@code false} otherwise.
         */
        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            int rowDiff = Math.abs(newRow - row);
            int colDiff = Math.abs(newColumn - column);
            return rowDiff * colDiff == 2;
        }

        /**
         * Returns a list of all legal moves for the Knight from its current position.
         * The legal moves include all possible L-shaped moves.
         *
         * @return A list of legal moves in the format "e2", "d4", etc.
         */
        @Override
        public List<String> getLegalMoves() {
            List<String> legalMoves = new ArrayList<>();
            int[][] moves = {
                    { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 },
                    { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }
            };

            for (int[] move : moves) {
                int newRow = row + move[0];
                int newColumn = column + move[1];
                if (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                    if (!boardState.isPositionOccupiedBySameColor(newRow, newColumn, this.color)) {
                        legalMoves.add(convertToPosition(newRow, newColumn));
                    }
                }
            }

            return legalMoves;
        }

        /**
         * Returns the initial letter representing the Knight piece.
         *
         * @return 'n' for Knight.
         */
        @Override
        protected char getPieceInitial() {
            return 'n'; // 'n' for knight
        }
    }

    /**
     * The {@code Bishop} class represents a Bishop piece in a chess game.
     * It extends the {@link Pieces} class and includes specific behavior
     * for the Bishop piece, such as valid moves and drawing the piece.
     */
    public static class Bishop extends Pieces {
        private BoardState boardState;

        /**
         * Constructs a {@code Bishop} piece with the specified position, color, and
         * board state.
         *
         * @param row        The row position of the Bishop on the chessboard.
         * @param column     The column position of the Bishop on the chessboard.
         * @param color      The color of the Bishop piece ("w" for white, "b" for
         *                   black).
         * @param boardState The current state of the chessboard.
         */
        public Bishop(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
        }

        /**
         * Draws the Bishop piece on the chessboard.
         *
         * @param g        The {@code Graphics} context to use for drawing the piece.
         * @param x        The x-coordinate where the piece should be drawn.
         * @param y        The y-coordinate where the piece should be drawn.
         * @param tileSize The size of the tile on which the piece is drawn.
         */
        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        /**
         * Determines if the Bishop can move to the specified position.
         * The Bishop moves diagonally any number of squares.
         *
         * @param newRow    The target row position.
         * @param newColumn The target column position.
         * @return {@code true} if the move is valid, {@code false} otherwise.
         */
        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            return Math.abs(newRow - row) == Math.abs(newColumn - column);
        }

        /**
         * Returns a list of all legal moves for the Bishop from its current position.
         * The legal moves include all possible diagonal moves.
         *
         * @return A list of legal moves in the format "e2", "d4", etc.
         */
        @Override
        public List<String> getLegalMoves() {
            List<String> legalMoves = new ArrayList<>();

            // Add all possible diagonal moves
            addMovesInDirection(legalMoves, 1, 1); // Down-right
            addMovesInDirection(legalMoves, 1, -1); // Down-left
            addMovesInDirection(legalMoves, -1, 1); // Up-right
            addMovesInDirection(legalMoves, -1, -1); // Up-left

            return legalMoves;
        }

        /**
         * Adds legal moves in a specific direction to the provided list.
         * This method iterates through the board in the specified direction
         * until it hits the edge of the board, a piece of the same color, or an
         * opponent's piece.
         *
         * @param legalMoves The list to which the legal moves are added.
         * @param rowDelta   The row increment (positive, negative, or zero).
         * @param colDelta   The column increment (positive, negative, or zero).
         */
        private void addMovesInDirection(List<String> legalMoves, int rowDelta, int colDelta) {
            int currentRow = row + rowDelta;
            int currentColumn = column + colDelta;

            while (currentRow >= 0 && currentRow < 8 && currentColumn >= 0 && currentColumn < 8) {
                if (!boardState.isPositionOccupiedBySameColor(currentRow, currentColumn, this.color)) {
                    legalMoves.add(convertToPosition(currentRow, currentColumn));
                    if (boardState.getPieceAt(currentRow, currentColumn) != null) {
                        break; // Stop if another piece is in the way
                    }
                } else {
                    break; // Stop if a piece of the same color is in the way
                }
                currentRow += rowDelta;
                currentColumn += colDelta;
            }
        }

        /**
         * Returns the initial letter representing the Bishop piece.
         *
         * @return 'b' for Bishop.
         */
        @Override
        protected char getPieceInitial() {
            return 'b'; // 'b' for bishop
        }
    }

    /**
     * The {@code King} class represents a King piece in a chess game.
     * It extends the {@link Pieces} class and includes specific behavior
     * for the King piece, such as valid moves, castling logic, and drawing the
     * piece.
     */
    public static class King extends Pieces {
        private BoardState boardState;
        private boolean hasMoved;

        /**
         * Constructs a {@code King} piece with the specified position, color, and board
         * state.
         *
         * @param row        The row position of the King on the chessboard.
         * @param column     The column position of the King on the chessboard.
         * @param color      The color of the King piece ("w" for white, "b" for black).
         * @param boardState The current state of the chessboard.
         */
        public King(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
            this.hasMoved = false;
        }

        /**
         * Checks if the King has moved from its original position.
         *
         * @return {@code true} if the King has moved, {@code false} otherwise.
         */
        public boolean hasMoved() {
            return this.hasMoved;
        }

        /**
         * Sets the King's state to indicate that it has moved.
         * This method is used to prevent the King from participating in castling
         * after it has moved from its initial position.
         */
        public void movedPiece() {
            this.hasMoved = true;
        }

        /**
         * Draws the King piece on the chessboard.
         *
         * @param g        The {@code Graphics} context to use for drawing the piece.
         * @param x        The x-coordinate where the piece should be drawn.
         * @param y        The y-coordinate where the piece should be drawn.
         * @param tileSize The size of the tile on which the piece is drawn.
         */
        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        /**
         * Determines if the King can move to the specified position.
         * The King can move one square in any direction.
         *
         * @param newRow    The target row position.
         * @param newColumn The target column position.
         * @return {@code true} if the move is valid, {@code false} otherwise.
         */
        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            int rowDiff = Math.abs(newRow - row);
            int colDiff = Math.abs(newColumn - column);
            return (rowDiff <= 1 && colDiff <= 1);
        }

        /**
         * Returns a list of all legal moves for the King from its current position.
         * The legal moves include one square in any direction and castling moves if the
         * King has not moved yet.
         *
         * @return A list of legal moves in the format "e2", "d4", etc.
         */
        @Override
        public List<String> getLegalMoves() {
            List<String> legalMoves = new ArrayList<>();
            int[][] moves = {
                    { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },
                    { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }
            };

            for (int[] move : moves) {
                int newRow = row + move[0];
                int newColumn = column + move[1];
                if (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                    if (!boardState.isPositionOccupiedBySameColor(newRow, newColumn, this.color)) {
                        legalMoves.add(convertToPosition(newRow, newColumn));
                    }
                }
            }

            // Add castling moves if the King has not moved yet
            if (!this.hasMoved) {
                legalMoves.add(convertToPosition(row, column + 2)); // Kingside castling
                legalMoves.add(convertToPosition(row, column - 2)); // Queenside castling
            }

            return legalMoves;
        }

        /**
         * Returns the initial letter representing the King piece.
         *
         * @return 'k' for King.
         */
        @Override
        protected char getPieceInitial() {
            return 'k'; // 'k' for king
        }
    }
}