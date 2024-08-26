import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Chessboard extends JPanel {
    private static final int SIZE = 8;
    private static final Color LIGHT_SQUARE_COLOR = new Color(240, 217, 181);
    private static final Color DARK_SQUARE_COLOR = new Color(181, 136, 99);
    private static final Color LEGAL_MOVE_COLOR = new Color(100, 100, 100, 75);
    private static final int TILE_SIZE = 80;

    private BoardState boardState; // BoardState instance
    private Pieces selectedPiece; // Currently selected piece
    private List<String> legalMoves; // List of legal moves for the selected piece

    private Point dragStartPoint; // Starting point of the drag
    private Point dragCurrentPoint; // Current point of the drag

    public Chessboard() {
        setPreferredSize(new Dimension(SIZE * TILE_SIZE, SIZE * TILE_SIZE));
        setBackground(Color.WHITE);
        boardState = new BoardState();

        // Initialize all major pieces excluding pawns
        List<Pieces> pieces = new ArrayList<>();
        String[] pieceTypes = { "rook", "knight", "bishop", "queen", "king", "bishop", "knight", "rook" };

        for (int col = 0; col < SIZE; col++) {
            pieces.add(ChessPieceFactory.createPiece(pieceTypes[col], 0, col, "black", boardState));
            pieces.add(ChessPieceFactory.createPiece("pawn", 1, col, "black", boardState));

            pieces.add(ChessPieceFactory.createPiece(pieceTypes[col], 7, col, "white", boardState));
            pieces.add(ChessPieceFactory.createPiece("pawn", 6, col, "white", boardState));
        }

        boardState.initializeBoard(pieces);

        // Add mouse listeners to handle clicks and dragging
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePress(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseRelease(e.getX(), e.getY());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDrag(e.getX(), e.getY());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the chessboard
        drawChessboard(g2d);

        // Draw the pieces
        drawPieces(g2d);

        // Draw grey circles on legal moves if a piece is selected
        if (selectedPiece != null && legalMoves != null) {
            highlightLegalMoves(g2d);
        }

        // Draw the piece being dragged
        if (dragStartPoint != null && dragCurrentPoint != null && selectedPiece != null) {
            ImageIcon icon = selectedPiece.getIcon();
            g2d.drawImage(icon.getImage(), dragCurrentPoint.x - TILE_SIZE / 2, dragCurrentPoint.y - TILE_SIZE / 2,
                    TILE_SIZE, TILE_SIZE, null);
        }
    }

    private void drawChessboard(Graphics2D g2d) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    g2d.setColor(LIGHT_SQUARE_COLOR);
                } else {
                    g2d.setColor(DARK_SQUARE_COLOR);
                }
                g2d.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void drawPieces(Graphics2D g2d) {
        List<Pieces> pieces = boardState.getPieces();
        for (Pieces piece : pieces) {
            if (piece != selectedPiece) { // Draw only non-selected pieces
                int x = piece.getColumn() * TILE_SIZE;
                int y = piece.getRow() * TILE_SIZE;
                piece.draw(g2d, x, y, TILE_SIZE);
            }
        }
    }

    private void highlightLegalMoves(Graphics2D g2d) {
        g2d.setColor(LEGAL_MOVE_COLOR);
        for (String move : legalMoves) {
            int[] coords = convertToCoords(move);
            int x = coords[1] * TILE_SIZE;
            int y = coords[0] * TILE_SIZE;
            int circleDiameter = TILE_SIZE / 3; // Diameter of the circle

            // Draw the circle
            g2d.fillOval(x + TILE_SIZE / 2 - circleDiameter / 2, y + TILE_SIZE / 2 - circleDiameter / 2, circleDiameter,
                    circleDiameter);
        }
    }

    private void handleMousePress(int x, int y) {
        int row = y / TILE_SIZE;
        int col = x / TILE_SIZE;

        // Find the piece at the clicked position
        selectedPiece = findPieceAt(row, col);
        if (selectedPiece != null) {
            legalMoves = selectedPiece.getLegalMoves();
            // Filter legal moves to avoid leaving the king in check
            legalMoves = filterLegalMovesToAvoidCheck(selectedPiece, legalMoves);
            dragStartPoint = new Point(x, y);
            dragCurrentPoint = new Point(x, y);
            repaint();
        } else {
            selectedPiece = null;
            legalMoves = null;
            dragStartPoint = null;
            dragCurrentPoint = null;
            repaint(); // Remove highlights
        }
    }

    private void handleMouseRelease(int x, int y) {
        if (selectedPiece != null) {
            int row = y / TILE_SIZE;
            int col = x / TILE_SIZE;
            System.out.println(selectedPiece.getRow() + "," + selectedPiece.getColumn());
            // Check if the new position is a legal move
            if (legalMoves.contains(convertToPosition(row, col))) {
                Pieces targetPiece = findPieceAt(row, col);

                if (selectedPiece instanceof ChessPieces.Pawn) {
                    if (row == 0 || row == 7) {
                        PawnPromotionDialog promotionDialog = new PawnPromotionDialog(this, row, col);
                        promotionDialog.setVisible(true);
                    }
                }

                // Castling Logic
                if (selectedPiece instanceof ChessPieces.King) {
                    ChessPieces.King king = (ChessPieces.King) selectedPiece;
                    if (!king.hasMoved()) {
                        boolean castlingOccurred = false;
                        if (row == 7) {
                            if (col == 6) {
                                boardState.castleRooks(true, true);
                                castlingOccurred = true;
                            } else if (col == 2) {
                                boardState.castleRooks(false, true);
                                castlingOccurred = true;
                            }
                        } else if (row == 0) {
                            if (col == 6) {
                                boardState.castleRooks(true, false);
                                castlingOccurred = true;
                            } else if (col == 2) {
                                boardState.castleRooks(false, false);
                                castlingOccurred = true;
                            }
                        }

                        if (castlingOccurred) {
                            king.movedPiece();
                        }
                    }

                    System.out.println("This king's moved state is: " + king.hasMoved());
                }

                if (selectedPiece instanceof ChessPieces.Rook) {
                    ChessPieces.Rook rook = (ChessPieces.Rook) selectedPiece;
                    System.out.println("This rook's moved state is: " + rook.hasMoved());
                }

                if (targetPiece != null && !targetPiece.getColor().equals(selectedPiece.getColor())) {
                    boardState.removePiece(targetPiece);
                }
                if (selectedPiece instanceof ChessPieces.King) {
                    ChessPieces.King king = (ChessPieces.King) selectedPiece;
                    king.movedPiece();
                }

                if (selectedPiece instanceof ChessPieces.Rook) {
                    ChessPieces.Rook rook = (ChessPieces.Rook) selectedPiece;
                    rook.movedPiece();
                }

                // Update piece position
                selectedPiece.setRow(row);
                selectedPiece.setColumn(col);
                boardState.removePiece(selectedPiece); // Remove piece from old position
                boardState.addPiece(selectedPiece); // Add piece to new position
            }

            // Clear selection and repaint
            selectedPiece = null;
            legalMoves = null;
            dragStartPoint = null;
            dragCurrentPoint = null;
            repaint();
        }
    }

    private void handleMouseDrag(int x, int y) {
        if (selectedPiece != null) {
            dragCurrentPoint = new Point(x, y);
            repaint(); // Repaint to show the piece being dragged
        }

    }

    private List<String> filterLegalMovesToAvoidCheck(Pieces piece, List<String> moves) {
        List<String> filteredMoves = new ArrayList<>();
        for (String move : moves) {
            int[] coords = convertToCoords(move);
            if (!boardState.moveLeavesKingInCheck(piece, coords[0], coords[1])) {
                filteredMoves.add(move);
            }
        }
        if (piece instanceof ChessPieces.King) {
            ChessPieces.King king = (ChessPieces.King) piece;
            Pieces blockingPiece = boardState.getPieceAt(piece.getColor() == "white" ? 7 : 0, 1);
            System.err.println(blockingPiece);
            // Castling logic for both White and Black King
            Map<String, String> kingCastlingConditions = Map.of(
                    "f8", "g8", // White King castling (Kingside)
                    "d8", "c8", // White King castling (Queenside)
                    "d1", "c1", // Black King castling (Queenside)
                    "f1", "g1" // Black King castling (Kingside)
            );
            if (!king.hasMoved()) {
                for (Map.Entry<String, String> entry : kingCastlingConditions.entrySet()) {
                    if (!filteredMoves.contains(entry.getKey())) {
                        filteredMoves.remove(entry.getValue());
                    }
                }
                if (blockingPiece != null && !king.hasMoved()) {
                    filteredMoves.remove(piece.getColor().equals("white") ? "c8" : "c1");
                }
            }
        }

        return filteredMoves;
    }

    private Pieces findPieceAt(int row, int col) {
        for (Pieces piece : boardState.getPieces()) {
            if (piece.getRow() == row && piece.getColumn() == col) {
                return piece;
            }
        }
        return null;
    }

    private String convertToPosition(int row, int column) {
        char columnChar = (char) ('a' + column);
        return "" + columnChar + (row + 1);
    }

    private int[] convertToCoords(String position) {
        char columnChar = position.charAt(0);
        int column = columnChar - 'a';
        int row = Character.getNumericValue(position.charAt(1)) - 1;
        return new int[] { row, column };
    }

    public void promotePawn(int row, int col, String pieceChoice) {
        // Get the pawn that is being promoted
        Pieces pawn = boardState.getPieceAt(row, col);
        if (pawn == null || !(pawn instanceof ChessPieces.Pawn)) {
            return; // No valid pawn found at the specified location
        }

        String color = pawn.getColor();

        // Remove the pawn from the board
        boardState.removePiece(pawn);

        // Use the factory to create the new piece
        Pieces newPiece = ChessPieceFactory.createPiece(pieceChoice, row, col, color, boardState);

        // Add the new piece to the board
        boardState.addPiece(newPiece);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Remove the title bar
        frame.add(new Chessboard());
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}
