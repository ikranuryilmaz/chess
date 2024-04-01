import java.util.ArrayList;

public class PiecePawn extends Piece {
    public PiecePawn(Boolean isWhite){
        super(Game.pieceImagesBlack.get(PieceType.PAWN), Game.pieceImagesWhite.get(PieceType.PAWN));
        this.isWhite=isWhite;
        this.pieceType=PieceType.PAWN;
    }


    public ArrayList<int[]> getValidMoves(Piece[][] chessboard, int pieceRow, int pieceCol) {
        ArrayList<int[]> result = new ArrayList<>();

        // Define the direction in which the pawn can move based on its color
        int direction = (isWhite) ? -1 : 1;

        // Move one square forward
        int newRow = pieceRow + direction;
        int newCol = pieceCol;
        if (isValidSquare(newRow, newCol) && chessboard[newRow][newCol] == null) {
            result.add(new int[]{newRow, newCol});
        }

        // Move two squares forward if it's the pawn's first move
        newRow = pieceRow + 2 * direction;
        if (isValidSquare(newRow, newCol) && chessboard[newRow][newCol] == null &&
            isInitialPawnPosition(pieceRow, isWhite)) {
            result.add(new int[]{newRow, newCol});
        }

        // Capture diagonally to the left
        newRow = pieceRow + direction;
        newCol = pieceCol - 1;
        if (isValidSquare(newRow, newCol) && chessboard[newRow][newCol] != null &&
            chessboard[newRow][newCol].isWhite != isWhite) {
            result.add(new int[]{newRow, newCol});
        }

        // Capture diagonally to the right
        newCol = pieceCol + 1;
        if (isValidSquare(newRow, newCol) && chessboard[newRow][newCol] != null &&
            chessboard[newRow][newCol].isWhite != isWhite) {
            result.add(new int[]{newRow, newCol});
        }

        return result;
    }

    private boolean isInitialPawnPosition(int row, boolean isWhite) {
        return (isWhite && row == 6) || (!isWhite && row == 1);
    }
}