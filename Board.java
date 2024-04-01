import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

public class Board extends JPanel {
    public static int tileSize = 90;
    int columns = 8;
    int rows = 8;
    int height = 70;
    int width = 70;
    int turn = 0;

    public Map<String, Image> pieceImages;
    private String[][] chessboard;

    public int selectedRow = -1;
    public int selectedCol = -1;
    public int clickedColPromote = -1;
    public int clickedRowPromote = -1;

    public String selectedPieceName = "";

    public static int getTilesize() {
        return tileSize;
    }

    public Board() {
        this.setPreferredSize(new Dimension(columns * tileSize, rows * tileSize));
        this.setBackground(Color.magenta);
        initializePieceImages();
        initializeChessboard();
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    public void initializePieceImages() {
        pieceImages = new HashMap<>();

        pieceImages.put("whitePawn", loadImage("whitePawn.png"));
        pieceImages.put("whiteRook", loadImage("whiteRook.png"));
        pieceImages.put("whiteKnight", loadImage("whiteKnight.png"));
        pieceImages.put("whiteBishop", loadImage("whiteBishop.png"));
        pieceImages.put("whiteQueen", loadImage("whiteQueen.png"));
        pieceImages.put("whiteKing", loadImage("whiteKing.png"));

        pieceImages.put("blackPawn", loadImage("blackPawn.png"));
        pieceImages.put("blackRook", loadImage("blackRook.png"));
        pieceImages.put("blackKnight", loadImage("blackKnight.png"));
        pieceImages.put("blackBishop", loadImage("blackBishop.png"));
        pieceImages.put("blackQueen", loadImage("blackQueen.png"));
        pieceImages.put("blackKing", loadImage("blackKing.png"));
    }

    public Image loadImage(String imagePath) {
        try {
            Image im = ImageIO.read(new File(imagePath));
            return im.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void initializeChessboard() {
        chessboard = new String[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                chessboard[i][j] = getPieceNameInitialGame(i, j);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawChessboard(g2d);
        drawPieces(g2d);
    }

    private void drawChessboard(Graphics2D g2d) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                g2d.setColor((i + j) % 2 == 0 ? Color.white : Color.pink);
                g2d.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
            }
        }
    }

    private void drawPieces(Graphics2D g2d) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String pieceName = chessboard[i][j];
                Image pieceImage = pieceImages.get(pieceName);

                if (pieceImage != null) {
                    g2d.drawImage(pieceImage, j * tileSize + tileSize / 2 - width / 2,
                            i * tileSize + tileSize / 2 - height / 2, this);
                }
            }
        }
    }

    private void handleMouseClick(MouseEvent e) {
        int clickedCol = e.getX() / tileSize;
        int clickedRow = e.getY() / tileSize;

        String clickedPieceName = chessboard[clickedRow][clickedCol];

        if (!selectedPieceName.isEmpty()) {
            movePiece(selectedRow, selectedCol, clickedRow, clickedCol, selectedPieceName);
            selectedPieceName = "";
        } else {
            if (!clickedPieceName.isEmpty()) {
                selectedPieceName = clickedPieceName;
                selectedRow = clickedRow;
                selectedCol = clickedCol;
                System.out.println("Selected piece: " + selectedPieceName);
            } else {
                System.out.println("Clicked on an empty square.");
            }
        }

        repaint();
    }
    
    public boolean isMoveValid(String pieceName, int fromCol, int fromRow, int toCol, int toRow) {
        boolean isMoveValid = false;
        int whoseTurn = turn % 2;

        // Check whose turn it is
        if (whoseTurn == 0) { // White's turn
            if (pieceName.contains("black")) {
                return false;
            } else {
                if (pieceName.equals("whitePawn")) {
                    // Assuming pawns move forward and can move two squares on their first move
                    if (((fromCol == toCol && toRow - fromRow == 2 && fromRow == 1) && chessboard[toRow-1][toCol].equals("")||
                            (fromCol == toCol && toRow - fromRow == 1))&& chessboard[toRow][toCol].equals("")) {
                        isMoveValid = true;
                        System.out.println("aaa");
                    } else if (fromRow - toRow == -1 && Math.abs(fromCol - toCol) == 1
                            && chessboard[toRow][toCol].contains("black")) {
                        isMoveValid = true;
                    } 
                    if (isMoveValid &&(fromRow == 6 && toRow == 7)) {
                        choosePromotedPiece("white");
                    }
                } else if (pieceName.equals("whiteRook")) {
                    // Rooks can move vertically or horizontally
                    if (fromRow == toRow || fromCol == toCol) {
                        // Check for obstacles along the path
                        int rowStep = (fromRow == toRow) ? 0 : (toRow > fromRow) ? 1 : -1;
                        int colStep = (fromCol == toCol) ? 0 : (toCol > fromCol) ? 1 : -1;

                        int currentRow = fromRow + rowStep;
                        int currentCol = fromCol + colStep;

                        while (currentRow != toRow || currentCol != toCol) {
                            if (!chessboard[currentRow][currentCol].isEmpty()) {
                                // There's an obstacle in the path
                                return false;
                            }

                            currentRow += rowStep;
                            currentCol += colStep;
                        }

                        // Check if the destination square is empty or occupied by an opponent's piece
                        isMoveValid = chessboard[toRow][toCol].isEmpty()
                                || chessboard[toRow][toCol].contains("black");
                    }
                } else if (pieceName.equals("whiteKnight")) {
                    if ((Math.abs(fromCol - toCol) == 2 && Math.abs(fromRow - toRow) == 1) ||
                            (Math.abs(fromCol - toCol) == 1 && Math.abs(fromRow - toRow) == 2)
                                    && (chessboard[toRow][toCol].isEmpty()
                                            || chessboard[toRow][toCol].contains("black"))) {
                        isMoveValid = true;
                    }
                } else if (pieceName.equals("whiteBishop")
                        && (chessboard[toRow][toCol].isEmpty() || chessboard[toRow][toCol].contains("black"))) {
                    // Bishops move diagonally
                    if (Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol)) {
                        // Check for obstacles along the path
                        int rowStep = (toRow > fromRow) ? 1 : -1;
                        int colStep = (toCol > fromCol) ? 1 : -1;

                        int currentRow = fromRow + rowStep;
                        int currentCol = fromCol + colStep;

                        while (currentRow != toRow || currentCol != toCol) {
                            if (!chessboard[currentRow][currentCol].isEmpty()) {
                                // There's an obstacle in the path
                                return false;
                            }

                            currentRow += rowStep;
                            currentCol += colStep;
                        }

                        // Check if the destination square is empty or occupied by an opponent's piece
                        isMoveValid = chessboard[toRow][toCol].isEmpty()
                                || chessboard[toRow][toCol].contains("black");
                    }
                } else if (pieceName.equals("whiteQueen")) {
                    if ((Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol))
                            || (fromRow == toRow || fromCol == toCol)) {
                        int rowStep = (fromRow == toRow) ? 0 : (toRow > fromRow) ? 1 : -1;
                        int colStep = (fromCol == toCol) ? 0 : (toCol > fromCol) ? 1 : -1;

                        int currentRow = fromRow + rowStep;
                        int currentCol = fromCol + colStep;

                        while (currentRow != toRow || currentCol != toCol) {
                            if (!chessboard[currentRow][currentCol].isEmpty()) {
                                // There's an obstacle in the path
                                return false;
                            }

                            currentRow += rowStep;
                            currentCol += colStep;
                        }

                        // Check if the destination square is empty or occupied by an opponent's piece
                        isMoveValid = chessboard[toRow][toCol].isEmpty()
                                || chessboard[toRow][toCol].contains("black");
                    }

                } else if (pieceName.equals("whiteKing")) {
                    if (Math.abs(toRow - fromRow) <= 1 && Math.abs(fromCol - toCol) <= 1) {
                        isMoveValid = chessboard[toRow][toCol].isEmpty()
                                || chessboard[toRow][toCol].contains("black");
                    }
                }
                // Add conditions for other pieces or specific move rules

            }
        } else { // Black's turn
            if (pieceName.contains("white")) {
                return false;
            } else {

                if (pieceName.equals("blackPawn")) {
                    // Assuming pawns move forward and can move two squares on their first move
                    if (((fromCol == toCol && toRow - fromRow == -2 && fromRow == 6) && chessboard[toRow+1][toCol].equals("")||
                            (fromCol == toCol && toRow - fromRow == -1))&& chessboard[toRow][toCol].equals("")) {
                        isMoveValid = true;
                    } else if (fromRow - toRow == 1 && Math.abs(toCol - fromCol) == 1
                            && chessboard[toRow][toCol].contains("white")) {
                        isMoveValid = true;
                    }
                    if (isMoveValid &&(fromRow == 1 && toRow == 0)) {
                        choosePromotedPiece("black");
                    }
                } else if (pieceName.equals("blackRook")) {
                    // Rooks can move vertically or horizontally
                    if (fromRow == toRow || fromCol == toCol) {
                        // Check for obstacles along the path
                        int rowStep = (fromRow == toRow) ? 0 : (toRow > fromRow) ? 1 : -1;
                        int colStep = (fromCol == toCol) ? 0 : (toCol > fromCol) ? 1 : -1;

                        int currentRow = fromRow + rowStep;
                        int currentCol = fromCol + colStep;

                        while (currentRow != toRow || currentCol != toCol) {
                            if (!chessboard[currentRow][currentCol].isEmpty()) {
                                // There's an obstacle in the path
                                return false;
                            }

                            currentRow += rowStep;
                            currentCol += colStep;
                        }

                        // Check if the destination square is empty or occupied by an opponent's piece
                        isMoveValid = chessboard[toRow][toCol].isEmpty()
                                || chessboard[toRow][toCol].contains("white");
                    }
                } else if (pieceName.equals("blackKnight")) {
                    if ((Math.abs(fromCol - toCol) == 2 && Math.abs(fromRow - toRow) == 1) ||
                            (Math.abs(fromCol - toCol) == 1 && Math.abs(fromRow - toRow) == 2)
                                    && (chessboard[toRow][toCol].isEmpty()
                                            || chessboard[toRow][toCol].contains("white"))) {
                        isMoveValid = true;
                    }
                } else if (pieceName.equals("blackBishop")) {
                    // Bishops move diagonally
                    if (Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol)
                            && (chessboard[toRow][toCol].isEmpty() || chessboard[toRow][toCol].contains("white"))) {
                        // Check for obstacles along the path
                        int rowStep = (toRow > fromRow) ? 1 : -1;
                        int colStep = (toCol > fromCol) ? 1 : -1;

                        int currentRow = fromRow + rowStep;
                        int currentCol = fromCol + colStep;

                        while (currentRow != toRow || currentCol != toCol) {
                            if (!chessboard[currentRow][currentCol].isEmpty()) {
                                // There's an obstacle in the path
                                return false;
                            }

                            currentRow += rowStep;
                            currentCol += colStep;
                        }

                        // Check if the destination square is empty or occupied by an opponent's piece
                        isMoveValid = chessboard[toRow][toCol].isEmpty()
                                || chessboard[toRow][toCol].contains("white");
                    }
                } else if (pieceName.equals("blackQueen")) {
                    if ((Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol))
                            || (fromRow == toRow || fromCol == toCol)) {
                        int rowStep = (fromRow == toRow) ? 0 : (toRow > fromRow) ? 1 : -1;
                        int colStep = (fromCol == toCol) ? 0 : (toCol > fromCol) ? 1 : -1;

                        int currentRow = fromRow + rowStep;
                        int currentCol = fromCol + colStep;

                        while (currentRow != toRow || currentCol != toCol) {
                            if (!chessboard[currentRow][currentCol].isEmpty()) {
                                // There's an obstacle in the path
                                return false;
                            }

                            currentRow += rowStep;
                            currentCol += colStep;
                        }

                        // Check if the destination square is empty or occupied by an opponent's piece
                        isMoveValid = chessboard[toRow][toCol].isEmpty()
                                || chessboard[toRow][toCol].contains("white");
                    }

                } else if (pieceName.equals("blackKing")) {
                    if (Math.abs(toRow - fromRow) <= 1 && Math.abs(fromCol - toCol) <= 1) {
                        isMoveValid = chessboard[toRow][toCol].isEmpty()
                                || chessboard[toRow][toCol].contains("white");
                    }
                }
                // Add conditions for other pieces and their valid moves
            }

        }
        return isMoveValid;

    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol, String pieceName) {
        if (isMoveValid(pieceName, fromCol, fromRow, toCol, toRow)) {

            chessboard[toRow][toCol] = chessboard[fromRow][fromCol];
            chessboard[fromRow][fromCol] = "";
            clickedColPromote=toCol;
            clickedRowPromote=toRow;

            repaint();
            turn++;
        }

    }

    public void promotePawn(int row, int column, String colour, int choice) {
        String[] options;
        if (colour.equals("white")) {
            options = new String[] { "whiteQueen", "whiteRook", "whiteKnight", "whiteBishop" };
        } else {
            options = new String[] { "blackQueen", "blackRook", "blackKnight", "blackBishop" };
        }


      
        // Assign the chosen piece based on the user's selection
        String promotedPiece = options[choice];

        // Update the chessboard
        chessboard[row][column] = promotedPiece;

        // Trigger a repaint
        repaint();
    }

    public String getPieceNameInitialGame(int row, int col) {
        if ((row == 1 || row == 6) && col >= 0 && col < 8) {
            return (row == 1) ? "whitePawn" : "blackPawn";
        } else if (row == 0 && col == 0) {
            return "whiteRook";
        } else if (row == 0 && col == 1) {
            return "whiteKnight";
        } else if (row == 0 && col == 2) {
            return "whiteBishop";
        } else if (row == 0 && col == 3) {
            return "whiteQueen";
        } else if (row == 0 && col == 4) {
            return "whiteKing";
        } else if (row == 0 && col == 7) {
            return "whiteRook";
        } else if (row == 0 && col == 6) {
            return "whiteKnight";
        } else if (row == 0 && col == 5) {
            return "whiteBishop";
        } else if (row == 7 && col == 4) {
            return "blackKing";
        } else if (row == 7 && col == 3) {
            return "blackQueen";
        } else if (row == 7 && col == 2) {
            return "blackBishop";
        } else if (row == 7 && col == 1) {
            return "blackKnight";
        } else if (row == 7 && col == 0) {
            return "blackRook";
        } else if (row == 7 && col == 7) {
            return "blackRook";
        } else if (row == 7 && col == 6) {
            return "blackKnight";
        } else if (row == 7 && col == 5) {
            return "blackBishop";
        } else if (row == 7 && col == 4) {
            return "blackKing";
        } else if (row == 7 && col == 3) {
            return "blackQueen";
        } else if (row == 7 && col == 2) {
            return "blackBishop";
        } else if (row == 7 && col == 1) {
            return "blackKnight";
        } else if (row == 7 && col == 0) {
            return "blackRook";
        }
        return "";
    }

    public void choosePromotedPiece(String colour) {
        System.out.println("vbnmöç");
        JFrame frame = new JFrame("Pawn Promotion");
        frame.setLayout(new GridLayout(1, 4));

        String[] options = { "Queen", "Rook", "Knight", "Bishop" };
        int[] index={0,1,2,3};

        for (int i : index) {
            JButton button = new JButton(options[i]);
            button.addActionListener(e -> handlePromotionChoice(i, colour, frame));
            frame.add(button);
        }

        frame.setSize(400, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        

    }

    private void handlePromotionChoice(int selectedPiece, String colour, JFrame frame) {
        // Update the chessboard with the selected promoted piece
        promotePawn(clickedRowPromote, clickedColPromote, colour ,selectedPiece);
        frame.dispose();

        // Close the promotion window
        //((JFrame) SwingUtilities.getRoot(this)).dispose();
        //repaint();
    }

}
