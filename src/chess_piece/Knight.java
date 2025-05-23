package chess_piece;

import chessboard.*;

public class Knight extends  ChessPiece{
    public Knight(String color, int row, int col)
    {
        super("Knight", color, row, col);
        if(color.equals("white")) this.setImage(getImage("/Piece/white_knight"));
        else this.setImage(getImage("/Piece/black_knight"));
    }

    @Override
    public boolean isValidMove(int col, int row)
    {
        if(!this.is_within_board(col,row)) return false;
        if(this.did_not_move(col,row)) return false;

        if(Math.abs(col-this.getColPosition()) == 1 && Math.abs(row-this.getOld_rowPosition()) == 2 || (Math.abs(col-this.getColPosition()) == 2 && Math.abs(row-this.getOld_rowPosition()) == 1))
        {
            if(ChessBoard.piece_squares[row][col] != null && ChessBoard.piece_squares[row][col].getColor().equals(this.getColor()) )
            {
                return false;
            }
            return true;
        }
        return false;

    }
}
