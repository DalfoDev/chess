import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ChessPieces {

    // Queen Class
    public static class Queen extends Pieces {
        private BoardState boardState;

        public Queen(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
        }

        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            return (newRow == row || newColumn == column || Math.abs(newRow - row) == Math.abs(newColumn - column));
        }

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

        @Override
        protected char getPieceInitial() {
            return 'q'; // 'q' for queen
        }
    }

    // Pawn Class
    public static class Pawn extends Pieces {
        private BoardState boardState;

        public Pawn(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
        }

        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            int rowDiff = newRow - row;
            int colDiff = Math.abs(newColumn - column);

            if (color.equals("white")) {
                if (rowDiff == -1 && colDiff == 0 && boardState.getPieceAt(newRow, newColumn) == null) {
                    return true; // Move forward
                }
                if (row == 6 && rowDiff == -2 && colDiff == 0 && boardState.getPieceAt(newRow, newColumn) == null
                        && boardState.getPieceAt(row - 1, column) == null) {
                    return true; // Initial double move
                }
                if (rowDiff == -1 && colDiff == 1 && boardState.getPieceAt(newRow, newColumn) != null
                        && !boardState.isPositionOccupiedBySameColor(newRow, newColumn, color)) {
                    return true; // Capture
                }
            } else {
                if (rowDiff == 1 && colDiff == 0 && boardState.getPieceAt(newRow, newColumn) == null) {
                    return true; // Move forward
                }
                if (row == 1 && rowDiff == 2 && colDiff == 0 && boardState.getPieceAt(newRow, newColumn) == null
                        && boardState.getPieceAt(row + 1, column) == null) {
                    return true; // Initial double move
                }
                if (rowDiff == 1 && colDiff == 1 && boardState.getPieceAt(newRow, newColumn) != null
                        && !boardState.isPositionOccupiedBySameColor(newRow, newColumn, color)) {
                    return true; // Capture
                }
            }

            return false;
        }

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

        @Override
        protected char getPieceInitial() {
            return 'p'; // 'p' for pawn
        }
    }

    // Rook Class
    public static class Rook extends Pieces {
        private BoardState boardState;
        private boolean hasMoved;

        public Rook(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
            this.hasMoved = false;
        }

        public boolean hasMoved() {
            return this.hasMoved;
        }

        public void movedPiece() {
            this.hasMoved = true;
        }

        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            return newRow == row || newColumn == column;
        }

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

        @Override
        protected char getPieceInitial() {
            return 'r'; // 'r' for rook
        }
    }

    // Knight Class
    public static class Knight extends Pieces {
        private BoardState boardState;

        public Knight(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
        }

        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            int rowDiff = Math.abs(newRow - row);
            int colDiff = Math.abs(newColumn - column);
            return rowDiff * colDiff == 2;
        }

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

        @Override
        protected char getPieceInitial() {
            return 'n'; // 'n' for knight
        }
    }

    // Bishop Class
    public static class Bishop extends Pieces {
        private BoardState boardState;

        public Bishop(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
        }

        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            return Math.abs(newRow - row) == Math.abs(newColumn - column);
        }

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

        @Override
        protected char getPieceInitial() {
            return 'b'; // 'b' for bishop
        }
    }

    // King Class
    public static class King extends Pieces {
        private BoardState boardState;
        private boolean hasMoved;

        public King(int row, int column, String color, BoardState boardState) {
            super(row, column, color);
            this.boardState = boardState;
            this.hasMoved = false;

        }

        public boolean hasMoved() {
            return this.hasMoved;
        }

        public void movedPiece() {
            this.hasMoved = true;
        }

        @Override
        public void draw(Graphics g, int x, int y, int tileSize) {
            ImageIcon icon = getIcon();
            if (icon != null) {
                g.drawImage(icon.getImage(), x, y, tileSize, tileSize, null);
            } else {
                System.err.println("Error: Icon not found for path: " + getIconPath());
            }
        }

        @Override
        public boolean isValidMove(int newRow, int newColumn) {
            int rowDiff = Math.abs(newRow - row);
            int colDiff = Math.abs(newColumn - column);
            return (rowDiff <= 1 && colDiff <= 1);
        }

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

            if (!this.hasMoved) {
                legalMoves.add(convertToPosition(row, column + 2));
                legalMoves.add(convertToPosition(row, column - 2));
            }

            return legalMoves;
        }

        @Override
        protected char getPieceInitial() {
            return 'k'; // 'k' for king
        }

    }
}
