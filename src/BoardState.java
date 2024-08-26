import java.util.ArrayList;
import java.util.List;

public class BoardState {
    private List<Pieces> pieces;

    public BoardState() {
        pieces = new ArrayList<>();
    }

    public void initializeBoard(List<Pieces> initialPieces) {
        pieces.clear();
        pieces.addAll(initialPieces);
    }

    public List<Pieces> getPieces() {
        return pieces;
    }

    public Pieces getPieceAt(int row, int column) {
        for (Pieces piece : pieces) {
            if (piece.getRow() == row && piece.getColumn() == column) {
                return piece;
            }
        }
        return null;
    }

    public boolean isPositionOccupiedBySameColor(int row, int column, String color) {
        Pieces piece = getPieceAt(row, column);
        return piece != null && piece.getColor().equals(color);
    }

    public void addPiece(Pieces piece) {
        pieces.add(piece);
    }

    public void removePiece(Pieces piece) {
        pieces.remove(piece);
    }

    public boolean isKingInCheck(String color) {
        Pieces king = findKing(color);
        if (king == null) {
            return false;
        }

        for (Pieces piece : pieces) {
            if (!piece.getColor().equals(color)) {
                if (piece.getLegalMoves().contains(convertToPosition(king.getRow(), king.getColumn()))) {
                    return true;
                }
            }
        }

        return false;
    }

    private Pieces findKing(String color) {
        for (Pieces piece : pieces) {
            if (piece instanceof ChessPieces.King && piece.getColor().equals(color)) {
                return piece;
            }
        }
        return null;
    }

    private String convertToPosition(int row, int column) {
        char columnChar = (char) ('a' + column);
        return "" + columnChar + (row + 1);
    }

    public boolean moveLeavesKingInCheck(Pieces piece, int newRow, int newColumn) {
        int oldRow = piece.getRow();
        int oldColumn = piece.getColumn();
        Pieces targetPiece = getPieceAt(newRow, newColumn);

        piece.setRow(newRow);
        piece.setColumn(newColumn);
        if (targetPiece != null) {
            pieces.remove(targetPiece);
        }

        boolean kingInCheck = isKingInCheck(piece.getColor());

        piece.setRow(oldRow);
        piece.setColumn(oldColumn);
        if (targetPiece != null) {
            pieces.add(targetPiece);
        }

        return kingInCheck;
    }

    private void movePiece(Pieces piece, int row, int col) {
        piece.setRow(row);
        piece.setColumn(col);
    }

    public void castleRooks(boolean kingSide, boolean white) {
        int row = white ? 7 : 0;
        int col = kingSide ? 7 : 0;
        int newCol = kingSide ? 5 : 3;

        Pieces rook = getPieceAt(row, col);
        if (rook instanceof ChessPieces.Rook) {
            ChessPieces.Rook castlingRook = (ChessPieces.Rook) rook;
            if (!castlingRook.hasMoved()) {
                movePiece(castlingRook, row, newCol);
                castlingRook.movedPiece();
            }
        }
    }

}
