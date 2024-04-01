import java.util.ArrayList;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PieceKing extends Piece {
    public PieceKing(Boolean isWhite) {
        super(Game.pieceImagesBlack.get(PieceType.KING), Game.pieceImagesWhite.get(PieceType.KING));
        this.isWhite = isWhite;
        this.pieceType = PieceType.KING;
    }

    private boolean checkShown = false;

    public ArrayList<int[]> getValidMovesPartI(Piece[][] chessboard, int pieceRow, int pieceCol) {
        ArrayList<int[]> result = new ArrayList<>();

        // Move one square forward
        for (int newRow = pieceRow - 1; newRow - pieceRow < 2; newRow++) {
            for (int newCol = pieceCol - 1; newCol - pieceCol < 2; newCol++) {
                if (isValidSquare(newRow, newCol) && chessboard[newRow][newCol] == null) {
                    result.add(new int[] { newRow, newCol });
                }
                if (isValidSquare(newRow, newCol) && chessboard[newRow][newCol] != null &&
                        chessboard[newRow][newCol].isWhite != isWhite) {
                    result.add(new int[] { newRow, newCol });
                }
            }
        }
        return result;
    }   

    public ArrayList<int[]> getValidMoves(Piece[][] chessboard, int pieceRow, int pieceCol) {
        ArrayList<int[]> result = this.getValidMovesPartI(chessboard, pieceRow, pieceCol);
       
        ArrayList<int[]> dangerPartI = this.inDangerPart2(chessboard,pieceRow,pieceCol);

        for (int i = result.size()-1; i>=0; i--) {
            for (int j = 0; j < dangerPartI.size(); j++) {
                if (dangerPartI.get(j)[0]==result.get(i)[0]&&dangerPartI.get(j)[1]==result.get(i)[1]) {
                    result.remove(i);
                    break;
                }
            }
        }

        return result;
    }

    Boolean isCheck(Piece[][] chessboard) {
        for (int icolumn = 0; icolumn < 8; icolumn++) {
            for (int jrow = 0; jrow < 8; jrow++) {
                if (chessboard[icolumn][jrow] != null) {
                    if (chessboard[icolumn][jrow].isWhite != isWhite) {
                        ArrayList<int[]> otherPieceMove = chessboard[icolumn][jrow].getValidMovesPartI(chessboard,
                                icolumn, jrow);
                        for (int i = 0; i < otherPieceMove.size(); i++) {
                            if (this.equals(chessboard[otherPieceMove.get(i)[0]][otherPieceMove.get(i)[1]])) {
                                return true;

                            }

                        }

                    }
                }

            }
        }

 /*
           * else{
           * checkShown=false;
           * }
           */

        return false;
    }

    ArrayList<int[]> inDangerPart2(Piece[][] chessboard, int pieceRow, int pieceCol) {
        ArrayList<int[]> danger = new ArrayList<>();
        ArrayList<int[]> potentialMoves = this.getValidMovesPartI(chessboard, pieceRow, pieceCol);

        for (int icolumn = 0; icolumn < 8; icolumn++) {
            for (int jrow = 0; jrow < 8; jrow++) {
                if (chessboard[icolumn][jrow] != null) {
                    if (chessboard[icolumn][jrow].isWhite != isWhite) {
                        ArrayList<int[]> otherPieceMove = chessboard[icolumn][jrow].getValidMovesPartI(chessboard, icolumn, jrow);
                        for (int i = 0; i < otherPieceMove.size(); i++) {
                            for (int[] move : potentialMoves) {

                                if (move[0] == otherPieceMove.get(i)[0] && move[1]==otherPieceMove.get(i)[1]) {
                                    danger.add(move);

                                }
                            }
                        }
                    }

                }
            }
        
        }
        return danger;
    }

}