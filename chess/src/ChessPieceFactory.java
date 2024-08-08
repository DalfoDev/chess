public class ChessPieceFactory {

    public static Pieces createPiece(String type, int row, int column, String color, BoardState boardState) {
        switch (type.toLowerCase()) {

            case "queen":
                return new ChessPieces.Queen(row, column, color, boardState);
            case "rook":
                return new ChessPieces.Rook(row, column, color, boardState);
            case "bishop":
                return new ChessPieces.Bishop(row, column, color, boardState);
            case "knight":
                return new ChessPieces.Knight(row, column, color, boardState);
            case "pawn":
                return new ChessPieces.Pawn(row, column, color, boardState);
            case "king":
                return new ChessPieces.King(row, column, color, boardState);

            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }
}
