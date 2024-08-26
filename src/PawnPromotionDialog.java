import javax.swing.*;
import java.awt.*;

public class PawnPromotionDialog extends JFrame {

    private String promotionChoice;
    private Chessboard chessboard; // Reference to the Chessboard

    public PawnPromotionDialog(Chessboard chessboard, int row, int col, boolean white) {
        this.chessboard = chessboard;
        setTitle("Pawn Promotion");
        setLayout(new FlowLayout());
        setSize(400, 100);
        setUndecorated(true);
        setLocation(525, white ? 75 : 825);

        // Create buttons for each piece

        // queen
        Icon queenIcon = new ImageIcon(
                white ? getClass().getResource("/icons/wq.png") : getClass().getResource("/icons/bq.png"));
        JButton queenButton = new JButton(queenIcon);
        queenButton.setFocusPainted(false);

        // rook
        Icon rookIcon = new ImageIcon(
                white ? getClass().getResource("/icons/wr.png") : getClass().getResource("/icons/br.png"));
        JButton rookButton = new JButton(rookIcon);
        rookButton.setFocusPainted(false);

        // bisop
        Icon bishopIcon = new ImageIcon(
                white ? getClass().getResource("/icons/wb.png") : getClass().getResource("/icons/bb.png"));
        JButton bishopButton = new JButton(bishopIcon);
        bishopButton.setFocusPainted(false);

        // knight
        Icon kinghtIcon = new ImageIcon(
                white ? getClass().getResource("/icons/wn.png") : getClass().getResource("/icons/bn.png"));
        JButton knightButton = new JButton(kinghtIcon);
        knightButton.setFocusPainted(false);

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
    }

    private void handlePromotionChoice(String choice, int row, int col) {
        this.promotionChoice = choice;
        chessboard.promotePawn(row, col, choice);
        chessboard.revalidate(); // Ensures the component hierarchy is up to date
        chessboard.repaint();
        dispose(); // Close the promotion frame

    }

    public String getPromotionChoice() {
        return promotionChoice;
    }
}
