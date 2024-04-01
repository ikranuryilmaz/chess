import java.awt.Image;
import java.util.ArrayList;


public class PieceRook extends Piece {
    public PieceRook(Boolean isWhite){
        super(Game.pieceImagesBlack.get(PieceType.ROOK), Game.pieceImagesWhite.get(PieceType.ROOK));
        this.isWhite=isWhite;
        this.pieceType=PieceType.ROOK;
    }

    public ArrayList<int[]> getValidMoves(Piece[][] chessboard, int pieceRow, int pieceCol){
        ArrayList<int[]> result = new ArrayList<int[]>();
        for (int i=1;i<8;i++){
            int newRow=pieceRow+i;
            int newCol=pieceCol;
            if(isValidSquare(newRow, newCol)){
                int[] pos={newRow, newCol};
                if(chessboard[newRow][newCol] != null){
                    if(chessboard[newRow][newCol].isWhite != isWhite){
                        result.add(pos);
                    }
                    break;
                }
                else{
                    result.add(pos);
                }
            }
        }

        for (int i=1;i<8;i++){
            int newRow=pieceRow-i;
            int newCol=pieceCol;
            if(isValidSquare(newRow, newCol)){
                int[] pos={newRow, newCol};
                if(chessboard[newRow][newCol] != null){
                    if(chessboard[newRow][newCol].isWhite != isWhite){
                        result.add(pos);
                    }
                    break;
                }
                else{
                    result.add(pos);
                }
            }
        }
        
        for (int i=1;i<8;i++){
            int newRow=pieceRow;
            int newCol=pieceCol+i;
            if(isValidSquare(newRow, newCol)){
                int[] pos={newRow, newCol};
                if(chessboard[newRow][newCol] != null){
                    if(chessboard[newRow][newCol].isWhite != isWhite){
                        result.add(pos);
                    }
                    break;
                }
                else{
                    result.add(pos);
                }
            }
        }

        for (int i=1;i<8;i++){
            int newRow=pieceRow;
            int newCol=pieceCol-i;
            if(isValidSquare(newRow, newCol)){
                int[] pos={newRow, newCol};
                if(chessboard[newRow][newCol] != null){
                    if(chessboard[newRow][newCol].isWhite != isWhite){
                        result.add(pos);
                    }
                    break;
                }
                else{
                    result.add(pos);
                }
            }
        }
        return result;
    }
}