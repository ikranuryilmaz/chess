import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics; 
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class Game  extends JPanel{
    public static Map<PieceType, Image> pieceImagesWhite;
    public static Map<PieceType, Image> pieceImagesBlack;
    private Piece[][] chessboard;
    private static int pieceSize = 70;
    private static int tileSize = 90;
    private int selectedCol = -1;
    private int selectedRow = -1;




    public Game(){
        this.setPreferredSize(new Dimension(8 * tileSize, 8 * tileSize));
        this.setBackground(Color.magenta);
        initializePieceImages();
        initializeChessboard();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickedColumn = (int)Math.floor((float)e.getX()/tileSize);
                int clikedRow = (int)Math.floor((float)e.getY()/tileSize);
                handleMouseClick(clickedColumn, clikedRow);
            }
        });
    }

    public Image loadImage(String imagePath) {
        try {
            Image im = ImageIO.read(new File(imagePath));
            return im.getScaledInstance(pieceSize, pieceSize, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void initializePieceImages(){
        pieceImagesWhite = new HashMap<>();
        pieceImagesWhite.put(PieceType.PAWN, loadImage("whitePawn.png"));
        pieceImagesWhite.put(PieceType.ROOK, loadImage("whiteRook.png"));
        pieceImagesWhite.put(PieceType.KNIGHT, loadImage("whiteKnight.png"));
        pieceImagesWhite.put(PieceType.BISHOP, loadImage("whiteBishop.png"));
        pieceImagesWhite.put(PieceType.QUEEN, loadImage("whiteQueen.png"));
        pieceImagesWhite.put(PieceType.KING, loadImage("whiteKing.png"));

        pieceImagesBlack = new HashMap<>();
        pieceImagesBlack.put(PieceType.PAWN, loadImage("blackPawn.png"));
        pieceImagesBlack.put(PieceType.ROOK, loadImage("blackRook.png"));
        pieceImagesBlack.put(PieceType.KNIGHT, loadImage("blackKnight.png"));
        pieceImagesBlack.put(PieceType.BISHOP, loadImage("blackBishop.png"));
        pieceImagesBlack.put(PieceType.QUEEN, loadImage("blackQueen.png"));
        pieceImagesBlack.put(PieceType.KING, loadImage("blackKing.png"));
    }

    public void initializeChessboard() {
        Piece[][] chessboard2 = {
            {new PieceRook(false), new PieceKnight(false), new PieceBishop(false), new PieceQueen(false), new PieceKing(false), new PieceBishop(false), new PieceKnight(false), new PieceRook(false)},
            {new PiecePawn(false), new PiecePawn(false), new PiecePawn(false), new PiecePawn(false), new PiecePawn(false), new PiecePawn(false), new PiecePawn(false), new PiecePawn(false)},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {new PiecePawn(true), new PiecePawn(true), new PiecePawn(true), new PiecePawn(true), new PiecePawn(true), new PiecePawn(true), new PiecePawn(true), new PiecePawn(true)},
            {new PieceRook(true), new PieceKnight(true), new PieceBishop(true), new PieceQueen(true), new PieceKing(true), new PieceBishop(true), new PieceKnight(true), new PieceRook(true)}
        };
        chessboard=chessboard2;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawChessboard(g2d);
        drawPieces(g2d);
    }
    
    private void drawChessboard(Graphics2D g2d) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                
                g2d.setColor((i + j) % 2 == 0 ? Color.white : Color.pink);
                if (j==selectedCol && i==selectedRow){
                    g2d.setColor(new Color(100,20,255));
                }
                else{
                    if (selectedCol!=-1 && chessboard[selectedRow][selectedCol]!=null && chessboard[selectedRow][selectedCol].isMoveValid(chessboard, selectedCol, selectedRow, j, i)){
                        g2d.setColor((i + j) % 2 == 0 ? new Color(80,20,255) : new Color(150,20,255));
                    }
                }
                g2d.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
            }
        }
    }

    private void drawPieces(Graphics2D g2d) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece selectedPiece = chessboard[i][j];

                if (selectedPiece != null) {
                    g2d.drawImage(selectedPiece.getImage(), j * tileSize + tileSize / 2 - pieceSize / 2,
                    i * tileSize + tileSize / 2 - pieceSize / 2, this);
                }
            }
        }
    }

    private void handleMouseClick(int clickedCol, int clickedRow) {

        if (selectedCol!=-1) {
            movePiece(selectedRow, selectedCol, clickedRow, clickedCol);
            selectedCol=-1;
            selectedRow=-1;
        } else {
            if (chessboard[clickedRow][clickedCol]!=null) {
                selectedRow = clickedRow;
                selectedCol = clickedCol;
            }
        }

        repaint();
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        if (chessboard[selectedRow][selectedCol].isMoveValid(chessboard, selectedCol, selectedRow, toCol, toRow)) {

            chessboard[toRow][toCol] = chessboard[fromRow][fromCol];
            chessboard[fromRow][fromCol] = null;
            
            Piece movedPiece = chessboard[toRow][toCol];
            if (movedPiece.pieceType==PieceType.PAWN){
                if(toRow==0 || toRow==7){
                    choosePromotedPiece(movedPiece.isWhite, toRow, toCol);
                }
            }
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(chessboard[i][j]!=null){
                        if(chessboard[i][j].pieceType==PieceType.KING){
                            PieceKing king= (PieceKing)chessboard[i][j];
                            if (king.isCheck(chessboard)){
                                selectedCol=-1;
                                selectedRow=-1;
                                repaint();
                                JOptionPane.showMessageDialog(null, "CHECK!");
                            }
                        }
                    }
                }
            }
            
        }
    }

    public void choosePromotedPiece(Boolean colour, int row, int col) {
        JFrame frame = new JFrame("Pawn Promotion");
        frame.setLayout(new GridLayout(1, 4));

        Piece[] options = { new PieceQueen(colour), new PieceKnight(colour), new PieceRook(colour), new PieceBishop(colour)};

        for (Piece option : options) {
            JButton button = new JButton(option.pieceType.toString());
            button.addActionListener(e -> handlePromotionChoice(option, row, col, frame));
            frame.add(button);
        }

        frame.setSize(400, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true); 
    }

    private void handlePromotionChoice(Piece selectedPiece, int row, int column, JFrame frame) {
        // Update the chessboard with the selected promoted piece
        chessboard[row][column] = selectedPiece;
        repaint();
        frame.dispose();
    }
    
    
}
