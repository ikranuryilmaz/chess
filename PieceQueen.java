import java.awt.Image;
import java.util.ArrayList;

public class PieceQueen extends Piece {
    public PieceQueen(Boolean isWhite){
        super(Game.pieceImagesBlack.get(PieceType.QUEEN), Game.pieceImagesWhite.get(PieceType.QUEEN));
        this.isWhite=isWhite;
        this.pieceType=PieceType.QUEEN;
    }
    
    public ArrayList<int[]> getValidMoves(Piece[][] chessboard, int pieceRow, int pieceCol){
        PieceBishop bishop = new PieceBishop(isWhite);
        PieceRook rook = new PieceRook(isWhite);
        ArrayList<int[]> result = bishop.getValidMoves(chessboard, pieceRow, pieceCol);
        result.addAll(rook.getValidMoves(chessboard, pieceRow, pieceCol));
        return result;
    }
}