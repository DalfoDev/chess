import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.util.List;

public abstract class Pieces {
    // Attributes
    protected int row;
    protected int column;
    protected String position; // E.g., "e4"
    protected String color; // "white" or "black"
    protected String iconPath; // Path to the icon image file

    // Constructor
    public Pieces(int row, int column, String color) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.position = convertToPosition(row, column);
        this.iconPath = generateIconPath();
    }

    // Getters and Setters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
        this.position = convertToPosition(row, column);
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
        this.position = convertToPosition(row, column);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
        int[] coords = convertToCoords(position);
        this.row = coords[0];
        this.column = coords[1];
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.iconPath = generateIconPath(); // Update icon path when color changes
    }

    public String getIconPath() {
        return iconPath;
    }

    // Abstract methods
    public abstract void draw(Graphics g, int x, int y, int tileSize);

    public abstract boolean isValidMove(int newRow, int newColumn);

    public abstract List<String> getLegalMoves(); // New abstract method

    // Helper methods
    protected String convertToPosition(int row, int column) {
        char columnChar = (char) ('a' + column);
        return "" + columnChar + (row + 1);
    }

    protected int[] convertToCoords(String position) {
        char columnChar = position.charAt(0);
        int column = columnChar - 'a';
        int row = Character.getNumericValue(position.charAt(1)) - 1;
        return new int[] { row, column };
    }

    private String generateIconPath() {
        char pieceInitial = getPieceInitial();
        String colorPrefix = color.equals("white") ? "w" : "b";
        return "/icons/" + colorPrefix + pieceInitial + ".png";
    }

    protected abstract char getPieceInitial(); // Subclasses should implement this method

    // Load the icon image
    protected ImageIcon getIcon() {
        return new ImageIcon(getClass().getResource(getIconPath()));
    }
}
