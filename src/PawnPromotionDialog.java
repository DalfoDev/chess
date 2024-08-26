import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PawnPromotionDialog extends JFrame {

    private String promotionChoice;
    private Chessboard chessboard; // Reference to the Chessboard

    public PawnPromotionDialog(Chessboard chessboard, int row, int col) {
        this.chessboard = chessboard;
        setTitle("Pawn Promotion");
        setLayout(new FlowLayout());
        setSize(400, 100);

        // Create buttons for each piece
        JButton queenButton = new JButton("Queen");
        JButton rookButton = new JButton("Rook");
        JButton bishopButton = new JButton("Bishop");
        JButton knightButton = new JButton("Knight");

        Icon icon = new ImageIcon("chess/src/icons/wq.png");
        JButton button7 = new JButton(icon);

        // Add action listeners to the buttons
        queenButton.addActionListener(e -> handlePromotionChoice("Queen", row, col));
        rookButton.addActionListener(e -> handlePromotionChoice("Rook", row, col));
        bishopButton.addActionListener(e -> handlePromotionChoice("Bishop", row, col));
        knightButton.addActionListener(e -> handlePromotionChoice("Knight", row, col));

        // Add buttons to the frame
        add(queenButton);
        add(rookButton);
        add(bishopButton);
        add(knightButton);
        add(button7);

        // Position the frame at the top or bottom of the chessboard
        setLocationRelativeTo(chessboard); // Center it on the main window
    }

    private void handlePromotionChoice(String choice, int row, int col) {
        this.promotionChoice = choice;
        chessboard.promotePawn(row, col, choice);
        dispose(); // Close the promotion frame
    }

    public String getPromotionChoice() {
        return promotionChoice;
    }
}
