import java.util.ArrayList; // Import ArrayList class to handle dynamic arrays.
import java.util.List; // Import List interface to define lists of pieces.

public class BoardState {
    private List<Pieces> pieces; // A list to hold all the pieces currently on the board.

    // Constructor initializes the list of pieces.
    public BoardState() {
        pieces = new ArrayList<>();
    }

    // Method to initialize the board with a list of initial pieces.
    public void initializeBoard(List<Pieces> initialPieces) {
        pieces.clear(); // Clear any existing pieces from the board.
        pieces.addAll(initialPieces); // Add all the initial pieces to the board.
    }

    // Method to get the current list of pieces on the board.
    public List<Pieces> getPieces() {
        return pieces;
    }

    // Method to get the piece located at a specific row and column.
    public Pieces getPieceAt(int row, int column) {
        // Loop through all pieces to find the one at the specified position.
        for (Pieces piece : pieces) {
            if (piece.getRow() == row && piece.getColumn() == column) {
                return piece; // Return the piece if found.
            }
        }
        return null; // Return null if no piece is found at the position.
    }

    // Method to check if a position is occupied by a piece of the same color.
    public boolean isPositionOccupiedBySameColor(int row, int column, String color) {
        Pieces piece = getPieceAt(row, column); // Get the piece at the specified position.
        return piece != null && piece.getColor().equals(color); // Check if the piece exists and matches the specified
                                                                // color.
    }

    // Method to add a piece to the board.
    public void addPiece(Pieces piece) {
        pieces.add(piece); // Add the piece to the list.
    }

    // Method to remove a piece from the board.
    public void removePiece(Pieces piece) {
        pieces.remove(piece); // Remove the piece from the list.
    }

    // Method to check if the king of a specific color is in check.
    public boolean isKingInCheck(String color) {
        Pieces king = findKing(color); // Find the king of the specified color.
        if (king == null) { // If no king is found, return false.
            return false;
        }

        // Check if any opponent's piece can move to the king's position.
        for (Pieces piece : pieces) {
            if (!piece.getColor().equals(color)) { // Check only opponent's pieces.
                if (piece.getLegalMoves().contains(convertToPosition(king.getRow(), king.getColumn()))) {
                    return true; // If an opponent can attack the king, return true.
                }
            }
        }

        return false; // Return false if the king is not in check.
    }

    // Private method to find the king of a specific color on the board.
    private Pieces findKing(String color) {
        // Loop through all pieces to find the king of the specified color.
        for (Pieces piece : pieces) {
            if (piece instanceof ChessPieces.King && piece.getColor().equals(color)) {
                return piece; // Return the king if found.
            }
        }
        return null; // Return null if no king is found.
    }

    // Private method to convert a board position to a standard chess notation
    // (e.g., "e4").
    private String convertToPosition(int row, int column) {
        char columnChar = (char) ('a' + column); // Convert column index to letter.
        return "" + columnChar + (row + 1); // Combine column letter with row number.
    }

    // Method to check if a move leaves the king in check.
    public boolean moveLeavesKingInCheck(Pieces piece, int newRow, int newColumn) {
        int oldRow = piece.getRow(); // Store the piece's current row.
        int oldColumn = piece.getColumn(); // Store the piece's current column.
        Pieces targetPiece = getPieceAt(newRow, newColumn); // Get any piece at the new position.

        // Temporarily move the piece to the new position.
        piece.setRow(newRow);
        piece.setColumn(newColumn);
        if (targetPiece != null) {
            pieces.remove(targetPiece); // Remove the target piece if it exists.
        }

        // Check if the move leaves the king in check.
        boolean kingInCheck = isKingInCheck(piece.getColor());

        // Revert the piece to its original position.
        piece.setRow(oldRow);
        piece.setColumn(oldColumn);
        if (targetPiece != null) {
            pieces.add(targetPiece); // Re-add the target piece if it was removed.
        }

        return kingInCheck; // Return whether the move leaves the king in check.
    }

    // Private method to move a piece to a new position.
    private void movePiece(Pieces piece, int row, int col) {
        piece.setRow(row); // Set the piece's new row.
        piece.setColumn(col); // Set the piece's new column.
    }

    // Method to handle castling for the rooks.
    public void castleRooks(boolean kingSide, boolean white) {
        int row = white ? 7 : 0; // Determine the row based on the color of the player.
        int col = kingSide ? 7 : 0; // Determine the rook's initial column based on kingSide.
        int newCol = kingSide ? 5 : 3; // Determine the rook's new column after castling.

        Pieces rook = getPieceAt(row, col); // Get the rook at the specified position.
        if (rook instanceof ChessPieces.Rook) { // Check if the piece is a rook.
            ChessPieces.Rook castlingRook = (ChessPieces.Rook) rook;
            if (!castlingRook.hasMoved()) { // Ensure the rook has not moved before.
                movePiece(castlingRook, row, newCol); // Move the rook to its new position.
                castlingRook.movedPiece(); // Mark the rook as having moved.
            }
        }
    }

}
