package chess_piece;

import chessboard.*;


public class Bishop extends  ChessPiece{
    public Bishop(String color, int row, int col)
    {
        super("Bishop", color, row, col);
        if(color.equals("white")) this.setImage(getImage("/Piece/white_bishop"));
        else this.setImage(getImage("/Piece/black_bishop"));
    }

    @Override
    public boolean isValidMove(int col, int row)
    {

        if(!this.is_within_board(col,row)) return false;
        if(this.did_not_move(col, row)) return false;

        if (Math.abs(col - this.getColPosition()) != Math.abs(row - this.getOld_rowPosition())) return false;
        if((ChessBoard.piece_squares[row][col] != null && ChessBoard.piece_squares[row][col].getColor().equals(this.getColor())))
        {
            return false;
        }
        int colDirection = 0;
        int rowDirection = 0;

        if (col > this.getColPosition()) colDirection = 1;
        else colDirection = -1;

        if (row > this.getOld_rowPosition()) rowDirection = 1;
        else rowDirection = -1;

        int aheadCol = this.getColPosition() + colDirection;
        int aheadRow = this.getOld_rowPosition() + rowDirection;
        while (aheadCol != col && aheadRow != row) {
            if (ChessBoard.piece_squares[aheadRow][aheadCol] != null) {
                return false;
            }
            aheadCol += colDirection;
            aheadRow += rowDirection;
        }

        boolean beingChecked = isInCheck(col, row);
        if(beingChecked) return false;

        return true;
    }
}
