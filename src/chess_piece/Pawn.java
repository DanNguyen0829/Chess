package chess_piece;

import chessboard.*;
import main.*;

public class Pawn extends  ChessPiece{
    public Pawn(String color, int row, int col)
    {
        super("Pawn", color, row, col);
        if(color.equals("white")) this.setImage(getImage("/Piece/white_pawn"));
        else this.setImage(getImage("/Piece/black_pawn"));
    }

    @Override
    public boolean isValidMove(int col, int row) {
        if (!this.is_within_board(col, row)) return false; //If not in board then return false
        if (this.did_not_move(col, row)) return false;

        int colDiff = Math.abs(col - this.getColPosition());
        int rowDiff = row - this.getOld_rowPosition();

        if (getColor().equals("white")){
            if(colDiff==0 && !hasMove && rowDiff==-2 && ChessBoard.piece_squares[row][col]==null)
            {
                boolean beingChecked = isInCheck(col, row);
                if(beingChecked) return false;

                return true;
            }
            else if(colDiff==0 && rowDiff==-1 && ChessBoard.piece_squares[row][col]==null)
            {
                boolean beingChecked = isInCheck(col, row);

                if(beingChecked) return false;

                return true;
            }
            else if(colDiff==1 && rowDiff==-1 && ChessBoard.piece_squares[row][col]!=null && !this.getColor().equals(ChessBoard.piece_squares[row][col].getColor()))
            {
                boolean beingChecked = isInCheck(col, row);

                if(beingChecked) return false;

                return true;
            }
        }
        else
        {
            if(colDiff==0 && !hasMove && rowDiff==2 && ChessBoard.piece_squares[row][col]==null)
            {

                boolean beingChecked = isInCheck(col, row);
                if(beingChecked) return false;

                return true;
            }
            else if(colDiff==0 && rowDiff==1 && ChessBoard.piece_squares[row][col]==null)
            {

                boolean beingChecked = isInCheck(col, row);
                if(beingChecked) return false;

                return true;
            }
            else if(colDiff==1 && rowDiff==1 && ChessBoard.piece_squares[row][col]!=null && !this.getColor().equals(ChessBoard.piece_squares[row][col].getColor()))
            {

                boolean beingChecked = isInCheck(col, row);
                if(beingChecked) return false;

                return true;
            }
        }
        return false;
    }
}
