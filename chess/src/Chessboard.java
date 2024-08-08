import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.ArrayList;

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
            pieces.add(ChessPieceFactory.createPiece(pieceTypes[col], 7, col, "white", boardState));
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

            // Check if the new position is a legal move
            if (legalMoves.contains(convertToPosition(row, col))) {
                Pieces targetPiece = findPieceAt(row, col);
                if (targetPiece != null && !targetPiece.getColor().equals(selectedPiece.getColor())) {
                    // Capture the opponent's piece
                    boardState.removePiece(targetPiece);
                }

                // Update piece position
                selectedPiece.setRow(row);
                selectedPiece.setColumn(col);
                boardState.removePiece(selectedPiece); // Remove piece from old position
                boardState.addPiece(selectedPiece); // Add piece to new position

                // Check if the king is in check
                if (boardState.isKingInCheck(selectedPiece.getColor())) {
                    System.out.println("King is in check!");
                }
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
