package chess_piece;
import chessboard.*;
public class Rook extends  ChessPiece{
    public Rook(String color, int row, int col)
    {
        super("Rook", color, row, col);
        if(color.equals("white")) this.setImage(getImage("/Piece/white_rook"));
        else this.setImage(getImage("/Piece/black_rook"));
    }

    @Override
    public boolean isValidMove(int col, int row) {
        if (!this.is_within_board(col, row)) return false;
        if (this.did_not_move(col, row)) return false;

        int currentRow = this.getOld_rowPosition();
        int currentCol = this.getColPosition();

        if (currentRow != row && currentCol != col) return false;

        if(ChessBoard.piece_squares[row][col] != null && ChessBoard.piece_squares[row][col].getColor().equals(this.getColor()))
        {
            return false;
        }

        if (currentCol == col)
        {
            if(row<currentRow)
            {
                for(int r=currentRow-1; r>row; r--)
                {
                    if (ChessBoard.piece_squares[r][col]!=null)
                    {
                        return false;
                    }
                }
            }
            if(currentRow<row)
            {
                for(int r=currentRow+1; r<row; r++)
                {
                    if(ChessBoard.piece_squares[r][col]!=null)
                    {
                        return false;
                    }
                }
            }

        }
        else
        {
            if(col<currentCol)
            {
                for(int c=currentCol-1; c>col; c--)
                {
                    if (ChessBoard.piece_squares[row][c]!=null)
                    {
                        return false;
                    }
                }
            }
            if(currentCol<col)
            {
                for(int c=currentCol+1; c<col; c++)
                {
                    if(ChessBoard.piece_squares[row][c]!=null)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
