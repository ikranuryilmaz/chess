import java.awt.Image;
import java.util.ArrayList; 

public class Piece {

    public PieceType pieceType;
    public Image pieceImageBlack,pieceImageWhite;
    public Boolean isWhite = true;

    // Constructor
    public Piece(Image pieceImageBlack, Image pieceImageWhite) {
        this.pieceImageBlack = pieceImageBlack;
        this.pieceImageWhite = pieceImageWhite;
    }

    public Image getImage(){
        if (isWhite)
            return pieceImageWhite;
        else
            return pieceImageBlack;
    }

    public boolean isValidSquare(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public ArrayList<int[]> getValidMoves(Piece[][] chessboard, int pieceRow, int pieceCol){
        return null;
    }
    public ArrayList<int[]> getValidMovesPartI(Piece[][] chessboard, int pieceRow, int pieceCol){
        return getValidMoves(chessboard, pieceRow, pieceCol);
    }

    public Boolean isMoveValid(Piece[][] chessboard, int fromCol, int  fromRow, int  toCol, int toRow){
        for (int[] move : this.getValidMoves(chessboard, fromRow, fromCol)) {

            if (toRow==move[0] && toCol==move[1]){
                return true;
            }
        }
        return false;
    }

 /*   public void draw(Graphics2D g2d, int x, int y, ImageObserver observer) {
        if (image != null) {
            g2d.drawImage(image, x, y, observer);
        }
    }*/
}
