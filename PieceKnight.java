import java.awt.Image;
import java.util.ArrayList;


public class PieceKnight extends Piece {
    public PieceKnight(Boolean isWhite){
        super(Game.pieceImagesBlack.get(PieceType.KNIGHT), Game.pieceImagesWhite.get(PieceType.KNIGHT));
        this.isWhite=isWhite;
        this.pieceType=PieceType.KNIGHT;
    }

    public ArrayList<int[]> getValidMoves(Piece[][] chessboard, int pieceRow, int pieceCol) {
        ArrayList<int[]> result = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int[] pos = {row, col};

                // Check if the move forms an L-shape (absolute differences are 1 and 2)
                int rowDiff = Math.abs(row - pieceRow);
                int colDiff = Math.abs(col - pieceCol);
                
                if ((rowDiff == 1 && colDiff == 2) || (rowDiff == 2 && colDiff == 1)) {
                    if(!(chessboard[row][col] != null && chessboard[row][col].isWhite == isWhite))
                        result.add(pos);
                }
            }
        }

        return result;
    }
}